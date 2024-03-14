package com.mindee.product.generated;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.AsyncPredictResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GeneratedV1Test {
  protected AsyncPredictResponse<GeneratedV1> getAsyncPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      AsyncPredictResponse.class,
      GeneratedV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/generated/response_v1/" + name + ".json"),
      type
    );
  }

}
