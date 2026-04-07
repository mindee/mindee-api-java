package com.mindee.v2.input;

import static com.mindee.TestingUtilities.getV2ResourcePath;

import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocalResponseTest {
  /**
   * Fake secret key.
   */
  String secretKey = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";

  /**
   * Real signature using a fake secret key.
   */
  String signature = "e51bdf80f1a08ed44ee161100fc30a25cb35b4ede671b0a575dc9064a3f5dbf1";

  /**
   * File that the signature applies to.
   */
  Path filePath = getV2ResourcePath("products/extraction/standard_field_types.json");

  protected void assertLocalResponse(LocalResponse localResponse) {
    Assertions.assertNotNull(localResponse.getFile());
    Assertions
      .assertFalse(
        localResponse.isValidHmacSignature(this.secretKey, "invalid signature is invalid")
      );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
    var response = localResponse.deserializeResponse(ExtractionResponse.class);
    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getInference());
  }

  @Test
  void loadDocument_withFile_mustReturnValidLocalResponse() throws IOException {
    assertLocalResponse(new LocalResponse(new File(this.filePath.toString())));
  }

  @Test
  void loadDocument_withString_mustReturnValidLocalResponse() {
    var localResponse = new LocalResponse("{'some': 'json', 'with': 'data'}");
    Assertions.assertNotNull(localResponse.getFile());
    Assertions
      .assertFalse(
        localResponse.isValidHmacSignature(this.secretKey, "invalid signature is invalid")
      );
  }

  @Test
  void loadDocument_withInputStream_mustReturnValidLocalResponse() throws IOException {
    var localResponse = new LocalResponse(Files.newInputStream(this.filePath));
    assertLocalResponse(localResponse);
  }
}
