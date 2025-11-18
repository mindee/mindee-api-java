package com.mindee.v1.parsing.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

import static com.mindee.TestingUtilities.getV1ResourcePath;

public class ErrorTest {

  @Test
  void given_details_as_object_mustBeDeserialized() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    Error error = objectMapper.readValue(
        getV1ResourcePath("errors/with_object_response_in_detail.json").toFile(),
      Error.class);

    Assertions.assertNotNull(error);
    Assertions.assertNotNull(error.getDetails());
    Assertions.assertEquals(
      "{\"document\":[\"error message\"]}",
      error.getDetails().toString());
  }

  @Test
  void given_details_as_string_mustBeDeserialized() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    Error error = objectMapper.readValue(
        getV1ResourcePath("errors/with_string_response_in_detail.json").toFile(),
      Error.class);

    Assertions.assertNotNull(error);
    Assertions.assertNotNull(error.getDetails());
    Assertions.assertEquals("error message", error.getDetails().toString());
  }
}
