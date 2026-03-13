package com.mindee.v2.product;

import static com.mindee.TestingUtilities.getV2ResourcePath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mindee.input.LocalResponse;
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
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      SplitResponse response = loadResponse("products/split/split_single.json");
      assertNotNull(response.getInference());

      ArrayList<SplitRange> splits = response.getInference().getResult().getSplits();
      assertEquals(1, splits.size());
      assertEquals("receipt", splits.get(0).getDocumentType());
      assertEquals(0, splits.get(0).getPageRange().get(0));
    }
  }

  @Nested
  @DisplayName("Result with multiple values")
  class MultiPredictionTest {
    @Test
    @DisplayName("all properties must be valid")
    void mustHaveValidProperties() throws IOException {
      SplitResponse response = loadResponse("products/split/split_multiple.json");
      assertNotNull(response.getInference());

      ArrayList<SplitRange> splits = response.getInference().getResult().getSplits();
      assertEquals(3, splits.size());

      SplitRange split1 = splits.get(0);
      assertEquals("passport", split1.getDocumentType());
      assertEquals(0, split1.getPageRange().get(0));

      SplitRange split2 = splits.get(1);
      assertEquals("invoice", split2.getDocumentType());
      assertEquals(1, split2.getPageRange().get(0));

      SplitRange split3 = splits.get(2);
      assertEquals("receipt", split3.getDocumentType());
      assertEquals(4, split3.getPageRange().get(0));
    }
  }
}
