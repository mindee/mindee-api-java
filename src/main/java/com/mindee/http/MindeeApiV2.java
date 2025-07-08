package com.mindee.http;

import com.mindee.InferencePredictOptions;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.v2.JobResponse;
import com.mindee.parsing.v2.CommonResponse;
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
      InferencePredictOptions options
  ) throws IOException;

  /**
   * Get a document from the predict queue.
   */
  abstract public CommonResponse getInferenceFromQueue(
      String jobId
  );

}
