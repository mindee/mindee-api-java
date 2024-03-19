package com.mindee.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


public class LocalResponseTest {
  /**
   * Fake secret key.
   */
  String secretKey = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";

  /**
   * Real signature using fake secret key.
   */
  String signature = "5ed1673e34421217a5dbfcad905ee62261a3dd66c442f3edd19302072bbf70d0";
  
  /**
   * File which the signature applies to.
   */
  String filePath = "src/test/resources/async/get_completed_empty.json";

  @Test
  void loadDocument_withFile_mustReturnValidLocalResponse() throws IOException {
    LocalResponse localResponse = new LocalResponse(new File(this.filePath));
    Assertions.assertNotNull(localResponse.getFile());
    Assertions.assertFalse(localResponse.isValidHmacSignature(
        this.secretKey, "invalid signature is invalid")
    );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
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
        Files.newInputStream(Paths.get(this.filePath))
    );
    Assertions.assertNotNull(localResponse.getFile());
    Assertions.assertFalse(localResponse.isValidHmacSignature(
        this.secretKey, "invalid signature is invalid")
    );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
  }
}
