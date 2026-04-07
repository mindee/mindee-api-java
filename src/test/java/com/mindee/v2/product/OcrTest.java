package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.v2.input.LocalResponse;
import com.mindee.v2.product.ocr.OcrResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - OCR Model Tests")
public class OcrTest {
  private OcrResponse loadResponse(String filePath) throws IOException {
    var localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(OcrResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/ocr/ocr_single.json");
      assertNotNull(response.getInference());

      var pages = response.getInference().getResult().getPages();
      assertEquals(1, pages.size());
      assertEquals(305, pages.get(0).getWords().size());
      assertEquals("Shipper:", pages.get(0).getWords().get(0).getContent());
    }
  }

  @Nested
  @DisplayName("Result with multiple values")
  class MultiPredictionTest {
    @Test
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/ocr/ocr_multiple.json");
      assertNotNull(response.getInference());

      var pages = response.getInference().getResult().getPages();
      assertEquals(3, pages.size());

      var page1 = pages.get(0);
      assertNotNull(page1.getContent());
      assertEquals(295, page1.getWords().size());
      assertEquals("FICTIOCORP", page1.getWords().get(0).getContent());

      var page2 = pages.get(1);
      assertNotNull(page2.getContent());
      assertEquals(450, page2.getWords().size());
      assertEquals("KEOLIO", page2.getWords().get(0).getContent());
    }
  }
}
