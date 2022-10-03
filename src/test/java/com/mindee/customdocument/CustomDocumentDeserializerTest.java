package com.mindee.customdocument;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.model.customdocument.CustomDocumentResponse;

class CustomDocumentDeserializerTest {

  @Test
  public void deserializeWithFullResponse() throws IOException {
    CustomDocumentResponse response = new ObjectMapper()
        .readValue(new FileInputStream("src/test/resources/cnmss_2benefs_fullresponse.json"),
            CustomDocumentResponse.class);

    Assertions.assertEquals(10, response.getFields().size());
    Assertions.assertNotNull(response.getFields().get("assure_validite_debut"));
    Assertions.assertEquals("2021-06-01", response.getField(
        "assure_validite_debut")
        .getValues().get(0).getContent());
  }
}
