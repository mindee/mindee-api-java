package com.mindee.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeSettings;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.utils.MindeeException;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

/**
 * HTTP Client class.
 */
public final class MindeeHttpApi implements MindeeApi {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final Function<CustomEndpoint, String> buildBaseUrl = this::buildUrl;
  /**
   * The MindeeSetting needed to make the api call.
   */
  private final MindeeSettings mindeeSettings;
  /**
   * The HttpClientBuilder used to create HttpClient objects used to make api calls over http.
   * Defaults to HttpClientBuilder.create().useSystemProperties()
   */
  private final HttpClientBuilder httpClientBuilder;
  /**
   * The function used to generate the API endpoint URL. Only needs to be set if the api calls need
   * to be directed through internal URLs.
   */
  private final Function<CustomEndpoint, String> urlFromEndpoint;
  /**
   * The function used to generate the API endpoint URL for Async calls. Only needs to be set if the
   * api calls need to be directed through internal URLs.
   */
  private final Function<CustomEndpoint, String> asyncUrlFromEndpoint;
  /**
   * The function used to generate the Job status URL for Async calls. Only needs to be set if the
   * api calls need to be directed through internal URLs.
   */
  private final Function<CustomEndpoint, String> jobStatusUrlFromEndpoint;

  public MindeeHttpApi(MindeeSettings mindeeSettings) {
    this(mindeeSettings, null, null, null, null);
  }

  @Builder
  private MindeeHttpApi(
      MindeeSettings mindeeSettings,
      HttpClientBuilder httpClientBuilder,
      Function<CustomEndpoint, String> urlFromEndpoint,
      Function<CustomEndpoint, String> asyncUrlFromEndpoint,
      Function<CustomEndpoint, String> jobStatusUrlFromEndpoint
  ) {
    this.mindeeSettings = mindeeSettings;

    if (httpClientBuilder != null) {
      this.httpClientBuilder = httpClientBuilder;
    } else {
      this.httpClientBuilder = HttpClientBuilder.create().useSystemProperties();
    }

    if (urlFromEndpoint != null) {
      this.urlFromEndpoint = urlFromEndpoint;
    } else {
      this.urlFromEndpoint = buildBaseUrl.andThen((url) -> url.concat("/predict"));
    }

    if (asyncUrlFromEndpoint != null) {
      this.asyncUrlFromEndpoint = asyncUrlFromEndpoint;
    } else {
      this.asyncUrlFromEndpoint = this.urlFromEndpoint.andThen((url) -> url.concat("_async"));
    }

    if (jobStatusUrlFromEndpoint != null) {
      this.jobStatusUrlFromEndpoint = jobStatusUrlFromEndpoint;
    } else {
      this.jobStatusUrlFromEndpoint = this.buildBaseUrl.andThen(
          (url) -> url.concat("/documents/queue/"));
    }
  }

