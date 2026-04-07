package com.mindee.v2.product;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.crop.CropResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Crop Model Tests")
public class CropTest {
  private CropResponse loadResponse(String filePath) throws IOException {
    var localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(CropResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("crop properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/crop/crop_single.json");
      assertNotNull(response.getInference());

      var crops = response.getInference().getResult().getCrops();
      assertEquals(1, crops.size());

      var crop0 = crops.get(0);
      assertEquals("invoice", crop0.getObjectType());
      assertNotNull(crop0.getLocation().getPolygon());
      assertEquals(0, crop0.getLocation().getPage());
    }

    @Test
    @DisplayName("RST output must be valid")
    void mustHaveValidDisplay() throws IOException {
      var response = loadResponse("products/crop/crop_single.json");
      assertStringEqualsFile(
        response.getInference().toString(),
        getV2ResourcePath("products/crop/crop_single.rst")
      );
    }
  }

  @Nested
  @DisplayName("Result with multiple values")
  class MultiPredictionTest {
    @Test
    @DisplayName("crop properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/crop/crop_multiple.json");
      assertNotNull(response.getInference());

      var crops = response.getInference().getResult().getCrops();
      assertEquals(2, crops.size());

      var crop0 = crops.get(0);
      assertEquals("invoice", crop0.getObjectType());
      assertNotNull(crop0.getLocation().getPolygon());
      assertEquals(0, crop0.getLocation().getPage());

      var crop1 = crops.get(1);
      assertEquals("receipt", crop1.getObjectType());
      assertNotNull(crop1.getLocation().getPolygon());
      assertEquals(0, crop1.getLocation().getPage());
    }

    @Test
    @DisplayName("RST output must be valid")
    void mustHaveValidDisplay() throws IOException {
      var response = loadResponse("products/crop/crop_multiple.json");
      assertStringEqualsFile(
        response.getInference().toString(),
        getV2ResourcePath("products/crop/crop_multiple.rst")
      );
    }

    @Test
    @DisplayName("extraction properties must be valid")
    void extractionMustHaveValidProperties() throws IOException {
      CropResponse response = loadResponse("products/crop/default_sample_extraction.json");
      assertNotNull(response.getInference());

      var crops = response.getInference().getResult().getCrops();
      assertEquals(2, crops.size());

      var crop0 = crops.get(0);
      assertEquals("receipt", crop0.getObjectType());
      assertNotNull(crop0.getLocation().getPolygon());
      assertEquals(0, crop0.getLocation().getPage());
      ExtractionResponse extractionResponse0 = crop0.getExtractionResponse();
      assertNotNull(extractionResponse0);
      assertEquals(
        "CHEZ ALAIN MIAM MIAM",
        extractionResponse0
          .getInference()
          .getResult()
          .getFields()
          .getSimpleField("supplier_name")
          .getValue()
      );

      var crop1 = crops.get(1);
      assertEquals("receipt", crop1.getObjectType());
      assertNotNull(crop1.getLocation().getPolygon());
      assertEquals(0, crop1.getLocation().getPage());
      ExtractionResponse extractionResponse1 = crop1.getExtractionResponse();
      assertNotNull(extractionResponse1);
      assertEquals(
        "La cerise sur la pizza",
        extractionResponse1
          .getInference()
          .getResult()
          .getFields()
          .getSimpleField("supplier_name")
          .getValue()
      );
    }
  }
}
