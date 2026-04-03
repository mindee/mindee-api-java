package com.mindee.v1.http;

import com.mindee.http.MindeeApiCommon;
import com.mindee.v1.parsing.common.AsyncPredictResponse;
import com.mindee.v1.parsing.common.Inference;
import com.mindee.v1.parsing.common.PredictResponse;
import com.mindee.v1.parsing.common.WorkflowResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
public abstract class MindeeApiV1 extends MindeeApiCommon {

  /**
   * Get a document from the predict queue.
   */
  public abstract <DocT extends Inference> AsyncPredictResponse<DocT> documentQueueGet(
      Class<DocT> documentClass,
      Endpoint endpoint,
      String jobId
  );

  /**
   * Send a file to the prediction queue.
   */
  public abstract <DocT extends Inference> AsyncPredictResponse<DocT> predictAsyncPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

  /**
   * Send a file to the prediction API.
   */
  public abstract <DocT extends Inference> PredictResponse<DocT> predictPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException;

  public abstract <DocT extends Inference> WorkflowResponse<DocT> executeWorkflowPost(
      Class<DocT> documentClass,
      String workflowId,
      RequestParameters requestParameters
  ) throws IOException;
}