  /**
   * GET job status and document for an enqued job
   */
  public <DocT extends Inference> PredictResponse<DocT> checkJobStatus(
      Class<DocT> documentClass, String jobId) {
    // required to register jackson date module format to deserialize
    mapper.findAndRegisterModules();
    CustomEndpoint customEndpoint = customEndpointFromClass(documentClass);
    String endpoint = jobStatusUrlFromEndpoint.apply(customEndpoint).concat(jobId);
    HttpGet get = new HttpGet(endpoint);

    if (this.mindeeSettings.getApiKey().isPresent()) {
      get.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    get.setHeader(HttpHeaders.USER_AGENT, getUserAgent());

    try (CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(get)) {
      HttpEntity responseEntity = response.getEntity();

      if (is2xxStatusCode(response.getStatusLine().getStatusCode())) {
        JavaType type = mapper.getTypeFactory().constructParametricType(
            PredictResponse.class,
            documentClass
        );
        return mapper.readValue(responseEntity.getContent(), type);
      } else {
        String errorMessage = "Mindee API client: Unhandled - HTTP Status code "
            + response.getStatusLine().getStatusCode();
        throw new MindeeException(errorMessage);
      }
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  /**
   * POST a prediction request for a standard product.
   */
  public <DocT extends Inference> PredictResponse<DocT> predict(
      Class<DocT> documentClass,
      RequestParameters requestParameters
  ) throws IOException {
    CustomEndpoint customEndpoint = customEndpointFromClass(documentClass);
    return predict(documentClass, customEndpoint, requestParameters);
  }

  /**
   * POST a prediction request for a custom product.
   */
  public <DocT extends Inference> PredictResponse<DocT> predict(
      Class<DocT> documentClass,
      CustomEndpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException {

    // required to register jackson date module format to deserialize
    mapper.findAndRegisterModules();

    String url = requestParameters.getAsyncCall() ? asyncUrlFromEndpoint.apply(endpoint)
        : urlFromEndpoint.apply(endpoint);
    HttpPost post = new HttpPost(url);
    HttpEntity entity = buildHttpBody(requestParameters);
    if (this.mindeeSettings.getApiKey().isPresent()) {
      post.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    post.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    post.setEntity(entity);

    String errorMessage = "Mindee API client: ";
    PredictResponse<DocT> predictResponse;

    try (
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(post)
    ) {
      HttpEntity responseEntity = response.getEntity();

      if (responseEntity.getContentLength() != 0) {
        JavaType type = mapper.getTypeFactory().constructParametricType(
            PredictResponse.class,
            documentClass
        );
        predictResponse = mapper.readValue(responseEntity.getContent(), type);

        if (is2xxStatusCode(response.getStatusLine().getStatusCode())) {
          return predictResponse;
        }
        if (predictResponse != null) {
          errorMessage += predictResponse.getApiRequest().getError().toString();
        }

      } else {
        ByteArrayOutputStream contentRead = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int length; (length = responseEntity.getContent().read(buffer)) != -1; ) {
          contentRead.write(buffer, 0, length);
        }
        errorMessage += " Unhandled - HTTP Status code "
            + response.getStatusLine().getStatusCode()
            + " - Content "
            + contentRead.toString("UTF-8");
      }
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }

    throw new MindeeException(errorMessage);
  }


  private boolean is2xxStatusCode(int statusCode) {
    return statusCode >= 200 && statusCode <= 299;
  }

  private String getUserAgent() {
    String javaVersion = System.getProperty("java.version");
    String sdkVersion = getClass().getPackage().getImplementationVersion();
    String osName = System.getProperty("os.name").toLowerCase();

    if (osName.contains("windows")) {
      osName = "windows";
    } else if (osName.contains("darwin")) {
      osName = "macos";
    } else if (osName.contains("mac")) {
      osName = "macos";
    } else if (osName.contains("linux")) {
      osName = "linux";
    } else if (osName.contains("freebsd")) {
      osName = "freebsd";
    } else if (osName.contains("aix")) {
      osName = "aix";
    }

    return String.format("mindee-api-java@v%s java-v%s %s", sdkVersion, javaVersion, osName);
  }

  private String buildUrl(CustomEndpoint customEndpoint) {

    return this.mindeeSettings.getBaseUrl()
        + "/products/"
        + customEndpoint.getAccountName()
        + "/"
        + customEndpoint.getEndpointName()
        + "/v"
        + customEndpoint.getVersion();
  }

  private HttpEntity buildHttpBody(RequestParameters requestParameters)
      throws JsonProcessingException, UnsupportedEncodingException {
    if (requestParameters.getFile() != null) {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.addBinaryBody(
          "document",
          requestParameters.getFile(),
          ContentType.DEFAULT_BINARY,
          requestParameters.getFileName()
      );
      if (Boolean.TRUE.equals(requestParameters.getAllWords())) {
        builder.addTextBody("include_mvision", "true");
      }
      return builder.build();
    } else if (requestParameters.getFileUrl() != null) {
      Map<String, URL> urlMap = new HashMap<>();
      urlMap.put("document", requestParameters.getFileUrl());
      final StringEntity entity = new StringEntity(mapper.writeValueAsString(urlMap),
          ContentType.APPLICATION_JSON);
      return entity;
    } else {
      throw new MindeeException("Either document bytes or a document url are needed");
    }

  }

  private <DocT extends Inference> CustomEndpoint customEndpointFromClass(
      Class<DocT> documentClass) {
    EndpointInfo endpointAnnotation = documentClass.getAnnotation(EndpointInfo.class);
    CustomEndpoint customEndpoint;

    // that means it could be custom document
    if (endpointAnnotation == null) {
      CustomEndpointInfo customEndpointAnnotation = documentClass.getAnnotation(
          CustomEndpointInfo.class
      );
      if (customEndpointAnnotation == null) {
        throw new MindeeException(
            "The class is not supported as a prediction model. "
                + "The endpoint attribute is missing. "
                + "Please refer to the document or contact the support."
        );
      }
      customEndpoint = new CustomEndpoint(
          customEndpointAnnotation.endpointName(),
          customEndpointAnnotation.accountName(),
          customEndpointAnnotation.version());
    } else {
      customEndpoint = new CustomEndpoint(
          endpointAnnotation.endpointName(),
          endpointAnnotation.accountName(),
          endpointAnnotation.version());
    }
    return customEndpoint;
  }
}
