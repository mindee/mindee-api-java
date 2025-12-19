package com.mindee.parsing.common;

import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.product.internationalid.InternationalIdV2;
import com.mindee.product.internationalid.InternationalIdV2Document;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class FullTextOcrTest {
  private Inference<InternationalIdV2Document, InternationalIdV2Document> loadInference() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      AsyncPredictResponse.class,
      InternationalIdV2.class);
    AsyncPredictResponse<InternationalIdV2> prediction = objectMapper.readValue(
      getV1ResourcePath("extras/full_text_ocr/complete.json").toFile(),
      type);

    return prediction.getDocumentObj().getInference();
  }

  private List<Page<InternationalIdV2Document>> loadPages() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      AsyncPredictResponse.class,
      InternationalIdV2.class);
    AsyncPredictResponse<InternationalIdV2> prediction = objectMapper.readValue(
      getV1ResourcePath("extras/full_text_ocr/complete.json").toFile(),
      type);

    return prediction.getDocumentObj().getInference().getPages();
  }

  @Test
  void should_GetFullTextOcrResult() throws IOException {
    List<String> expectedText = Files.readAllLines(
        getV1ResourcePath("extras/full_text_ocr/full_text_ocr.txt")
    );
    List<Page<InternationalIdV2Document>> pages = loadPages();
    Inference<InternationalIdV2Document, InternationalIdV2Document> inference = loadInference();
    String fullTextOcr = inference.getExtras().getFullTextOcr();
    String page0Ocr = pages.get(0).getExtras().getFullTextOcr().getContent();
    Assertions.assertEquals(String.join("\n", expectedText), fullTextOcr);
    Assertions.assertEquals(String.join("\n", expectedText), page0Ocr);
  }
}
