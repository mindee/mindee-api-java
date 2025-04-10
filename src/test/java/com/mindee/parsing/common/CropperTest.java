package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.standard.PositionField;
import com.mindee.product.receipt.ReceiptV5;
import com.mindee.product.receipt.ReceiptV5Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class CropperTest {

  private List<Page<ReceiptV5Document>> loadResult() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptV5.class);
    PredictResponse<ReceiptV5> prediction = objectMapper.readValue(
      new File("src/test/resources/extras/cropper/complete.json"),
      type);

    return prediction.getDocument().getInference().getPages();
  }

  @Test
  void should_GetCropperResult() throws IOException {
    List<Page<ReceiptV5Document>> pages = loadResult();
    List<PositionField> cropping = pages.get(0).getExtras().getCropper().getCropping();
    Assertions.assertEquals("Polygon with 24 points.", cropping.get(0).toString());
  }
}
