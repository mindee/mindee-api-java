package com.mindee.v2.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.MindeeSettings;
import com.mindee.v2.clientoptions.BaseParameters;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.ErrorResponse;
import com.mindee.v2.parsing.JobResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.net.URIBuilder;

/**
 * HTTP Client class for the V2 API.
 */
public final class MindeeHttpApiV2 extends MindeeApiV2 {

  private static final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

  /**
   * The MindeeSetting needed to make the api call.
   */
  private final MindeeSettings mindeeSettings;
  /**
   * The HttpClientBuilder used to create HttpClient objects used to make api calls over http.
   * Defaults to HttpClientBuilder.create().useSystemProperties()
   */
  private final HttpClientBuilder httpClientBuilder;

  public MindeeHttpApiV2(MindeeSettings mindeeSettings) {
    this(mindeeSettings, null);
  }

  @Builder
  private MindeeHttpApiV2(MindeeSettings mindeeSettings, HttpClientBuilder httpClientBuilder) {
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
   * @param options Options to send the file along with.
   * @return A job response.
   */
  @Override
  public JobResponse reqPostEnqueue(LocalInputSource inputSource, BaseParameters options) {
    var productInfo = getParamsProductInfo(options.getClass());
    var url = String
      .format("%s/products/%s/enqueue", this.mindeeSettings.getBaseUrl(), productInfo.slug());
    var post = buildHttpPost(url);

    var builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder
      .addBinaryBody(
        "file",
        inputSource.getFile(),
        ContentType.DEFAULT_BINARY,
        inputSource.getFilename()
      );
    post.setEntity(options.buildHttpBody(builder).build());
    return executeEnqueue(post);
  }

  /**
   * Enqueues a doc with the POST method.
   *
   * @param inputSource Input source to send.
   * @param options Options to send the file along with.
   * @return A job response.
   */
  @Override
  public JobResponse reqPostEnqueue(URLInputSource inputSource, BaseParameters options) {
    var productInfo = getParamsProductInfo(options.getClass());
    var url = String
      .format("%s/products/%s/enqueue", this.mindeeSettings.getBaseUrl(), productInfo.slug());
    var post = buildHttpPost(url);

    var builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder.addTextBody("url", inputSource.getUrl().toString());
    post.setEntity(options.buildHttpBody(builder).build());
    return executeEnqueue(post);
  }

  /**
   * Executes an enqueue action, common to URL & local inputs.
   *
   * @param post HTTP Post object.
   * @return a valid job response.
   */
  private JobResponse executeEnqueue(HttpPost post) {
    try (var httpClient = httpClientBuilder.build()) {
      return httpClient.execute(post, response -> {
        var responseEntity = response.getEntity();
        var statusCode = response.getCode();
        if (isInvalidStatusCode(statusCode)) {
          throw getHttpError(response);
        }
        try {
          var raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          return deserializeOrThrow(raw, JobResponse.class, response.getCode());
        } finally {
          EntityUtils.consumeQuietly(responseEntity);
        }
      });
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  @Override
  public JobResponse reqGetJob(String jobId) {

    var url = this.mindeeSettings.getBaseUrl() + "/jobs/" + jobId;
    var get = new HttpGet(url);

    if (this.mindeeSettings.getApiKey().isPresent()) {
      get.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    get.setHeader(HttpHeaders.USER_AGENT, getUserAgent());
    var noRedirect = RequestConfig.custom().setRedirectsEnabled(false).build();
    get.setConfig(noRedirect);

    try (var httpClient = httpClientBuilder.build()) {
      return httpClient.execute(get, response -> {
        var responseEntity = response.getEntity();
        var statusCode = response.getCode();
        if (isInvalidStatusCode(statusCode)) {
          throw getHttpError(response);
        }
        try {
          var raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

          return deserializeOrThrow(raw, JobResponse.class, response.getCode());
        } finally {
          EntityUtils.consumeQuietly(responseEntity);
        }
      });
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  @Override
  public <TResponse extends CommonResponse> TResponse reqGetResult(
      Class<TResponse> responseClass,
      String inferenceId
  ) {
    var productInfo = getResponseProductInfo(responseClass);
    var url = String
      .format(
        "%s/products/%s/results/%s",
        this.mindeeSettings.getBaseUrl(),
        productInfo.slug(),
        inferenceId
      );
    var get = new HttpGet(url);

    if (this.mindeeSettings.getApiKey().isPresent()) {
      get.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    get.setHeader(HttpHeaders.USER_AGENT, getUserAgent());

    try (var httpClient = httpClientBuilder.build()) {

      return httpClient.execute(get, response -> {
        var entity = response.getEntity();
        var status = response.getCode();
        try {
          if (isInvalidStatusCode(status)) {
            throw getHttpError(response);
          }
          var raw = EntityUtils.toString(entity, StandardCharsets.UTF_8);
          return deserializeOrThrow(raw, responseClass, status);
        } finally {
          EntityUtils.consumeQuietly(entity);
        }
      });
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

      var err = mapper.readValue(rawBody, ErrorResponse.class);

      if (err.getDetail() == null) {
        err = makeUnknownError(response.getCode());
      }
      return new MindeeHttpExceptionV2(err.getStatus(), err.getDetail());

    } catch (Exception e) {
      return new MindeeHttpExceptionV2(response.getCode(), "Unknown error");
    }
  }

  private HttpPost buildHttpPost(String url) {
    HttpPost post;
    try {
      var uriBuilder = new URIBuilder(url);
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
    return post;
  }

  private <R extends CommonResponse> R deserializeOrThrow(
      String body,
      Class<R> clazz,
      int httpStatus
  ) throws MindeeHttpExceptionV2 {

    if (httpStatus >= 200 && httpStatus < 400) {
      try {
        var model = mapper.readerFor(clazz).<R>readValue(body);
        model.setRawResponse(body);
        return model;
      } catch (Exception exception) {
        throw new MindeeException(
          "Couldn't deserialize server response:\n" + exception.getMessage()
        );
      }
    }

    ErrorResponse err;
    try {
      err = mapper.readValue(body, ErrorResponse.class);
      if (err.getDetail() == null) {
        err = makeUnknownError(httpStatus);
      }
    } catch (Exception ignored) {
      err = makeUnknownError(httpStatus);
    }
    throw new MindeeHttpExceptionV2(err.getStatus(), err.getDetail());
  }
}
