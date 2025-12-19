package com.mindee.input;

import static com.mindee.TestingUtilities.getV2ResourcePath;

import com.mindee.parsing.v2.InferenceResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class LocalResponseV2Test {
  /**
   * Fake secret key.
   */
  String secretKey = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";

  /**
   * Real signature using fake secret key.
   */
  String signature = "1df388c992d87897fe61dfc56c444c58fc3c7369c31e2b5fd20d867695e93e85";

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
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getInference());
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
