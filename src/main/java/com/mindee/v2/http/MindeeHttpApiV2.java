package com.mindee.v2.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.MindeeSettings;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.error.ErrorResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
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
   * @param parameters Options to send the file along with.
   * @return A job response.
   */
  @Override
  public JobResponse reqPostEnqueue(
      LocalInputSource inputSource,
      BaseProductParameters parameters
  ) {
    var productInfo = getParamsProductAttributes(parameters.getClass());
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
    parameters.getRequestParameters().forEach(builder::addTextBody);
    post.setEntity(builder.build());
    return executeAPIRequest(post, JobResponse.class);
  }

  /**
   * Enqueues a doc with the POST method.
   *
   * @param inputSource Input source to send.
   * @param options Options to send the file along with.
   * @return A job response.
   */
  @Override
  public JobResponse reqPostEnqueue(URLInputSource inputSource, BaseProductParameters options) {
    var productInfo = getParamsProductAttributes(options.getClass());
    var url = String
      .format("%s/products/%s/enqueue", this.mindeeSettings.getBaseUrl(), productInfo.slug());
    var post = buildHttpPost(url);

    var builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder.addTextBody("url", inputSource.getUrl().toString());
    options.getRequestParameters().forEach(builder::addTextBody);
    post.setEntity(builder.build());
    return executeAPIRequest(post, JobResponse.class);
  }

  @Override
  public JobResponse reqGetJobById(String jobId) {

    var url = this.mindeeSettings.getBaseUrl() + "/jobs/" + jobId;
    var get = new HttpGet(url);

    var noRedirect = RequestConfig.custom().setRedirectsEnabled(false).build();
    get.setConfig(noRedirect);

    return this.executeAPIRequest(get, JobResponse.class);
  }

  @Override
  public <TResponse extends CommonResponse> TResponse reqGetResultById(
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
    return reqGetResultByUrl(responseClass, url);
  }

  @Override
  public <TResponse extends CommonResponse> TResponse reqGetResultByUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  ) {
    if (inferenceUrl == null || inferenceUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceUrl must not be null or blank.");
    }
    validateInferenceUrl(inferenceUrl);
    var get = new HttpGet(inferenceUrl);
    return executeAPIRequest(get, responseClass);
  }

  @Override
  public <TSearchResponse extends BaseSearchResponse> TSearchResponse reqGetSearch(
      Class<TSearchResponse> responseClass,
      BaseSearchParameters parameters
  ) {
    var productInfo = getResponseProductInfo(responseClass);
    URIBuilder url;
    try {
      url = new URIBuilder(this.mindeeSettings.getBaseUrl() + "/search/" + productInfo.slug());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    parameters.getRequestParameters().forEach(url::addParameter);
    var get = new HttpGet(url.toString());
    return this.executeAPIRequest(get, responseClass);
  }

  /**
   * Ensures that a caller-supplied inference URL targets the configured Mindee
   * base URL so the {@code Authorization} header attached by
   * {@link #executeAPIRequest} cannot leak to third-party hosts.
   *
   * <p>
   * The URL must be an absolute HTTPS URL whose host + port match the
   * configured base URL, whose path is under the base URL's path, and which
   * carries no embedded userinfo.
   */
  void validateInferenceUrl(String inferenceUrl) {
    java.net.URI target;
    java.net.URI base;
    try {
      target = new java.net.URI(inferenceUrl);
      base = new java.net.URI(this.mindeeSettings.getBaseUrl());
    } catch (java.net.URISyntaxException e) {
      throw new MindeeException("inferenceUrl is not a valid URI: " + inferenceUrl, e);
    }
    if (!target.isAbsolute()) {
      throw new MindeeException("inferenceUrl must be an absolute URL: " + inferenceUrl);
    }
    if (!"https".equalsIgnoreCase(target.getScheme())) {
      throw new MindeeException("inferenceUrl must use https: " + inferenceUrl);
    }
    if (target.getUserInfo() != null && !target.getUserInfo().isEmpty()) {
      throw new MindeeException("inferenceUrl must not contain userinfo: " + inferenceUrl);
    }
    String targetHost = target.getHost();
    String baseHost = base.getHost();
    if (targetHost == null || baseHost == null || !targetHost.equalsIgnoreCase(baseHost)) {
      throw new MindeeException(
        "inferenceUrl host '"
          + targetHost
          + "' does not match Mindee base URL host '"
          + baseHost
          + "'"
      );
    }
    int targetPort = target.getPort() == -1 ? defaultPort(target.getScheme()) : target.getPort();
    int basePort = base.getPort() == -1 ? defaultPort(base.getScheme()) : base.getPort();
    if (targetPort != basePort) {
      throw new MindeeException(
        "inferenceUrl port " + targetPort + " does not match Mindee base URL port " + basePort
      );
    }
    String basePath = base.getPath() == null ? "" : base.getPath();
    String targetPath = target.getPath() == null ? "" : target.getPath();
    if (!basePath.isEmpty() && !basePath.equals("/")) {
      String normalizedBase = basePath.endsWith("/") ? basePath : basePath + "/";
      if (!targetPath.equals(basePath) && !targetPath.startsWith(normalizedBase)) {
        throw new MindeeException(
          "inferenceUrl path '"
            + targetPath
            + "' is not under Mindee base URL path '"
            + basePath
            + "'"
        );
      }
    }
  }

  private static int defaultPort(String scheme) {
    if ("https".equalsIgnoreCase(scheme)) {
      return 443;
    }
    if ("http".equalsIgnoreCase(scheme)) {
      return 80;
    }
    return -1;
  }

  /**
   * Executes an enqueue action, common to URL & local inputs.
   *
   * @param apiRequest HTTP request object.
   * @return a valid job response.
   */
  private <TResponse extends CommonResponse> TResponse executeAPIRequest(
      HttpUriRequestBase apiRequest,
      Class<TResponse> responseClass
  ) {
    if (this.mindeeSettings.getApiKey().isPresent()) {
      apiRequest.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    apiRequest.setHeader(HttpHeaders.USER_AGENT, getUserAgent());

    try (var httpClient = httpClientBuilder.build()) {
      return httpClient.execute(apiRequest, response -> {
        var responseEntity = response.getEntity();
        var statusCode = response.getCode();
        if (isInvalidStatusCode(statusCode)) {
          throw getHttpError(response);
        }
        try {
          var raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          return deserializeOrThrow(raw, responseClass, response.getCode());
        } finally {
          EntityUtils.consumeQuietly(responseEntity);
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
