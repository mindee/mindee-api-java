package com.mindee.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.InferencePredictOptions;
import com.mindee.MindeeException;
import com.mindee.MindeeSettingsV2;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.AsyncInferenceResponse;
import com.mindee.parsing.v2.AsyncJobResponse;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.ErrorResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
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
import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.message.BasicNameValuePair;
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
  public AsyncJobResponse enqueuePost(
      LocalInputSource inputSource,
      InferencePredictOptions options
  ) {
    String url = this.mindeeSettings.getBaseUrl() + "/inferences/enqueue";
    HttpPost post = buildHttpPost(url, inputSource, options);

    mapper.findAndRegisterModules();
    try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
      return httpClient.execute(
          post, response -> {
            String raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
            return deserializeOrThrow(raw, AsyncJobResponse.class, response.getCode());
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

              return deserializeOrThrow(raw, AsyncInferenceResponse.class, response.getCode());
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

  private List<NameValuePair> buildPostParams(
      InferencePredictOptions options
  ) {
    ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("model_id", options.getModelId()));
    if (options.isFullText()) {
      params.add(new BasicNameValuePair("full_text_ocr", "true"));
    }
    if (options.isRag()) {
      params.add(new BasicNameValuePair("rag", "true"));
    }
    if (options.getAlias() != null) {
      params.add(new BasicNameValuePair("alias", options.getAlias()));
    }
    if (!options.getWebhookIds().isEmpty()) {
      params.add(new BasicNameValuePair("webhook_ids", String.join(",", options.getWebhookIds())));
    }
    return params;
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
      InferencePredictOptions options
  ) {
    MultipartEntityBuilder builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder.addBinaryBody(
        "document",
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
    return builder.build();
  }


  private HttpPost buildHttpPost(
      String url,
      LocalInputSource inputSource,
      InferencePredictOptions options
  ) {
    HttpPost post;
    try {
      URIBuilder uriBuilder = new URIBuilder(url);
      uriBuilder.addParameters(buildPostParams(options));
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
        R model = mapper.readValue(body, clazz);
        model.setRawResponse(body);
        return model;
      } catch (Exception ignored) {
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
