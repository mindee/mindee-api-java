package com.mindee.http;

import com.mindee.InferenceParameters;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
public abstract class MindeeApiV2 extends MindeeApiCommon {
  /**
   * Send a file to the prediction queue with a local file.
   */
  public abstract JobResponse reqPostInferenceEnqueue(
      LocalInputSource inputSource,
      InferenceParameters options
  ) throws IOException;

  /**
   * Send a file to the prediction queue with a remote file.
   */
  public abstract JobResponse reqPostInferenceEnqueue(
      URLInputSource inputSource,
      InferenceParameters options
  ) throws IOException;

  /**
   * Attempts to poll the queue.
   */
  public abstract JobResponse reqGetJob(
      String jobId
  );

  /**
   * Retrieves the inference from a 302 redirect.
   */
  abstract public InferenceResponse reqGetInference(String jobId);
}
