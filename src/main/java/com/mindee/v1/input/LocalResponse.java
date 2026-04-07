package com.mindee.v1.input;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeException;
import com.mindee.v1.parsing.common.AsyncPredictResponse;
import com.mindee.v1.parsing.common.Inference;
import com.mindee.v1.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

/**
 * A Mindee response saved locally.
 */
public class LocalResponse extends com.mindee.input.LocalResponse {

  public LocalResponse(InputStream input) {
    super(input);
  }

  public LocalResponse(String input) {
    super(input);
  }

  public LocalResponse(File input) throws IOException {
    super(input);
  }

  public LocalResponse(Path input) throws IOException {
    super(input);
  }

  /**
   * Deserialize this local JSON payload into a specific {@link AsyncPredictResponse}.
   * subtype: {@code InferenceResponse}, {@code JobResponse}.
   *
   * @param responseClass the concrete class to instantiate
   * @param <T> generic {@link Inference}
   * @return A {@link AsyncPredictResponse} instance.
   * @throws MindeeException if the payload cannot be deserialized into the requested type
   */
  public <T extends Inference> AsyncPredictResponse<T> deserializeAsyncResponse(
      Class<T> responseClass
  ) {
    var objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    var type = objectMapper
      .getTypeFactory()
      .constructParametricType(AsyncPredictResponse.class, responseClass);
    try {
      AsyncPredictResponse<T> response = objectMapper.readValue(this.file, type);
      response.setRawResponse(new String(this.file, StandardCharsets.UTF_8));
      return response;
    } catch (Exception ex) {
      throw new MindeeException("Invalid class specified for deserialization.", ex);
    }
  }

  /**
   * Deserialize this local JSON payload into a specific {@link PredictResponse}.
   *
   * @param responseClass the concrete class to instantiate
   * @param <T> generic {@link Inference}
   * @return A {@link PredictResponse} instance.
   * @throws MindeeException if the payload cannot be deserialized into the requested type
   */
  public <T extends Inference> PredictResponse<T> deserializeSyncResponse(Class<T> responseClass) {
    var objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    var type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, responseClass);
    try {
      PredictResponse<T> response = objectMapper.readValue(this.file, type);
      response.setRawResponse(new String(this.file, StandardCharsets.UTF_8));
      return response;
    } catch (Exception ex) {
      throw new MindeeException("Invalid class specified for deserialization.", ex);
    }
  }
}
