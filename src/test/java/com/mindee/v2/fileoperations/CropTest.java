package com.mindee.v2.fileoperations;

import static com.mindee.TestingUtilities.deleteRecursively;
import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.crop.CropResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CropTest {
  private static final Path outputPath = getResourcePath("output/v2/file_operations/crop");

  @BeforeAll
  public static void setup() throws IOException {
    deleteRecursively(outputPath);
    Files.createDirectories(outputPath);
  }

  @Test
  void singlePageCrop_cropsCorrectly() throws Exception {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/crop/default_sample.jpg"));
    var localResponse = new LocalResponse(getV2ResourcePath("products/crop/default_sample.json"));
    var doc = localResponse.deserializeResponse(CropResponse.class);

    var extractedCrops = new Crop(inputSample)
      .extractMultipleCrops(doc.getInference().getResult().getCrops());

    assertEquals(2, extractedCrops.size());

    extractedCrops.saveAllToDisk(outputPath);

    var crop0 = extractedCrops.get(0);
    assertEquals(0, crop0.getPageId());
    assertEquals(0, crop0.getElementId());
    assertEquals("default_sample_page-001-item-001.jpg", crop0.getFilename());
    assertEquals(2070, crop0.getImage().getHeight());
    assertEquals(1056, crop0.getImage().getWidth());
    assertTrue(Files.exists(outputPath.resolve("default_sample_page-001-item-001.jpg")));

    var crop1 = extractedCrops.get(1);
    assertEquals(0, crop1.getPageId());
    assertEquals(1, crop1.getElementId());
    assertEquals("default_sample_page-001-item-002.jpg", crop1.getFilename());
    assertEquals(1868, crop1.getImage().getHeight());
    assertEquals(1298, crop1.getImage().getWidth());
    assertTrue(Files.exists(outputPath.resolve("default_sample_page-001-item-002.jpg")));
  }

  @Test
  void multiPageCrop_cropsCorrectly() throws Exception {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/crop/multipage_sample.pdf"));
    var localResponse = new LocalResponse(getV2ResourcePath("products/crop/multipage_sample.json"));
    var doc = localResponse.deserializeResponse(CropResponse.class);

    var extractedCrops = new Crop(inputSample)
      .extractMultipleCrops(doc.getInference().getResult().getCrops());

    assertEquals(5, extractedCrops.size());

    extractedCrops.saveAllToDisk(outputPath);

    var crop0 = extractedCrops.get(0);
    assertEquals(0, crop0.getPageId());
    assertEquals("multipage_sample_page-001-item-001.jpg", crop0.getFilename());
    assertEquals(1533, crop0.getImage().getHeight());
    assertEquals(555, crop0.getImage().getWidth());
    assertTrue(Files.exists(outputPath.resolve("multipage_sample_page-001-item-002.jpg")));

    var crop4 = extractedCrops.get(4);
    assertEquals(1, crop4.getPageId());
    assertEquals(1, crop4.getElementId());
    assertEquals("multipage_sample_page-002-item-002.jpg", crop4.getFilename());
    assertEquals(1445, crop4.getImage().getHeight());
    assertEquals(547, crop4.getImage().getWidth());
    assertTrue(Files.exists(outputPath.resolve("multipage_sample_page-002-item-002.jpg")));
  }
}
