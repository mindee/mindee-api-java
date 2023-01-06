package com.mindee.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.ParseParameter;
import com.mindee.parsing.CustomEndpoint;
import com.mindee.parsing.CustomEndpointInfo;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.MindeeApi;
import com.mindee.MindeeSettings;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.utils.MindeeException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public final class MindeeHttpApi implements MindeeApi {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final MindeeSettings mindeeSettings;

  public MindeeHttpApi(MindeeSettings mindeeSettings) {
    this.mindeeSettings = mindeeSettings;
  }

  public <T extends Inference> Document<T> predict(
    Class<T> clazz,
    ParseParameter parseParameter) throws MindeeException, IOException {

    EndpointInfo endpointAnnotation = clazz.getAnnotation(EndpointInfo.class);
    CustomEndpoint customEndpoint;

    // that means it could be custom document
    if(endpointAnnotation == null) {
      CustomEndpointInfo customEndpointAnnotation = clazz.getAnnotation(CustomEndpointInfo.class);
      if(customEndpointAnnotation == null) {
        throw new MindeeException("The class is not supported as a prediction model. " +
          "The endpoint attribute is missing. " +
          "Please refer to the document or contact the support.");
      }
      customEndpoint = new CustomEndpoint(
        customEndpointAnnotation.endpointName(),
        customEndpointAnnotation.accountName(),
        customEndpointAnnotation.version());
    }
    else {
      customEndpoint = new CustomEndpoint(
        endpointAnnotation.endpointName(),
        endpointAnnotation.accountName(),
        endpointAnnotation.version());
    }

    return predict(clazz, customEndpoint, parseParameter);
  }

  public <T extends Inference> Document<T> predict(
    Class<T> clazz,
    CustomEndpoint customEndpoint,
    ParseParameter parseParameter)
    throws MindeeException, IOException {

    // required, to register jackson dateonly module format to deserialize
    mapper.findAndRegisterModules();

    HttpPost post = new HttpPost(buildUrl(customEndpoint));
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("document", parseParameter.getFile(), ContentType.DEFAULT_BINARY, parseParameter.getFileName());
    if (Boolean.TRUE.equals(parseParameter.getIncludeWords())) {
      builder.addTextBody("include_mvision", "true");
    }

    HttpEntity entity = builder.build();
    post.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey());
    post.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    post.setEntity(entity);

    String errorMessage = "Mindee API client : ";
    PredictResponse<T> predictResponse;

    try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
         CloseableHttpResponse response = httpClient.execute(post)) {
      HttpEntity responseEntity = response.getEntity();

      if (responseEntity.getContentLength() != 0) {
        JavaType type = mapper.getTypeFactory().constructParametricType(
          PredictResponse.class,
          clazz);
        predictResponse = mapper.readValue(
          responseEntity.getContent(), type);

        if (is2xxStatusCode(response.getStatusLine().getStatusCode())) {
          return predictResponse.getDocument();
        }

        if (predictResponse != null) {
          errorMessage += predictResponse.getApiRequest().getError().toString();

        }
      }
      ByteArrayOutputStream contentRead = new ByteArrayOutputStream();
      byte[] buffer = new byte[1024];
      for (int length; (length = responseEntity.getContent().read(buffer)) != -1; ) {
        contentRead.write(buffer, 0, length);
      }
      errorMessage += " Unhandled - HTTP Status code " + response.getStatusLine().getStatusCode() + " - Content " + contentRead.toString("UTF-8");
    } catch (IOException e) {
      throw new MindeeException(e.getMessage(), e);
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

    return this.mindeeSettings.getBaseUrl() + "/products/" +
      customEndpoint.getAccountName() +
      "/" +
      customEndpoint.getEndpointName() +
      "/v" +
      customEndpoint.getVersion() +
      "/predict";
  }
}
