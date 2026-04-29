package com.mindee.v2.fileOperations;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.parsing.LocalResponse;
import com.mindee.v2.product.split.SplitResponse;
import java.io.IOException;
import org.junit.jupiter.api.Test;

public class SplitTest {
  @Test
  void singlePage_splitsCorrectly() throws IOException {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/split/default_sample.pdf"));
    assertEquals(2, inputSample.getPageCount());
    var localResponse = new LocalResponse(getV2ResourcePath("products/split/default_sample.json"));
    var doc = localResponse.deserializeResponse(SplitResponse.class);

    var extractedSplit = new Split(inputSample)
      .extractSingle(doc.getInference().getResult().getSplits().get(0));

    assertEquals("default_sample_001-001.pdf", extractedSplit.getFilename());
    var asInputSource = extractedSplit.asInputSource();
    assertEquals(1, asInputSource.getPageCount());
  }

  @Test
  void multiplePages_splitsCorrectly() throws IOException {
    var inputSample = new LocalInputSource(getV2ResourcePath("products/split/default_sample.pdf"));
    assertEquals(2, inputSample.getPageCount());
    var localResponse = new LocalResponse(getV2ResourcePath("products/split/default_sample.json"));
    var doc = localResponse.deserializeResponse(SplitResponse.class);

    var extractedSplits = new Split(inputSample)
      .extractMultiple(doc.getInference().getResult().getSplits());

    assertEquals(2, extractedSplits.size());

    var split0 = extractedSplits.get(0);
    assertEquals("default_sample_001-001.pdf", split0.getFilename());
    var asInputSource0 = split0.asInputSource();
    assertEquals(1, asInputSource0.getPageCount());

    var split1 = extractedSplits.get(1);
    assertEquals("default_sample_002-002.pdf", split1.getFilename());
    var asInputSource1 = split1.asInputSource();
    assertEquals(1, asInputSource1.getPageCount());
  }
}
