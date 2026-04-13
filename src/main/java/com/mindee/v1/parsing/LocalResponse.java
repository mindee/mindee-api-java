package com.mindee.v1.parsing;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeException;
import com.mindee.parsing.BaseLocalResponse;
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
public class LocalResponse extends BaseLocalResponse {

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

  private <P extends Inference, R> R deserialize(
      Class<R> responseClass,
      Class<P> productClass
  ) throws IOException {
    var mapper = new ObjectMapper().findAndRegisterModules();
    var type = mapper.getTypeFactory().constructParametricType(responseClass, productClass);
    try {
      return mapper.readValue(this.file, type);
    } catch (Exception e) {
      if (e instanceof JacksonException) {
        throw new MindeeException("Invalid JSON payload.", e);
      }
      throw e;
    }
  }

  /**
   * Deserialize this local JSON payload into a specific {@link AsyncPredictResponse}.
   * subtype: {@code InferenceResponse}, {@code JobResponse}.
   *
   * @param productClass the concrete class to instantiate
   * @param <T> generic {@link Inference}
   * @return A {@link AsyncPredictResponse} instance.
   * @throws MindeeException if the payload cannot be deserialized into the requested type
   */
  public <T extends Inference> AsyncPredictResponse<T> deserializeAsyncResponse(
      Class<T> productClass
  ) throws IOException {
    @SuppressWarnings("unchecked")
    AsyncPredictResponse<T> response = deserialize(AsyncPredictResponse.class, productClass);
    response.setRawResponse(new String(this.file, StandardCharsets.UTF_8));
    return response;
  }

  /**
   * Deserialize this local JSON payload into a specific {@link PredictResponse}.
   *
   * @param productClass the concrete class to instantiate
   * @param <T> generic {@link Inference}
   * @return A {@link PredictResponse} instance.
   * @throws MindeeException if the payload cannot be deserialized into the requested type
   */
  public <T extends Inference> PredictResponse<T> deserializeSyncResponse(
      Class<T> productClass
  ) throws IOException {
    @SuppressWarnings("unchecked")
    PredictResponse<T> response = deserialize(PredictResponse.class, productClass);
    response.setRawResponse(new String(this.file, StandardCharsets.UTF_8));
    return response;
  }
}
