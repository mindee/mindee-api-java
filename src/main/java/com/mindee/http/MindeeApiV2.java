package com.mindee.http;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.JobResponse;
import com.mindee.v2.clientOptions.BaseParameters;
import com.mindee.v2.http.ProductInfo;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
public abstract class MindeeApiV2 extends MindeeApiCommon {
  /**
   * Send a file to the prediction queue with a local file.
   *
   * @param inputSource Local input source from URL.
   * @param options parameters.
   */
  public abstract JobResponse reqPostEnqueue(
      LocalInputSource inputSource,
      BaseParameters options
  ) throws IOException;

  /**
   * Send a file to the prediction queue with a remote file.
   *
   * @param inputSource Remote input source from URL.
   * @param options parameters.
   */
  public abstract JobResponse reqPostEnqueue(
      URLInputSource inputSource,
      BaseParameters options
  ) throws IOException;

  /**
   * Attempts to poll the queue.
   *
   * @param jobId id of the job to get.
   */
  public abstract JobResponse reqGetJob(String jobId);

  /**
   * Retrieves the inference from a 302 redirect.
   *
   * @param inferenceId ID of the inference to poll.
   */
  abstract public <TResponse extends CommonResponse> TResponse reqGetResult(
      Class<TResponse> responseClass,
      String inferenceId
  );

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

  protected ProductInfo getResponseProductInfo(Class<? extends CommonResponse> responseClass) {
    ProductInfo productInfo = responseClass.getAnnotation(ProductInfo.class);
    if (productInfo == null) {
      throw new MindeeException(
        "The class " + responseClass.getSimpleName() + " is not annotated with @ProductInfo"
      );
    }
    return productInfo;
  }

  protected ProductInfo getParamsProductInfo(Class<? extends BaseParameters> responseClass) {
    ProductInfo productInfo = responseClass.getAnnotation(ProductInfo.class);
    if (productInfo == null) {
      throw new MindeeException(
        "The class " + responseClass.getSimpleName() + " is not annotated with @ProductInfo"
      );
    }
    return productInfo;
  }
}
