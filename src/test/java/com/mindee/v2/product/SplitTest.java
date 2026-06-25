package com.mindee.v2.product;

import static com.mindee.TestingUtilities.deleteRecursively;
import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.fileoperations.Split;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.split.SplitRange;
import com.mindee.v2.product.split.SplitResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Split Model Tests")
public class SplitTest {
  private static final Path outputPath = getResourcePath("output/v2/product/split");

  @BeforeAll
  public static void setup() throws IOException {
    deleteRecursively(outputPath);
    Files.createDirectories(outputPath);
  }

  private SplitResponse loadResponse(String filePath) throws IOException {
    var localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(SplitResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("split properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/split/split_single.json");
      assertNotNull(response.getInference());

      var splits = response.getInference().getResult().getSplits();
      assertEquals(1, splits.size());
      SplitRange split0 = splits.get(0);
      assertEquals("receipt", split0.getDocumentType());
      assertEquals(0, split0.getPageRange().get(0));
    }
  }

  @Nested
  @DisplayName("Result with multiple values")
  class MultiPredictionTest {
    @Test
    @DisplayName("split properties must be valid")
    void mustHaveValidProperties() throws IOException {
      var response = loadResponse("products/split/split_multiple.json");
      assertNotNull(response.getInference());

      var splits = response.getInference().getResult().getSplits();
      assertEquals(3, splits.size());

      SplitRange split0 = splits.get(0);
      assertEquals("passport", split0.getDocumentType());
      assertEquals(0, split0.getPageRange().get(0));

      SplitRange split1 = splits.get(1);
      assertEquals("invoice", split1.getDocumentType());
      assertEquals(1, split1.getPageRange().get(0));

      SplitRange split2 = splits.get(2);
      assertEquals("receipt", split2.getDocumentType());
      assertEquals(4, split2.getPageRange().get(0));
    }

    @Test
    @DisplayName("extraction properties must be valid")
    void extractionMustHaveValidProperties() throws IOException {
      SplitResponse response = loadResponse("products/split/default_sample_extraction.json");
      assertNotNull(response.getInference());

      var splits = response.getInference().getResult().getSplits();
      assertEquals(2, splits.size());

      SplitRange split0 = splits.get(0);
      assertEquals("invoice", split0.getDocumentType());
      assertEquals(0, split0.getPageRange().get(0));
      ExtractionResponse extractionResponse0 = split0.getExtractionResponse();
      assertNotNull(extractionResponse0);
      assertEquals(
        "05 05 44 44 90",
        extractionResponse0
          .getInference()
          .getResult()
          .getFields()
          .getSimpleField("supplier_phone_number")
          .getValue()
      );

      SplitRange split1 = splits.get(1);
      assertEquals("invoice", split1.getDocumentType());
      assertEquals(1, split1.getPageRange().get(0));
      ExtractionResponse extractionResponse1 = split1.getExtractionResponse();
      assertNotNull(extractionResponse1);
      assertEquals(
        "416-555-1212",
        extractionResponse1
          .getInference()
          .getResult()
          .getFields()
          .getSimpleField("supplier_phone_number")
          .getValue()
      );
    }

    @Test
    @DisplayName("extract all crops works")
    void extractMultipleSplits() throws IOException {
      var inputSource = new LocalInputSource(
        getV2ResourcePath("products/split/default_sample.pdf")
      );

      SplitResponse response = loadResponse("products/split/default_sample_extraction.json");
      assertNotNull(response.getInference());

      var splits = response.getInference().getResult().getSplits();

      var splitter = new Split(inputSource);
      var classExtract = splitter.extractMultipleSplits(splits);

      assertNotNull(classExtract);
      assertEquals(splits.size(), classExtract.size());

      var methodExtract = response.getInference().getResult().extractFromInputSource(inputSource);
      assertEquals(classExtract.size(), methodExtract.size());

      classExtract.saveAllToDisk(outputPath.toString());

      assertTrue(Files.exists(outputPath.resolve("default_sample_pages-001-001.pdf")));
      assertTrue(Files.size(outputPath.resolve("default_sample_pages-001-001.pdf")) >= 1500);
      assertTrue(Files.exists(outputPath.resolve("default_sample_pages-002-002.pdf")));
      assertTrue(Files.size(outputPath.resolve("default_sample_pages-002-002.pdf")) >= 1500);
    }

    @Test
    @DisplayName("extract single crop works")
    void extractSingleSplit() throws IOException {
      var inputSource = new LocalInputSource(
        getV2ResourcePath("products/split/default_sample.pdf")
      );

      SplitResponse response = loadResponse("products/split/default_sample_extraction.json");
      assertNotNull(response.getInference());

      var extractedSplit = response
        .getInference()
        .getResult()
        .getSplits()
        .get(0)
        .extractFromInputSource(inputSource);

      extractedSplit.writeToFile(outputPath);

      assertTrue(Files.exists(outputPath.resolve("default_sample_pages-001-001.pdf")));
      assertTrue(Files.size(outputPath.resolve("default_sample_pages-001-001.pdf")) >= 1500);
    }
  }
}
