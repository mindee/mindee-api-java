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

    HttpPost post = new HttpPost(endPoint);
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("document", inputStream, ContentType.DEFAULT_BINARY, filename);
    HttpEntity entity = builder.build();
    post.setHeader(HttpHeaders.AUTHORIZATION, apiKey);
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
}
