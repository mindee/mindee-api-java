package com.mindee.v2.fileOperations;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.crop.CropResponse;
import java.nio.file.Files;
import org.junit.jupiter.api.Test;

class CropTest {
  @Test
  void singlePageSingleCrop_cropsCorrectly() throws Exception {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/crop/default_sample.jpg"));
    var localResponse = new LocalResponse(getV2ResourcePath("products/crop/default_sample.json"));
    var doc = localResponse.deserializeResponse(CropResponse.class);

    var extractedCrop = new Crop(inputSample)
      .extractSingle(doc.getInference().getResult().getCrops().get(0));

    assertEquals(0, extractedCrop.getPageId());
    assertEquals("default_sample_000.jpg", extractedCrop.getFilename());

    assertEquals(1056, extractedCrop.getImage().getWidth());
    assertEquals(2070, extractedCrop.getImage().getHeight());
  }

  @Test
  void singlePageMultiCrop_cropsCorrectly() throws Exception {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/crop/default_sample.jpg"));
    var localResponse = new LocalResponse(getV2ResourcePath("products/crop/default_sample.json"));
    var doc = localResponse.deserializeResponse(CropResponse.class);

    var extractedCrops = new Crop(inputSample)
      .extractMultiple(doc.getInference().getResult().getCrops());

    assertEquals(2, extractedCrops.size());

    var crop0 = extractedCrops.get(0);
    assertEquals(0, crop0.getPageId());
    assertEquals("default_sample_001.jpg", crop0.getFilename());

    assertEquals(1056, crop0.getImage().getWidth());
    assertEquals(2070, crop0.getImage().getHeight());

    var outputPath = getResourcePath("output");
    extractedCrops.saveAllToDisk(outputPath);
    assertTrue(Files.exists(outputPath.resolve("default_sample_001.jpg")));
    assertTrue(Files.exists(outputPath.resolve("default_sample_002.jpg")));
  }

  @Test
  void multiPageMultiCrop_cropsCorrectly() throws Exception {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/crop/multipage_sample.pdf"));
    var localResponse = new LocalResponse(getV2ResourcePath("products/crop/multipage_sample.json"));
    var doc = localResponse.deserializeResponse(CropResponse.class);

    var extractedCrops = new Crop(inputSample)
      .extractMultiple(doc.getInference().getResult().getCrops());

    assertEquals(5, extractedCrops.size());

    var crop0 = extractedCrops.get(0);
    assertEquals(0, crop0.getPageId());
    assertEquals("multipage_sample.pdf_001.jpg", crop0.getFilename());
    assertEquals(555, crop0.getImage().getWidth());
    assertEquals(1533, crop0.getImage().getHeight());

    var crop3 = extractedCrops.get(3);
    assertEquals(1, crop3.getPageId());
    assertEquals("multipage_sample.pdf_004.jpg", crop3.getFilename());
    assertEquals(562, crop3.getImage().getWidth());
    assertEquals(974, crop3.getImage().getHeight());

    var outputPath = getResourcePath("output");
    extractedCrops.saveAllToDisk(outputPath);
    assertTrue(Files.exists(outputPath.resolve("multipage_sample.pdf_001.jpg")));
    assertTrue(Files.exists(outputPath.resolve("multipage_sample.pdf_005.jpg")));
  }
}
