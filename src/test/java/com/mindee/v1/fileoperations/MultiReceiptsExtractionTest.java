package com.mindee.v1.fileoperations;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePath;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.image.ExtractedImage;
import com.mindee.image.ImageExtractor;
import com.mindee.input.LocalInputSource;
import com.mindee.v1.parsing.common.Page;
import com.mindee.v1.parsing.common.PredictResponse;
import com.mindee.v1.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.v1.product.multireceiptsdetector.MultiReceiptsDetectorV1Document;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MultiReceiptsExtractionTest {

  protected PredictResponse<MultiReceiptsDetectorV1> getMultiReceiptsPrediction(
      String name
  ) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper
      .getTypeFactory()
      .constructParametricType(PredictResponse.class, MultiReceiptsDetectorV1.class);
    return objectMapper
      .readValue(
        getV1ResourcePath("products/multi_receipts_detector/response_v1/" + name + ".json")
          .toFile(),
        type
      );
  }

  @Test
  public void givenAnImage_shouldExtractPositionFields() throws IOException {
    LocalInputSource image = new LocalInputSource(
      getV1ResourcePath("products/multi_receipts_detector/default_sample.jpg")
    );
    PredictResponse<MultiReceiptsDetectorV1> response = getMultiReceiptsPrediction("complete");
    MultiReceiptsDetectorV1 inference = response.getDocument().getInference();

    ImageExtractor extractor = new ImageExtractor(image);
    Assertions.assertEquals(1, extractor.getPageCount());

    for (Page<MultiReceiptsDetectorV1Document> page : inference.getPages()) {
      List<ExtractedImage> subImages = extractor
        .extractImagesFromPage(page.getPrediction().getReceipts(), page.getPageId());
      for (int i = 0; i < subImages.size(); i++) {
        ExtractedImage extractedImage = subImages.get(i);
        Assertions.assertNotNull(extractedImage.getImage());
        extractedImage.writeToFile("src/test/resources/output/");

        LocalInputSource source = extractedImage.asInputSource();
        Assertions
          .assertEquals(
            String
              .format("default_sample_page-%3s-item-%3s.jpg", page.getPageId() + 1, i + 1)
              .replace(" ", "0"),
            source.getFilename()
          );
      }
    }
  }

  @Test
  public void givenAPdf_shouldExtractPositionFields() throws IOException {
    LocalInputSource image = new LocalInputSource(
      getV1ResourcePath("products/multi_receipts_detector/multipage_sample.pdf")
    );
    PredictResponse<MultiReceiptsDetectorV1> response = getMultiReceiptsPrediction(
      "multipage_sample"
    );
    MultiReceiptsDetectorV1 inference = response.getDocument().getInference();

    ImageExtractor extractor = new ImageExtractor(image);
    Assertions.assertEquals(2, extractor.getPageCount());

    for (Page<MultiReceiptsDetectorV1Document> page : inference.getPages()) {
      List<ExtractedImage> subImages = extractor
        .extractImagesFromPage(page.getPrediction().getReceipts(), page.getPageId());

      for (int i = 0; i < subImages.size(); i++) {
        ExtractedImage extractedImage = subImages.get(i);
        Assertions.assertNotNull(extractedImage.getImage());
        extractedImage.writeToFile(getResourcePath("output/"));

        LocalInputSource source = extractedImage.asInputSource();
        Assertions
          .assertEquals(
            String
              .format("multipage_sample_page-%3s_%3s.jpg", page.getPageId() + 1, i + 1)
              .replace(" ", "0"),
            source.getFilename()
          );
      }
    }
  }
}
