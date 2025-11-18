package com.mindee.v1.extraction;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.Page;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.barcodereader.BarcodeReaderV1;
import com.mindee.product.barcodereader.BarcodeReaderV1Document;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static com.mindee.TestingUtilities.getResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePath;
import static com.mindee.TestingUtilities.getV1ResourcePathString;


public class ImageExtractorTest {

  protected PredictResponse<MultiReceiptsDetectorV1> getMultiReceiptsPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      MultiReceiptsDetectorV1.class
    );
    return objectMapper.readValue(
      getV1ResourcePath("products/multi_receipts_detector/response_v1/" + name + ".json").toFile(),
      type
    );
  }

  protected PredictResponse<BarcodeReaderV1> getBarcodeReaderPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      BarcodeReaderV1.class
    );
    return objectMapper.readValue(
      getV1ResourcePath("products/barcode_reader/response_v1/" + name + ".json").toFile(),
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
      List<ExtractedImage> subImages = extractor.extractImagesFromPage(
        page.getPrediction().getReceipts(), page.getPageId()
      );
      for (int i = 0; i < subImages.size(); i++) {
        ExtractedImage extractedImage = subImages.get(i);
        Assertions.assertNotNull(extractedImage.getImage());
        extractedImage.writeToFile("src/test/resources/output/");

        LocalInputSource source = extractedImage.asInputSource();
        Assertions.assertEquals(
          String.format("default_sample_page-001_%3s.jpg", i + 1).replace(" ", "0"),
          source.getFilename()
        );
      }
    }
  }

  @Test
  public void givenAnImage_shouldExtractValueFields() throws IOException {
    PredictResponse<BarcodeReaderV1> response = getBarcodeReaderPrediction("complete");
    BarcodeReaderV1 inference = response.getDocument().getInference();

    ImageExtractor extractor = new ImageExtractor(
        getV1ResourcePathString("products/barcode_reader/default_sample.jpg")
    );
    Assertions.assertEquals(1, extractor.getPageCount());

    for (Page<BarcodeReaderV1Document> page : inference.getPages()) {
      List<ExtractedImage> codes1D = extractor.extractImagesFromPage(
        page.getPrediction().getCodes1D(), page.getPageId(), "barcodes_1D.png"
      );
      for (int i = 0; i < codes1D.size(); i++) {
        ExtractedImage extractedImage = codes1D.get(i);
        Assertions.assertNotNull(extractedImage.getImage());
        LocalInputSource source = extractedImage.asInputSource();
        Assertions.assertEquals(
          String.format("barcodes_1D_page-001_%3s.png", i + 1).replace(" ", "0"),
          source.getFilename()
        );
        extractedImage.writeToFile(getResourcePath("output/"));
      }
      List<ExtractedImage> codes2D = extractor.extractImagesFromPage(
        page.getPrediction().getCodes2D(), page.getPageId(),"barcodes_2D.png"
      );
      for (ExtractedImage extractedImage : codes2D) {
        Assertions.assertNotNull(extractedImage.getImage());
        extractedImage.writeToFile(getResourcePath("output/"));
      }
    }
  }

  @Test
  public void givenAPdf_shouldExtractPositionFields() throws IOException {
    LocalInputSource image = new LocalInputSource(
      getV1ResourcePath("products/multi_receipts_detector/multipage_sample.pdf")
    );
    PredictResponse<MultiReceiptsDetectorV1> response = getMultiReceiptsPrediction("multipage_sample");
    MultiReceiptsDetectorV1 inference = response.getDocument().getInference();

    ImageExtractor extractor = new ImageExtractor(image);
    Assertions.assertEquals(2, extractor.getPageCount());

    for (Page<MultiReceiptsDetectorV1Document> page : inference.getPages()) {
      List<ExtractedImage> subImages = extractor.extractImagesFromPage(
        page.getPrediction().getReceipts(),
        page.getPageId()
      );

      for (int i = 0; i < subImages.size(); i++) {
        ExtractedImage extractedImage = subImages.get(i);
        Assertions.assertNotNull(extractedImage.getImage());
        extractedImage.writeToFile(getResourcePath("output/"));

        LocalInputSource source = extractedImage.asInputSource();
        Assertions.assertEquals(
          String.format("multipage_sample_page-%3s_%3s.jpg", page.getPageId() + 1,  i + 1).replace(" ", "0"),
          source.getFilename()
        );
      }
    }
  }
}
