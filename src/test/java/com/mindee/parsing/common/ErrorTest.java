package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class ErrorTest extends TestCase {

  @Test
  void given_details_as_object_mustBeDeserialized() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    Error error = objectMapper.readValue(
      new File("src/test/resources/data/errors/with_object_response_in_detail.json"),
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
      new File("src/test/resources/data/errors/with_string_response_in_detail.json"),
      Error.class);

    Assertions.assertNotNull(error);
    Assertions.assertNotNull(error.getDetails());
    Assertions.assertEquals("error message", error.getDetails().toString());
  }
}
