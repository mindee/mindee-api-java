package com.mindee.parsing;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.ParseParameter;
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

import java.io.IOException;

public final class MindeeApi {

  private static final ObjectMapper mapper = new ObjectMapper();
  private final String apiKey;
  private final String baseUrl;

  public MindeeApi() {
    this.apiKey = System.getenv("MINDEE_API_KEY");
    this.baseUrl = System.getenv("MINDEE_API_URL");
  }

  public MindeeApi(String apiKey) {
    this(apiKey, System.getenv("MINDEE_API_URL"));
  }

  public MindeeApi(String apiKey, String baseUrl) {
    this.apiKey = apiKey != null ? apiKey : System.getenv("MINDEE_API_KEY");
    this.baseUrl = baseUrl;
  }

  public <T extends Inference> Document<T> predict(
    Class<T> clazz,
    ParseParameter parseParameter) throws IOException, RuntimeException {

    // required, to register jackson dateonly module format to deserialize
    mapper.findAndRegisterModules();

    EndpointInfo endpointAnnotation = clazz.getAnnotation(EndpointInfo.class);

    HttpPost post = new HttpPost(buildUrl(endpointAnnotation));
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
    builder.addBinaryBody("document", parseParameter.getFileStream(), ContentType.DEFAULT_BINARY, parseParameter.getFileName());
    if (Boolean.TRUE.equals(parseParameter.getIncludeWords())) {
      builder.addTextBody("include_mvision", "true");
    }

    HttpEntity entity = builder.build();
    post.setHeader(HttpHeaders.AUTHORIZATION, this.apiKey);
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
      }
    } catch (IOException e) {
      throw new MindeeException(e.getMessage(), e);
    } finally {
      parseParameter.getFileStream().close();
    }

    return (Document<T>) predictResponse.getDocument();
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

  private String buildUrl(EndpointInfo endpointInfo) {

    if (endpointInfo == null) {
      throw new MindeeException("The endpoint attribute is missing. " +
        "Please refer to the document or contact the support.");
    }

    return this.baseUrl + "/products/" +
      endpointInfo.accountName() +
      "/" +
      endpointInfo.endpointName() +
      "/v" +
      endpointInfo.version() +
      "/predict";
  }
}
