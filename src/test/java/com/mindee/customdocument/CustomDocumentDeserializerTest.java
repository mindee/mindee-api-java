package com.mindee.customdocument;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.model.customdocument.CustomDocumentResponse;
import java.io.FileInputStream;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomDocumentDeserializerTest {

  @Test
  public void deserializeWithFullResponse() throws IOException {
    CustomDocumentResponse response = new ObjectMapper()
        .readValue(new FileInputStream("src/test/resources/data/custom/response_v1/complete.json"),
            CustomDocumentResponse.class);

    Assertions.assertEquals(11, response.getFields().size());
    Assertions.assertNotNull(response.getFields().get("date_normal"));
    Assertions.assertEquals("2020-12-17", response.getField(
        "date_normal")
        .getValues().get(0).getContent());
  }
}
