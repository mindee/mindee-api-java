package com.mindee.http;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeException;
import com.mindee.MindeeSettings;
import com.mindee.parsing.common.ApiResponse;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.ErrorDetails;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.Builder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

/**
 * HTTP Client class.
 */
public final class MindeeHttpApi extends MindeeApi {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final Function<Endpoint, String> buildBaseUrl = this::buildUrl;
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
  private final Function<Endpoint, String> urlFromEndpoint;
  /**
   * The function used to generate the API endpoint URL for Async calls. Only needs to be set if the
   * api calls need to be directed through internal URLs.
   */
  private final Function<Endpoint, String> asyncUrlFromEndpoint;
  /**
   * The function used to generate the Job status URL for Async calls. Only needs to be set if the
   * api calls need to be directed through internal URLs.
   */
  private final Function<Endpoint, String> documentUrlFromEndpoint;

  public MindeeHttpApi(MindeeSettings mindeeSettings) {
    this(
        mindeeSettings,
        null,
        null,
        null,
        null
    );
  }

  @Builder
  private MindeeHttpApi(
      MindeeSettings mindeeSettings,
      HttpClientBuilder httpClientBuilder,
      Function<Endpoint, String> urlFromEndpoint,
      Function<Endpoint, String> asyncUrlFromEndpoint,
      Function<Endpoint, String> documentUrlFromEndpoint
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

    if (documentUrlFromEndpoint != null) {
      this.documentUrlFromEndpoint = documentUrlFromEndpoint;
    } else {
      this.documentUrlFromEndpoint = this.buildBaseUrl.andThen(
          (url) -> url.concat("/documents/queue/"));
    }
  }

