package com.mindee.extraction;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.barcodereader.BarcodeReaderV1;
import com.mindee.product.barcodereader.BarcodeReaderV1Document;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1Document;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ImageExtractorTest {

  protected PredictResponse<MultiReceiptsDetectorV1> getMultiReceiptsPrediction(String name) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(
      PredictResponse.class,
      MultiReceiptsDetectorV1.class
    );
    return objectMapper.readValue(
      new File("src/test/resources/products/multi_receipts_detector/response_v1/" + name + ".json"),
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
      new File("src/test/resources/products/barcode_reader/response_v1/" + name + ".json"),
      type
    );
  }

  @Test
  public void shouldExtractPositionFields() throws IOException {
    LocalInputSource image = new LocalInputSource(
      "src/test/resources/products/multi_receipts_detector/default_sample.jpg"
    );
    PredictResponse<MultiReceiptsDetectorV1> response = getMultiReceiptsPrediction("complete");
    MultiReceiptsDetectorV1Document prediction = response.getDocument().getInference().getPrediction();

    ImageExtractor extractor = new ImageExtractor(image);
    List<ExtractedImage> subImages = extractor.extractImages(prediction.getReceipts());
    for (int i = 0; i < subImages.size(); i++) {
      ExtractedImage extractedImage = subImages.get(i);
      Assertions.assertNotNull(extractedImage.getImage());
      extractedImage.writeToFile("src/test/resources/output/");

      LocalInputSource source = extractedImage.asSource();
      Assertions.assertEquals(
          String.format("default_sample_%3s.jpg", i + 1).replace(" ", "0"),
          source.getFilename()
      );
    }
  }

  @Test
  public void shouldExtractValueFields() throws IOException {
    String imagePath = "src/test/resources/products/barcode_reader/default_sample.jpg";
    PredictResponse<BarcodeReaderV1> response = getBarcodeReaderPrediction("complete");
    BarcodeReaderV1Document prediction = response.getDocument().getInference().getPrediction();

    ImageExtractor extractor = new ImageExtractor(imagePath);
    List<ExtractedImage> codes1D = extractor.extractImages(prediction.getCodes1D(), "barcodes_1D.png");
    for (ExtractedImage extractedImage : codes1D) {
      Assertions.assertNotNull(extractedImage.getImage());
      extractedImage.writeToFile("src/test/resources/output/");
    }
    List<ExtractedImage> codes2D = extractor.extractImages(prediction.getCodes2D(), "barcodes_2D.png");
    for (ExtractedImage extractedImage : codes2D) {
      Assertions.assertNotNull(extractedImage.getImage());
      extractedImage.writeToFile("src/test/resources/output/");
    }
  }
}
