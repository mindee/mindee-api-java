package com.mindee.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;


public class MindeeHttpClient implements DocumentParsingHttpClient {


  private static final ObjectMapper mapper = new ObjectMapper();

  public Map parse(InputStream inputStream, String filename, String apiKey, String endPoint)
      throws IOException {
    return parse(inputStream, filename, apiKey, endPoint, Boolean.FALSE);
  }

  public Map parse(InputStream inputStream, String filename, String apiKey, String endPoint,
      Boolean includeWords)
      throws IOException {

    HttpPost post = new HttpPost(endPoint);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("document", inputStream, ContentType.DEFAULT_BINARY, filename);
    if (includeWords) {
      builder.addTextBody("include_mvision", "true");
    }

    HttpEntity entity = builder.build();
    post.setHeader(HttpHeaders.AUTHORIZATION, apiKey);
    post.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    post.setEntity(entity);

    try (CloseableHttpClient httpClient = HttpClientBuilder.create()
        .build(); CloseableHttpResponse response = httpClient.execute(post)) {

      if (is2xxStatusCode(response.getStatusLine().getStatusCode())) {
        HttpEntity responseEntity = response.getEntity();
        Map resultMap = mapper.readValue(responseEntity.getContent(), Map.class);
        return resultMap;
      }


    } finally {
      inputStream.close();
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
}
