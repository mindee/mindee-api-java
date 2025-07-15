package com.mindee.http;

import com.mindee.InferenceOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
abstract public class MindeeApiV2 extends MindeeApiCommon {
  /**
   * Send a file to the prediction queue.
   */
  abstract public JobResponse enqueuePost(
      LocalInputSource inputSource,
      InferenceOptions options
  ) throws IOException;

  /**
   * Get a document from the predict queue.
   */
  abstract public CommonResponse getInferenceFromQueue(
      String jobId
  );

}
