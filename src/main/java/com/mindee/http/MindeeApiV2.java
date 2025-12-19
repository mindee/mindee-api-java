package com.mindee.http;

import com.mindee.InferenceParameters;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
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
  public abstract JobResponse reqPostInferenceEnqueue(
      LocalInputSource inputSource,
      InferenceParameters options
  ) throws IOException;

  /**
   * Send a file to the prediction queue with a remote file.
   * 
   * @param inputSource Remote input source from URL.
   * @param options parameters.
   */
  public abstract JobResponse reqPostInferenceEnqueue(
      URLInputSource inputSource,
      InferenceParameters options
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
  abstract public InferenceResponse reqGetInference(String inferenceId);

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
}
