package com.mindee.v1.parsing;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV1ResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePathString;

import com.mindee.MindeeException;
import com.mindee.v1.product.internationalid.InternationalIdV2;
import com.mindee.v1.product.invoice.InvoiceV4;
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
  String signature = "5ed1673e34421217a5dbfcad905ee62261a3dd66c442f3edd19302072bbf70d0";

  /**
   * File that the signature applies to.
   */
  Path filePath = getV1ResourcePath("async/get_completed_empty.json");

  @Test
  void loadDocument_withFile_mustReturnValidLocalResponse() throws IOException {
    var localResponse = new LocalResponse(new File(this.filePath.toString()));
    Assertions.assertNotNull(localResponse.getFile());
    Assertions
      .assertFalse(
        localResponse.isValidHmacSignature(this.secretKey, "invalid signature is invalid")
      );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
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
    Assertions.assertNotNull(localResponse.getFile());
    Assertions
      .assertFalse(
        localResponse.isValidHmacSignature(this.secretKey, "invalid signature is invalid")
      );
    Assertions.assertEquals(this.signature, localResponse.getHmacSignature(this.secretKey));
    Assertions.assertTrue(localResponse.isValidHmacSignature(this.secretKey, this.signature));
  }

  @Test
  void givenJsonInput_whenSync_shouldDeserializeCorrectly() throws IOException {
    var localResponse = new LocalResponse(
      getV1ResourcePath("products/invoices/response_v4/complete.json")
    );
    var response = localResponse.deserializeSyncResponse(InvoiceV4.class);
    assertStringEqualsFile(
      response.getDocument().toString(),
      getV1ResourcePath("/products/invoices/response_v4/summary_full.rst")
    );
  }

  @Test
  void givenJsonInput_whenAsync_shouldDeserializeCorrectly() throws IOException {
    var localResponse = new LocalResponse(
      getV1ResourcePath("products/international_id/response_v2/complete.json")
    );
    var response = localResponse.deserializeAsyncResponse(InternationalIdV2.class);
    assertStringEqualsFile(
      response.getDocumentObj().toString(),
      getV1ResourcePathString("products/international_id/response_v2/summary_full.rst")
    );
  }

  @Test
  void givenInvalidJsonInput_shouldThrow() {
    var localResponse = new LocalResponse("{invalid json");
    var err = Assertions
      .assertThrows(
        MindeeException.class,
        () -> localResponse.deserializeSyncResponse(InvoiceV4.class)
      );
    Assertions.assertEquals("Invalid JSON payload.", err.getMessage());
  }
}
