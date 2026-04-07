package com.mindee.v1;

import com.mindee.v1.http.Endpoint;
import com.mindee.v1.http.MindeeApiV1;
import com.mindee.v1.http.RequestParameters;
import com.mindee.v1.parsing.common.AsyncPredictResponse;
import com.mindee.v1.parsing.common.Inference;
import com.mindee.v1.parsing.common.PredictResponse;
import com.mindee.v1.parsing.common.WorkflowResponse;
import java.io.IOException;

public class FakeMindeeApiV1<T extends Inference> extends MindeeApiV1 {

  private final AsyncPredictResponse<T> asyncPredictResponse;
  private final PredictResponse<T> predictResponse;
  private final WorkflowResponse<T> workflowResponse;

  public FakeMindeeApiV1(AsyncPredictResponse<T> asyncPredictResponse) {
    this.asyncPredictResponse = asyncPredictResponse;
    this.predictResponse = null;
    this.workflowResponse = null;
  }

  public FakeMindeeApiV1(PredictResponse<T> predictResponse) {
    this.asyncPredictResponse = null;
    this.predictResponse = predictResponse;
    this.workflowResponse = null;
  }

  public FakeMindeeApiV1(WorkflowResponse<T> workflowResponse) {
    this.asyncPredictResponse = null;
    this.predictResponse = null;
    this.workflowResponse = workflowResponse;
  }

  @Override
  public <DocT extends Inference> AsyncPredictResponse<DocT> documentQueueGet(
      Class<DocT> documentClass,
      Endpoint endpoint,
      String jobId
  ) {
    return (AsyncPredictResponse<DocT>) asyncPredictResponse;
  }

  @Override
  public <DocT extends Inference> AsyncPredictResponse<DocT> predictAsyncPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException {
    return (AsyncPredictResponse<DocT>) asyncPredictResponse;
  }

  @Override
  public <DocT extends Inference> PredictResponse<DocT> predictPost(
      Class<DocT> documentClass,
      Endpoint endpoint,
      RequestParameters requestParameters
  ) throws IOException {
    return (PredictResponse<DocT>) predictResponse;
  }

  @Override
  public <DocT extends Inference> WorkflowResponse<DocT> executeWorkflowPost(
      Class<DocT> documentClass,
      String workflowId,
      RequestParameters requestParameters
  ) throws IOException {
    return (WorkflowResponse<DocT>) workflowResponse;
  }
}
