package com.mindee.http;

import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.common.WorkflowResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
abstract public class MindeeApi extends MindeeApiCommon {

  /**
   * Get a document from the predict queue.
   */
  abstract public <DocT extends Inference> AsyncPredictResponse<DocT> documentQueueGet(
      Class<DocT> documentClass,
      Endpoint endpoint,
      String jobId
  );

  /**
   * Send a file to the prediction queue.
   */
  abstract public <DocT extends Inference> AsyncPredictResponse<DocT> predictAsyncPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

  /**
   * Send a file to the prediction API.
   */
  abstract public <DocT extends Inference> PredictResponse<DocT> predictPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

  abstract public <DocT extends Inference> WorkflowResponse<DocT> executeWorkflowPost(
      Class<DocT> documentClass,
      String workflowId,
      RequestParameters requestParameters
  ) throws IOException;
}
