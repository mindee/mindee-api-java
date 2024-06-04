package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.standard.StringField;
import com.mindee.product.internationalid.InternationalIdV2;
import com.mindee.product.internationalid.InternationalIdV2Document;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class FullTextOcrTest {
  private Inference<InternationalIdV2Document, InternationalIdV2Document> loadInference() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      AsyncPredictResponse.class,
      InternationalIdV2.class);
    AsyncPredictResponse<InternationalIdV2> prediction = objectMapper.readValue(
      new File("src/test/resources/extras/full_text_ocr/complete.json"),
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
      new File("src/test/resources/extras/full_text_ocr/complete.json"),
      type);

    return prediction.getDocumentObj().getInference().getPages();
  }

  @Test
  void should_GetFullTextOcrResult() throws IOException {
    List<String> expectedText = Files
      .readAllLines(Paths.get("src/test/resources/extras/full_text_ocr/full_text_ocr.txt"));
    List<Page<InternationalIdV2Document>> pages = loadPages();
    Inference<InternationalIdV2Document, InternationalIdV2Document> inference = loadInference();
    String fullTextOcr = inference.getExtras().getFullTextOcr();
    String page0Ocr = pages.get(0).getExtras().getFullTextOcr().getContent();
    Assertions.assertEquals(String.join("\n", expectedText), fullTextOcr);
    Assertions.assertEquals(String.join("\n", expectedText), page0Ocr);
  }
}
