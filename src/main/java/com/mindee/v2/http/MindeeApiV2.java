package com.mindee.v2.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.mindee.MindeeException;
import com.mindee.http.MindeeApiCommon;
import com.mindee.input.InputSource;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.clientoptions.BaseAnnotationParameters;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseRagDocumentUploadParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.parsing.BaseRagAnnotationResponse;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.error.ErrorResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchResponse;
import com.mindee.v2.product.ProductAttributes;
import com.mindee.v2.search.models.ModelSearchParameters;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.io.entity.EntityUtils;

/**
 * Communicate with the Mindee HTTP API V2.
 * <p>
 * You may use this base class to make your own custom class.
 * However, we may introduce breaking changes in minor versions as needed.
 * </p>
 */
public abstract class MindeeApiV2 extends MindeeApiCommon {

  protected static final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();
  protected final System.Logger logger = System.getLogger(getClass().getName());

  /**
   * Send a file to the asynchronous processing queue for a product.
   *
   * @param inputSource Local input source or URL input source.
   * @param parameters parameters.
   */
  public abstract JobResponse reqPostProductEnqueue(
      InputSource inputSource,
      BaseProductParameters parameters
  ) throws IOException;

  /**
   * Get the status of an inference that was previously enqueued.
   *
   * @param pollingUrl The job ID as returned by the enqueue call.
   */
  public abstract JobResponse reqGetJobByUrl(String pollingUrl);

  /**
   * Get the status of an inference that was previously enqueued.
   *
   * @param jobId The job ID as returned by the enqueue call.
   */
  public abstract JobResponse reqGetJobById(String jobId);

  /**
   * Get the result of an inference that was previously enqueued.
   *
   * @param responseClass The class of the response.
   * @param inferenceId Url to poll.
   */
  public abstract <TResponse extends BaseResponse> TResponse reqGetResultById(
      Class<TResponse> responseClass,
      String inferenceId
  );

  /**
   * Get the result of an inference that was previously enqueued.
   *
   * @param responseClass The class of the response.
   * @param inferenceUrl URL to poll.
   */
  public abstract <TResponse extends BaseResponse> TResponse reqGetResultByUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  );

  /**
   * Retrieves a list of resources with the given criteria.
   *
   * @param parameters Search parameters.
   */
  public abstract <TSearchResponse extends BaseSearchResponse> TSearchResponse reqGetSearch(
      BaseSearchParameters<TSearchResponse> parameters
  );

  /**
   * Add a document to the RAG database.
   *
   * @param parameters RAG document upload parameters.
   * @param localInputSource Local input source.
   */
  public abstract <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqPostRagDocument(
      BaseRagDocumentUploadParameters<TAnnotationResponse> parameters,
      LocalInputSource localInputSource
  ) throws IOException;

  /**
   * Get a document's info and annotations from the RAG database.
   *
   * @param responseClass The class of the response.
   * @param documentId The ID of the document.
   */
  public abstract <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqGetRagAnnotation(
      Class<TAnnotationResponse> responseClass,
      String documentId
  );

  /**
   * Update a document's annotations in the RAG database.
   *
   * @param parameters Annotation parameters.
   */
  public abstract <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse reqPatchRagAnnotation(
      BaseAnnotationParameters<TAnnotationResponse> parameters
  );

  /**
   * Deletes a document from the RAG database.
   * For extraction models only.
   *
   * @param documentId The ID of the document to delete.
   */
  public abstract boolean reqDeleteExtractionRagDocument(String documentId);

  /**
   * Retrieves a list of models available for a given API key.
   *
   * @param parameters Model search parameters.
   */
  @Deprecated
  public abstract SearchResponse reqGetSearch(ModelSearchParameters parameters);

  /**
   * Get the error from the server response.
   */
  protected MindeeHttpExceptionV2 getErrorFromResponse(ClassicHttpResponse response) {
    logger.log(System.Logger.Level.INFO, "Parsing error response ...");

    String rawBody;
    try {
      rawBody = response.getEntity() == null
          ? ""
          : EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);

      logger.log(System.Logger.Level.DEBUG, "HTTP response: {0}", rawBody);

      var errorResponse = mapper.readValue(rawBody, ErrorResponse.class);

      if (errorResponse.getDetail() == null) {
        errorResponse = makeUnknownError(response.getCode());
      }
      return new MindeeHttpExceptionV2(errorResponse);

    } catch (Exception exception) {
      return new MindeeHttpExceptionV2(makeUnknownError(response.getCode()), exception);
    }
  }

  protected <R extends BaseResponse> R deserializeResponse(
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

    ErrorResponse errorResponse;
    try {
      errorResponse = mapper.readValue(body, ErrorResponse.class);
      if (errorResponse.getDetail() == null) {
        errorResponse = makeUnknownError(httpStatus);
      }
    } catch (Exception ignored) {
      errorResponse = makeUnknownError(httpStatus);
    }
    throw new MindeeHttpExceptionV2(errorResponse);
  }

  /**
   * Creates an "unknown error" response from an HTTP status code.
   */
  protected ErrorResponse makeUnknownError(int statusCode) {
    return new ErrorResponse(
      "Unknown Error",
      "The server returned an Unknown error.",
      statusCode,
      statusCode + "-000",
      null
    );
  }

  protected ProductAttributes getResponseProductAttributes(
      Class<? extends BaseResponse> responseClass
  ) {
    var productInfo = responseClass.getAnnotation(ProductAttributes.class);
    if (productInfo == null) {
      throw new MindeeException(
        "The class " + responseClass.getSimpleName() + " is not annotated with @ProductAttributes"
      );
    }
    return productInfo;
  }

  protected ProductAttributes getParamsProductAttributes(
      Class<? extends BaseProductParameters> paramsClass
  ) {
    var productInfo = paramsClass.getAnnotation(ProductAttributes.class);
    if (productInfo == null) {
      throw new MindeeException(
        "The class " + paramsClass.getSimpleName() + " is not annotated with @ProductAttributes"
      );
    }
    return productInfo;
  }
}
