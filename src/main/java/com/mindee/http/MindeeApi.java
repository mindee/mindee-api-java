package com.mindee.http;

import com.mindee.parsing.CustomEndpoint;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
public interface MindeeApi {

  <DocT extends Inference> PredictResponse<DocT> checkJobStatus(
      Class<DocT> documentClass, String jobId);

  /**
   * Predict according to a specific model.
   */
  <DocT extends Inference> PredictResponse<DocT> predict(
      Class<DocT> documentClass,
      RequestParameters requestParameters
  ) throws IOException;

  /**
   * Predict according to a custom model for API Builder.
   */
  <DocT extends Inference> PredictResponse<DocT> predict(
      Class<DocT> clazz,
      CustomEndpoint customEndpoint,
      RequestParameters requestParameters
  ) throws IOException;
}
