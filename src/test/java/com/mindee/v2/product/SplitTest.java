package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.v2.product.split.SplitRange;
import com.mindee.v2.product.split.SplitResponse;
import java.io.IOException;
import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - Split Model Tests")
public class SplitTest {
  private SplitResponse loadResponse(String filePath) throws IOException {
    LocalResponse localResponse = new LocalResponse(getV2ResourcePath(filePath));
    return localResponse.deserializeResponse(SplitResponse.class);
  }

  @Nested
  @DisplayName("Result with single value")
  class SinglePredictionTest {
    @Test
    @DisplayName("split properties must be valid")
    void mustHaveValidProperties() throws IOException {
      SplitResponse response = loadResponse("products/split/split_single.json");
      assertNotNull(response.getInference());

      ArrayList<SplitRange> splits = response.getInference().getResult().getSplits();
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
      SplitResponse response = loadResponse("products/split/split_multiple.json");
      assertNotNull(response.getInference());

      ArrayList<SplitRange> splits = response.getInference().getResult().getSplits();
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

      ArrayList<SplitRange> splits = response.getInference().getResult().getSplits();
      assertEquals(2, splits.size());

      SplitRange split0 = splits.get(0);
      assertEquals("invoice", split0.getDocumentType());
      assertEquals(0, split0.getPageRange().get(0));
      InferenceResponse extractionResponse0 = split0.getExtractionResponse();
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
      InferenceResponse extractionResponse1 = split1.getExtractionResponse();
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
  }
}
