package com.mindee.v2.parsing;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.MindeeException;
import com.mindee.parsing.BaseLocalResponse;
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

  /**
   * Deserialize this local JSON payload into a specific {@link CommonResponse}
   * subtype: {@code InferenceResponse}, {@code JobResponse}.
   *
   * @param responseClass the concrete class to instantiate
   * @param <T> generic {@link CommonResponse}
   * @return Either a {@code InferenceResponse} or {@code JobResponse} instance.
   * @throws MindeeException if the payload cannot be deserialized into the requested type
   */
  public <T extends CommonResponse> T deserializeResponse(Class<T> responseClass) {
    var mapper = new ObjectMapper().findAndRegisterModules();
    try {
      var response = mapper.readValue(this.file, responseClass);
      response.setRawResponse(new String(this.file, StandardCharsets.UTF_8));
      return response;
    } catch (Exception e) {
      throw new MindeeException("Invalid JSON payload.", e);
    }
  }
}
