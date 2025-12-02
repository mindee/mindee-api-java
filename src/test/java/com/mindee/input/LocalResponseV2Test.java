package com.mindee.input;

import com.mindee.parsing.v2.InferenceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.mindee.TestingUtilities.getV2ResourcePath;


public class LocalResponseV2Test {
  /**
   * Fake secret key.
   */
  String secretKey = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";

  /**
   * Real signature using fake secret key.
   */
  String signature = "b82a515c832fd2c4f4ce3a7e6f53c12e8d10e19223f6cf0e3a9809a7a3da26be";

  /**
   * File which the signature applies to.
   */
  Path filePath = getV2ResourcePath("inference/standard_field_types.json");

  protected void assertLocalResponse(LocalResponse localResponse) {
    Assertions.assertNotNull(localResponse.getFile());
    Assertions.assertFalse(localResponse.isValidHmacSignature(
        this.secretKey, "invalid signature is invalid")
    );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
    InferenceResponse response = localResponse.deserializeResponse(InferenceResponse.class);
  }

  @Test
  void loadDocument_withFile_mustReturnValidLocalResponse() throws IOException {
    LocalResponse localResponse = new LocalResponse(new File(this.filePath.toString()));
    assertLocalResponse(localResponse);
  }

  @Test
  void loadDocument_withString_mustReturnValidLocalResponse() {
    LocalResponse localResponse = new LocalResponse("{'some': 'json', 'with': 'data'}");
    Assertions.assertNotNull(localResponse.getFile());
    Assertions.assertFalse(localResponse.isValidHmacSignature(
        this.secretKey, "invalid signature is invalid")
    );
  }

  @Test
  void loadDocument_withInputStream_mustReturnValidLocalResponse() throws IOException {
    LocalResponse localResponse = new LocalResponse(
        Files.newInputStream(this.filePath)
    );
    assertLocalResponse(localResponse);
  }
}
