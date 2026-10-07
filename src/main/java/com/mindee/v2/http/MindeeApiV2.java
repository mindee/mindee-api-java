package com.mindee.v2.http;

import com.mindee.MindeeException;
import com.mindee.http.MindeeApiCommon;
import com.mindee.input.InputSource;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.error.ErrorResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchResponse;
import com.mindee.v2.product.ProductAttributes;
import com.mindee.v2.search.models.ModelSearchParameters;
import java.io.IOException;

/**
 * Communicate with the Mindee HTTP API V2.
 * <p>
 * You may use this base class to make your own custom class.
 * However, we may introduce breaking changes in minor versions as needed.
 * </p>
 */
public abstract class MindeeApiV2 extends MindeeApiCommon {
  /**
   * Send a file to the asynchronous processing queue for a product.
   *
   * @param inputSource Local input source from URL.
   * @param parameters parameters.
   */
  public abstract JobResponse reqPostProductEnqueue(
      InputSource inputSource,
      BaseProductParameters parameters
  ) throws IOException;

  /**
   * Get the status of an inference that was previously enqueued.
   *
   * @param pollingUrl The job URL as returned by the predict_async route.
   */
  public abstract JobResponse reqGetJobByUrl(String pollingUrl);

  /**
   * Attempts to poll the queue.
   *
   * @param jobId id of the job to get.
   */
  public abstract JobResponse reqGetJobById(String jobId);

  /**
   * Retrieves the inference from a 302 redirect.
   *
   * @param inferenceId ID of the inference to poll.
   */
  public abstract <TResponse extends BaseResponse> TResponse reqGetResultById(
      Class<TResponse> responseClass,
      String inferenceId
  );

  /**
   * Retrieves the inference from a given URL.
   * The inference will only be available after it has finished processing.
   */
  public abstract <TResponse extends BaseResponse> TResponse reqGetResultByUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  );

  /**
   * Retrieves a list of resources with the given criteria.
   */
  public abstract <TSearchResponse extends BaseSearchResponse> TSearchResponse reqGetSearch(
      BaseSearchParameters<TSearchResponse> parameters
  );

  @Deprecated
  public abstract SearchResponse reqGetSearch(ModelSearchParameters parameters);

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
