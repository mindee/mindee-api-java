package com.mindee.v2.product;

import static com.mindee.TestingUtilities.assertStringEqualsFile;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.input.LocalResponse;
import com.mindee.v2.product.crop.CropItem;
import com.mindee.v2.product.crop.CropResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Crop Model Tests")
public class CropTest {
  private CropResponse loadResponse(String filePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(CropResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      CropResponse response = loadResponse("products/crop/crop_single.json");
      assertNotNull(response.getInference());

      ArrayList<CropItem> crops = response.getInference().getResult().getCrops();
      assertEquals(1, crops.size());

      CropItem crop1 = crops.get(0);
      assertEquals("invoice", crop1.getObjectType());
      assertNotNull(crop1.getLocation().getPolygon());
      assertEquals(0, crop1.getLocation().getPage());
    }

    @Test
    @DisplayName("RST output must be valid")
    void mustHaveValidDisplay() throws IOException {
      CropResponse response = loadResponse("products/crop/crop_single.json");
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
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      CropResponse response = loadResponse("products/crop/crop_multiple.json");
      assertNotNull(response.getInference());

      ArrayList<CropItem> crops = response.getInference().getResult().getCrops();
      assertEquals(2, crops.size());

      CropItem crop1 = crops.get(0);
      assertEquals("invoice", crop1.getObjectType());
      assertNotNull(crop1.getLocation().getPolygon());
      assertEquals(0, crop1.getLocation().getPage());

      CropItem crop2 = crops.get(1);
      assertEquals("invoice", crop2.getObjectType());
      assertNotNull(crop2.getLocation().getPolygon());
      assertEquals(0, crop2.getLocation().getPage());
    }

    @Test
    @DisplayName("RST output must be valid")
    void mustHaveValidDisplay() throws IOException {
      CropResponse response = loadResponse("products/crop/crop_multiple.json");
      assertStringEqualsFile(
        response.getInference().toString(),
        getV2ResourcePath("products/crop/crop_multiple.rst")
      );
    }
  }
}
