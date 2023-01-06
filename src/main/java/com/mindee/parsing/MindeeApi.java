package com.mindee.parsing;

import com.mindee.ParseParameter;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.utils.MindeeException;

import java.io.IOException;

public interface MindeeApi {

  /**
   * Predict according to a specific model.
   */
  <T extends Inference> Document<T> predict(
    Class<T> clazz,
    ParseParameter parseParameter) throws MindeeException, IOException;

  /**
   * Predict according to a custom model for API Builder.
   */
  <T extends Inference> Document<T> predict(
    Class<T> clazz,
    CustomEndpoint customEndpoint,
    ParseParameter parseParameter) throws MindeeException, IOException;
}
