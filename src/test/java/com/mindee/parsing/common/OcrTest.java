package com.mindee.parsing.common;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.ocr.Word;
import com.mindee.parsing.common.ocr.Ocr;
import com.mindee.product.receipt.ReceiptV5;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static com.mindee.TestingUtilities.getV1ResourcePath;

public class OcrTest {

  private Ocr loadResult() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      ReceiptV5.class);
    PredictResponse<ReceiptV5> prediction = objectMapper.readValue(
      getV1ResourcePath("extras/ocr/complete.json").toFile(),
      type);

    return prediction.getDocument().getOcr();
  }

  @Test
  void whenGettingAllLines_shouldNotAffectWordOrder() throws IOException {

    Ocr ocr = loadResult();

    // get the word list as deserialized from JSON
    List<Word> allWordsStart = ocr.getMVisionV1().getPages().get(0).getAllWords();

    // trigger any potential changes to the list order
    ocr.getMVisionV1().getPages().get(0).getAllLines();

    // get the word list as after any potential manipulations
    List<Word> allWordsEnd = ocr.getMVisionV1().getPages().get(0).getAllWords();

    Assertions.assertEquals(allWordsStart, allWordsEnd, "Word list should not change.");
  }

  @Test
  void whenDeserializedToString_shouldBeOrdered() throws IOException {

    Ocr ocr = loadResult();

    List<String> expectedLines = Files.readAllLines(
        getV1ResourcePath("extras/ocr/ocr.txt")
    );
    String expectedSummary = String.join(String.format("%n"), expectedLines);

    Assertions.assertEquals(expectedSummary, ocr.toString(), "Should match expected string exactly.");
  }
}
