package com.mindee.http;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.ParseParameter;
import com.mindee.parsing.EndpointInfo;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.lang.annotation.Annotation;

public final class MindeeApi {

  private static final String MINDEE_API_URL = "https://api.mindee.net/v1";

  private static final ObjectMapper mapper = new ObjectMapper();

  public <T extends Inference> Document<T> predict(
    Class<T> clazz,
    ParseParameter parseParameter)
      throws IOException {
    mapper.findAndRegisterModules();
    // Class<T> class =
    Annotation endpointAnnotation = clazz.getAnnotation(EndpointInfo.class);

    HttpPost post = new HttpPost(buildUrl());
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("document", parseParameter.getFileStream(), ContentType.DEFAULT_BINARY, parseParameter.getFileName());
    if (Boolean.TRUE.equals(parseParameter.getIncludeWords())) {
      builder.addTextBody("include_mvision", "true");
    }

    HttpEntity entity = builder.build();
    post.setHeader(HttpHeaders.AUTHORIZATION, apiKey);
    post.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    post.setEntity(entity);

    PredictResponse<Document<T>> predictResponse = null;

    try (CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        CloseableHttpResponse response = httpClient.execute(post)) {

      if (is2xxStatusCode(response.getStatusLine().getStatusCode())) {
        HttpEntity responseEntity = response.getEntity();
        JavaType type = mapper.getTypeFactory().constructParametricType(
            PredictResponse.class,
            clazz);
        predictResponse = mapper.readValue(
            responseEntity.getContent(), type);

        return (Document<T>) predictResponse.getDocument();
      }
    } finally {
      parseParameter.getFileStream().close();
    }

    return null;
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

  private static String buildUrl(Endpoint endpoint) {
    StringBuilder builder = new StringBuilder(MINDEE_API_URL);
    builder.append("/products/");
    builder.append(endpoint.getOwner());
    builder.append("/");
    builder.append(endpoint.getUrlName());
    builder.append("/v");
    builder.append(endpoint.getVersion());
    builder.append("/predict");
    return builder.toString();
  }
}
