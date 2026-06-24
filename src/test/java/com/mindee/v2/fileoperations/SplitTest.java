package com.mindee.v2.fileoperations;

import static com.mindee.TestingUtilities.deleteRecursively;
import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.split.SplitResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SplitTest {
  private static final Path outputPath = getResourcePath("output/v2/file_operations/split");

  @BeforeAll
  public static void setup() throws IOException {
    deleteRecursively(outputPath);
    Files.createDirectories(outputPath);
  }

  @Test
  void singlePage_splitsCorrectly() throws IOException {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/split/default_sample.pdf"));
    assertEquals(2, inputSample.getPageCount());
    var localResponse = new LocalResponse(getV2ResourcePath("products/split/default_sample.json"));
    var doc = localResponse.deserializeResponse(SplitResponse.class);

    var extractedSplit = new Split(inputSample)
      .extractSingleSplit(doc.getInference().getResult().getSplits().get(0));

    assertEquals("default_sample_pages-001-001.pdf", extractedSplit.getFilename());
    var asInputSource = extractedSplit.asInputSource();
    assertEquals(1, asInputSource.getPageCount());

    extractedSplit.writeToFile(outputPath);
  }

  @Test
  void multiplePages_splitsCorrectly() throws IOException {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/split/default_sample.pdf"));
    assertEquals(2, inputSample.getPageCount());
    var localResponse = new LocalResponse(getV2ResourcePath("products/split/default_sample.json"));
    var doc = localResponse.deserializeResponse(SplitResponse.class);

    var extractedSplits = new Split(inputSample)
      .extractMultipleSplits(doc.getInference().getResult().getSplits());

    assertEquals(2, extractedSplits.size());

    var split0 = extractedSplits.get(0);
    assertEquals("default_sample_pages-001-001.pdf", split0.getFilename());
    var asInputSource0 = split0.asInputSource();
    assertEquals(1, asInputSource0.getPageCount());

    var split1 = extractedSplits.get(1);
    assertEquals("default_sample_pages-002-002.pdf", split1.getFilename());
    var asInputSource1 = split1.asInputSource();
    assertEquals(1, asInputSource1.getPageCount());

    extractedSplits.saveAllToDisk(outputPath);
  }
}
