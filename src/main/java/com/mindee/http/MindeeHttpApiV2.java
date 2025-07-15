package com.mindee.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.InferenceOptions;
import com.mindee.MindeeException;
import com.mindee.MindeeSettingsV2;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

/**
 * HTTP Client class for the V2 API.
 */
public final class MindeeHttpApiV2 extends MindeeApiV2 {

  private static final ObjectMapper mapper = new ObjectMapper();

  /**
   * The MindeeSetting needed to make the api call.
   */
  private final MindeeSettingsV2 mindeeSettings;
  /**
   * The HttpClientBuilder used to create HttpClient objects used to make api calls over http.
   * Defaults to HttpClientBuilder.create().useSystemProperties()
   */
  private final HttpClientBuilder httpClientBuilder;


  public MindeeHttpApiV2(MindeeSettingsV2 mindeeSettings) {
    this(
        mindeeSettings,
        null
    );
  }

  @Builder
  private MindeeHttpApiV2(
      MindeeSettingsV2 mindeeSettings,
      HttpClientBuilder httpClientBuilder
  ) {
    this.mindeeSettings = mindeeSettings;

    if (httpClientBuilder != null) {
      this.httpClientBuilder = httpClientBuilder;
    } else {
      this.httpClientBuilder = HttpClientBuilder.create().useSystemProperties();
    }
  }

  /**
   * Enqueues a doc with the POST method.
   *
   * @param inputSource Input source to send.
   * @param options     Options to send the file along with.
   * @return A job response.
   */
  public JobResponse enqueuePost(
      LocalInputSource inputSource,
      InferenceOptions options
  ) {
    String url = this.mindeeSettings.getBaseUrl() + "/inferences/enqueue";
    HttpPost post = buildHttpPost(url, inputSource, options);

    mapper.findAndRegisterModules();
    try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
      return httpClient.execute(
          post, response -> {
            String raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return deserializeOrThrow(raw, JobResponse.class, response.getCode());
          }
      );
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  public CommonResponse getInferenceFromQueue(
      String jobId
  ) {

    String url = this.mindeeSettings.getBaseUrl() + "/inferences/" + jobId;
    HttpGet get = new HttpGet(url);

    if (this.mindeeSettings.getApiKey().isPresent()) {
      get.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    get.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    mapper.findAndRegisterModules();
    try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
      return httpClient.execute(
          get, response -> {
            HttpEntity responseEntity = response.getEntity();
            int statusCode = response.getCode();
            if (!is2xxStatusCode(statusCode)) {
              throw getHttpError(response);
            }
            try {
              String raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

              return deserializeOrThrow(raw, InferenceResponse.class, response.getCode());
            } finally {
              /* make sure the connection can be reused even if parsing fails */
              EntityUtils.consumeQuietly(responseEntity);
            }
          }
      );
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  private MindeeHttpExceptionV2 getHttpError(ClassicHttpResponse response) {
    String rawBody;
    try {
      rawBody = response.getEntity() == null
          ? ""
          : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

      ErrorResponse err = mapper.readValue(rawBody, ErrorResponse.class);

      if (err.getDetail() == null) {
        err = new ErrorResponse("Unknown error", response.getCode());
      }
      return new MindeeHttpExceptionV2(err.getStatus(), err.getDetail());

    } catch (Exception e) {
      return new MindeeHttpExceptionV2(response.getCode(), "Unknown error");
    }
  }


  private HttpEntity buildHttpBody(
      LocalInputSource inputSource,
      InferenceOptions options
  ) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder.addBinaryBody(
        "file",
        inputSource.getFile(),
        ContentType.DEFAULT_BINARY,
        inputSource.getFilename()
    );

    if (options.getAlias() != null) {
      builder.addTextBody(
          "alias",
          options.getAlias().toLowerCase()
      );
    }

    builder.addTextBody("model_id", options.getModelId());
    if (options.isFullText()) {
      builder.addTextBody("full_text_ocr", "true");
    }
    if (options.isRag()) {
      builder.addTextBody("rag", "true");
    }
    if (options.getAlias() != null) {
      builder.addTextBody("alias", options.getAlias());
    }
    if (!options.getWebhookIds().isEmpty()) {
      builder.addTextBody("webhook_ids", String.join(",", options.getWebhookIds()));
    }
    return builder.build();
  }


  private HttpPost buildHttpPost(
      String url,
      LocalInputSource inputSource,
      InferenceOptions options
  ) {
    HttpPost post;
    try {
      URIBuilder uriBuilder = new URIBuilder(url);
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
    post.setEntity(buildHttpBody(inputSource, options));
    return post;
  }


  private <R extends CommonResponse> R deserializeOrThrow(
      String body, Class<R> clazz, int httpStatus) throws MindeeHttpExceptionV2 {

    if (httpStatus >= 200 && httpStatus < 300) {
      try {
        R model = mapper.readerFor(clazz).readValue(body);
        model.setRawResponse(body);
        return model;
      } catch (Exception exception) {
        throw new MindeeException("Couldn't deserialize server response:\n" + exception.getMessage());
      }
    }

    ErrorResponse err;
    try {
      err = mapper.readValue(body, ErrorResponse.class);
      if (err.getDetail() == null) {
        err = new ErrorResponse("Unknown error", httpStatus);
      }
    } catch (Exception ignored) {
      err = new ErrorResponse("Unknown error", httpStatus);
    }
    throw new MindeeHttpExceptionV2(err.getStatus(), err.getDetail());
  }
}
