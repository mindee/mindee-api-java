package com.mindee.v2.http;

import com.mindee.MindeeException;
import com.mindee.input.InputSource;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.MindeeSettings;
import com.mindee.v2.clientoptions.BaseAnnotationParameters;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseRagDocumentUploadParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.parsing.BaseRagAnnotationResponse;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchResponse;
import com.mindee.v2.search.models.ModelSearchParameters;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import lombok.Builder;
import org.apache.hc.client5.http.classic.methods.HttpDelete;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.classic.methods.HttpPatch;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.net.URIBuilder;

/**
 * HTTP Client class for the V2 API.
 */
public final class MindeeHttpApiV2 extends MindeeApiV2 {

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

  @Override
  public JobResponse reqPostProductEnqueue(
      InputSource inputSource,
      BaseProductParameters parameters
  ) {
    var productInfo = getParamsProductAttributes(parameters.getClass());
    var url = String
      .format("%s/products/%s/enqueue", this.mindeeSettings.getBaseUrl(), productInfo.slug());
    var post = buildHttpPost(url);

    var builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);

    addPredictRequestParameters(inputSource, parameters, builder);

    post.setEntity(builder.build());

    logger.log(System.Logger.Level.DEBUG, "HTTP POST to {0} ...", url);
    return executeAPIRequest(post, JobResponse.class);
  }

  @Override
  public JobResponse reqGetJobByUrl(String pollingUrl) {
    var get = new HttpGet(pollingUrl);

    var noRedirect = RequestConfig.custom().setRedirectsEnabled(false).build();
    get.setConfig(noRedirect);

    logger.log(System.Logger.Level.DEBUG, "HTTP GET to {0}...", pollingUrl);
    return this.executeAPIRequest(get, JobResponse.class);
  }

  @Override
  public JobResponse reqGetJobById(String jobId) {
    var url = this.mindeeSettings.getBaseUrl() + "/jobs/" + jobId;
    return reqGetJobByUrl(url);
  }

  @Override
  public <TResponse extends BaseResponse> TResponse reqGetResultById(
      Class<TResponse> responseClass,
      String inferenceId
  ) {
    if (inferenceId == null || inferenceId.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceId cannot be null or empty.");
    }
    var productInfo = getResponseProductAttributes(responseClass);
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
  public <TResponse extends BaseResponse> TResponse reqGetResultByUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  ) {
    if (inferenceUrl == null || inferenceUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceUrl cannot be null or empty.");
    }
    var get = new HttpGet(inferenceUrl);

    logger.log(System.Logger.Level.DEBUG, "HTTP GET to {0}...", inferenceUrl);
    return executeAPIRequest(get, responseClass);
  }

  @Override
  public <TSearchResponse extends BaseSearchResponse> TSearchResponse reqGetSearch(
      BaseSearchParameters<TSearchResponse> parameters
  ) {
    var productInfo = getResponseProductAttributes(parameters.getResponseClass());
    URIBuilder url;
    try {
      url = new URIBuilder(this.mindeeSettings.getBaseUrl() + "/search/" + productInfo.slug());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    parameters.getRequestParameters().forEach(url::addParameter);
    var get = new HttpGet(url.toString());

    logger.log(System.Logger.Level.INFO, "Searching {0} ...", productInfo.slug());
    return this.executeAPIRequest(get, parameters.getResponseClass());
  }

  @Override
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqPostRagDocument(
      BaseRagDocumentUploadParameters<TAnnotationResponse> parameters,
      LocalInputSource localInputSource
  ) {
    var productInfo = getResponseProductAttributes(parameters.getResponseClass());
    var url = String
      .format("%s/products/%s/rag-documents", this.mindeeSettings.getBaseUrl(), productInfo.slug());
    var post = buildHttpPost(url);

    var builder = MultipartEntityBuilder.create();
    builder.setMode(HttpMultipartMode.EXTENDED);
    builder
      .addBinaryBody(
        "file",
        localInputSource.getFile(),
        ContentType.DEFAULT_BINARY,
        localInputSource.getFilename()
      );

    parameters.getRequestParameters().forEach(builder::addTextBody);
    post.setEntity(builder.build());

    logger.log(System.Logger.Level.DEBUG, "HTTP POST to {0} ...", url);
    return executeAPIRequest(post, parameters.getResponseClass());
  }

  @Override
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqGetRagAnnotation(
      Class<TAnnotationResponse> responseClass,
      String documentId
  ) {
    var productInfo = getResponseProductAttributes(responseClass);
    var url = String
      .format(
        "%s/products/%s/rag-documents/%s",
        this.mindeeSettings.getBaseUrl(),
        productInfo.slug(),
        documentId
      );
    var get = new HttpGet(url);

    logger.log(System.Logger.Level.DEBUG, "HTTP GET to {0} ...", url);
    return executeAPIRequest(get, responseClass);
  }

  @Override
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqPatchRagAnnotation(
      BaseAnnotationParameters<TAnnotationResponse> parameters
  ) {
    var productInfo = getResponseProductAttributes(parameters.getResponseClass());
    var url = String
      .format(
        "%s/products/%s/rag-documents/%s",
        this.mindeeSettings.getBaseUrl(),
        productInfo.slug(),
        parameters.getDocumentId()
      );
    var patch = new HttpPatch(url);

    try {
      var json = mapper.writeValueAsString(parameters.getRequestParameters());
      patch.setEntity(new StringEntity(json, ContentType.APPLICATION_JSON));
    } catch (com.fasterxml.jackson.core.JsonProcessingException e) {
      throw new com.mindee.MindeeException("Failed to serialize patch parameters", e);
    }

    logger.log(System.Logger.Level.DEBUG, "HTTP PATCH to {0} ...", url);
    return executeAPIRequest(patch, parameters.getResponseClass());
  }

  @Override
  public boolean reqDeleteExtractionRagDocument(String documentId) {
    var url = this.mindeeSettings.getBaseUrl() + "/products/extraction/rag-documents/" + documentId;
    var delete = new HttpDelete(url);

    logger.log(System.Logger.Level.DEBUG, "HTTP DELETE to {0} ...", url);

    if (this.mindeeSettings.getApiKey().isPresent()) {
      delete.setHeader(HttpHeaders.AUTHORIZATION, this.mindeeSettings.getApiKey().get());
    }
    delete.setHeader(HttpHeaders.USER_AGENT, getUserAgent());

    try (var httpClient = httpClientBuilder.build()) {
      return httpClient.execute(delete, response -> {
        int statusCode = response.getCode();
        EntityUtils.consumeQuietly(response.getEntity());
        return statusCode >= 200 && statusCode < 300;
      });
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
    }
  }

  @Override
  @Deprecated
  public SearchResponse reqGetSearch(ModelSearchParameters parameters) {
    URIBuilder url;
    try {
      url = new URIBuilder(this.mindeeSettings.getBaseUrl() + "/search/models");
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
    parameters.getRequestParameters().forEach(url::addParameter);
    var get = new HttpGet(url.toString());

    logger.log(System.Logger.Level.INFO, "Model search...");
    return this.executeAPIRequest(get, SearchResponse.class);
  }

  private void addPredictRequestParameters(
      InputSource inputSource,
      BaseProductParameters parameters,
      MultipartEntityBuilder builder
  ) {
    if (inputSource == null) {
      throw new IllegalArgumentException("Input source cannot be null");
    }

    if (inputSource instanceof LocalInputSource) {
      LocalInputSource localInputSource = (LocalInputSource) inputSource;
      builder
        .addBinaryBody(
          "file",
          localInputSource.getFile(),
          ContentType.DEFAULT_BINARY,
          localInputSource.getFilename()
        );
    } else if (inputSource instanceof URLInputSource) {
      URLInputSource urlInputSource = (URLInputSource) inputSource;
      builder.addTextBody("url", urlInputSource.getUrl().toString());
    } else {
      throw new IllegalArgumentException(
        "Unsupported input source type '" + inputSource.getClass() + "'"
      );
    }

    // Append all standard request parameters
    parameters.getRequestParameters().forEach(builder::addTextBody);
  }

  /**
   * Executes an enqueue action, common to URL & local inputs.
   *
   * @param apiRequest HTTP request object.
   * @return a valid job response.
   */
  private <TResponse extends BaseResponse> TResponse executeAPIRequest(
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
          throw getErrorFromResponse(response);
        }
        try {
          var raw = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
          logger.log(System.Logger.Level.DEBUG, "HTTP response: {0}", raw);
          return deserializeResponse(raw, responseClass, response.getCode());
        } finally {
          EntityUtils.consumeQuietly(responseEntity);
        }
      });
    } catch (IOException err) {
      throw new MindeeException(err.getMessage(), err);
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
}