  /**
   * GET job status and document for an enqueued job
   */
  public <DocT extends Inference> AsyncPredictResponse<DocT> documentQueueGet(
      Class<DocT> documentClass,
      Endpoint endpoint,
      String jobId
  ) {
    String endpointUrl = documentUrlFromEndpoint.apply(endpoint).concat(jobId);
    HttpGet get = new HttpGet(endpointUrl);

    // required to register jackson date module format to deserialize
    mapper.findAndRegisterModules();
    JavaType parametricType = mapper.getTypeFactory().constructParametricType(
        AsyncPredictResponse.class,
        documentClass
    );

    if (this.mindeeSettings.getApiKey().isPresent()) {
      get.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    get.setHeader(HttpHeaders.USER_AGENT, getUserAgent());

    try (
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(get)
    ) {
      HttpEntity responseEntity = response.getEntity();
      int statusCode = response.getStatusLine().getStatusCode();
      if (!is2xxStatusCode(statusCode)) {
        throw getHttpError(parametricType, response);
      }
      String rawResponse = readRawResponse(responseEntity);
      AsyncPredictResponse<DocT> mappedResponse = mapper.readValue(rawResponse, parametricType);
      mappedResponse.setRawResponse(rawResponse);
      if (
          mappedResponse.getJob() != null
          && mappedResponse.getJob().getError() != null
          && mappedResponse.getJob().getError().getCode() != null
      ) {
        throw new MindeeHttpException(
          500,
          mappedResponse.getJob().getError().getMessage(),
          mappedResponse.getJob().getError().getDetails().toString(),
          mappedResponse.getJob().getError().getCode()
        );
      }
      return mappedResponse;
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  /**
   * POST a prediction request for a custom product.
   */
  public <DocT extends Inference> PredictResponse<DocT> predictPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException {

    String url = urlFromEndpoint.apply(endpoint);
    HttpPost post = buildHttpPost(url, requestParameters);

    // required to register jackson date module format to deserialize
    mapper.findAndRegisterModules();
    JavaType parametricType = mapper.getTypeFactory().constructParametricType(
        PredictResponse.class,
        documentClass
    );
    try (
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(post)
    ) {
      HttpEntity responseEntity = response.getEntity();
      int statusCode = response.getStatusLine().getStatusCode();
      if (!is2xxStatusCode(statusCode)) {
        throw getHttpError(parametricType, response);
      }
      if (responseEntity.getContentLength() == 0) {
        throw new MindeeException("Empty response from server.");
      }
      String rawResponse = readRawResponse(responseEntity);
      PredictResponse<DocT> mappedResponse = mapper.readValue(rawResponse, parametricType);
      mappedResponse.setRawResponse(rawResponse);
      return mappedResponse;
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  /**
   * POST a prediction request for a custom product.
   */
  public <DocT extends Inference> AsyncPredictResponse<DocT> predictAsyncPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException {

    String url = asyncUrlFromEndpoint.apply(endpoint);
    HttpPost post = buildHttpPost(url, requestParameters);

    // required to register jackson date module format to deserialize
    mapper.findAndRegisterModules();
    JavaType parametricType = mapper.getTypeFactory().constructParametricType(
        AsyncPredictResponse.class,
        documentClass
    );
    try (
        CloseableHttpClient httpClient = httpClientBuilder.build();
        CloseableHttpResponse response = httpClient.execute(post)
    ) {
      HttpEntity responseEntity = response.getEntity();
      int statusCode = response.getStatusLine().getStatusCode();
      if (!is2xxStatusCode(statusCode)) {
        throw getHttpError(parametricType, response);
      }
      if (responseEntity.getContentLength() == 0) {
        throw new MindeeException("Empty response from server.");
      }
      String rawResponse = readRawResponse(responseEntity);
      AsyncPredictResponse<DocT> mappedResponse = mapper.readValue(rawResponse, parametricType);
      mappedResponse.setRawResponse(rawResponse);
      return mappedResponse;
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  private <ResponseT extends ApiResponse> MindeeHttpException getHttpError(
      JavaType parametricType,
      CloseableHttpResponse response
  ) {
    int statusCode = response.getStatusLine().getStatusCode();
    String message = "HTTP Status " + statusCode + " - ";
    String details;
    String errorCode;
    String rawResponse;
    try {
      rawResponse = readRawResponse(response.getEntity());
    } catch (IOException err) {
      message += "Could not read server response, check details.";
      errorCode = "";
      details = err.getMessage();
      return new MindeeHttpException(statusCode, message, details, errorCode);
    }
    try {
      ResponseT predictResponse = mapper.readValue(rawResponse, parametricType);
      message = predictResponse.getApiRequest().getError().getMessage();
      ErrorDetails errorDetails = predictResponse.getApiRequest().getError().getDetails();
      if (errorDetails != null) {
        details = errorDetails.toString();
      }
      else {
        details = "";
      }
      errorCode = predictResponse.getApiRequest().getError().getCode();
    } catch (IOException mapperError) {
      message += "Unhandled server response, check details.";
      details = rawResponse;
      errorCode = "";
    }
    return new MindeeHttpException(statusCode, message, details, errorCode);
  }

  private String buildUrl(Endpoint endpoint) {
    return this.mindeeSettings.getBaseUrl()
        + "/products/"
        + endpoint.getAccountName()
        + "/"
        + endpoint.getEndpointName()
        + "/v"
        + endpoint.getVersion();
  }

  private HttpPost buildHttpPost(
      String url,
      RequestParameters requestParameters
  ) throws JsonProcessingException {
    HttpPost post;
    try {
      URIBuilder uriBuilder = new URIBuilder(url);
      uriBuilder.addParameters(buildPostParams(requestParameters));
      post = new HttpPost(uriBuilder.build());
    }
    // This exception will never happen because we are providing the URL internally.
    // Do this to avoid declaring the exception in the method signature.
    catch (URISyntaxException err) {
      return new HttpPost("invalid URI");
    }

    if (this.mindeeSettings.getApiKey().isPresent()) {
      post.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    post.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    post.setEntity(buildHttpBody(requestParameters));
    return post;
  }

  private List<NameValuePair> buildPostParams(
      RequestParameters requestParameters
  ) {
    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    if (Boolean.TRUE.equals(requestParameters.getPredictOptions().getCropper())) {
      params.add(new BasicNameValuePair("cropper", "true"));
    }
    return params;
  }

  private HttpEntity buildHttpBody(
      RequestParameters requestParameters
  ) throws JsonProcessingException {
    if (requestParameters.getFile() != null) {
      MultipartEntityBuilder builder = MultipartEntityBuilder.create();
      builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
      builder.addBinaryBody(
          "document",
          requestParameters.getFile(),
          ContentType.DEFAULT_BINARY,
          requestParameters.getFileName()
      );
      if (Boolean.TRUE.equals(requestParameters.getPredictOptions().getAllWords())) {
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
      throw new MindeeException("Either document bytes or a document URL are needed");
    }
  }
}
