package com.mindee.parsing;

import com.mindee.ParseParameter;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.utils.MindeeException;
import java.io.IOException;

/**
 * Defines required methods for an API.
 */
public interface MindeeApi {

  /**
   * Predict according to a specific model.
   */
  <DocT extends Inference> Document<DocT> predict(
      Class<DocT> documentClass,
      ParseParameter parseParameter
  ) throws MindeeException, IOException;

  /**
   * Predict according to a custom model for API Builder.
   */
  <DocT extends Inference> Document<DocT> predict(
      Class<DocT> clazz,
      CustomEndpoint customEndpoint,
      ParseParameter parseParameter
  ) throws MindeeException, IOException;
}
