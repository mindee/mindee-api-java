package com.mindee.http;

import com.mindee.PredictOptionsV2;
import com.mindee.parsing.v2.AsyncInferenceResponse;
import com.mindee.parsing.v2.AsyncJobResponse;

import java.io.IOException;

/**
 * Defines required methods for an API.
 */
abstract public class MindeeApiV2 extends MindeeApiCommon {
  /**
   * Send a file to the prediction queue.
   */
  abstract public AsyncJobResponse enqueuePost(
      PredictOptionsV2 options,
      RequestParameters requestParameters
  ) throws IOException;

  /**
   * Get a document from the predict queue.
   */
  abstract public AsyncInferenceResponse getInferenceFromQueue(
      String jobId
  );

}
