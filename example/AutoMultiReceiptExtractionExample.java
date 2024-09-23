import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.extraction.ExtractedImage;
import com.mindee.extraction.ImageExtractor;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.common.Page;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1Document;
import com.mindee.product.receipt.ReceiptV5;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AutoMultiReceiptExtractionExample {
  private static final String API_KEY = "my-api-key";
  private static final MindeeClient mindeeClient = new MindeeClient(API_KEY);

  public static void main(String[] args) throws IOException, InterruptedException {
    String myFilePath = "/path/to/the/file.ext";
    processMultiReceipts(myFilePath);
  }

  private static void processMultiReceipts(String filePath) throws IOException, InterruptedException {
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));
    PredictResponse<MultiReceiptsDetectorV1> resultSplit =
      mindeeClient.parse(MultiReceiptsDetectorV1.class, inputSource);

    ImageExtractor imageExtractor = new ImageExtractor(inputSource);

    for (Page<MultiReceiptsDetectorV1Document> page : resultSplit.getDocument().getInference().getPages()) {
      List<ExtractedImage> subImages = imageExtractor.extractImagesFromPage(
        page.getPrediction().getReceipts(),
        page.getPageId()
      );

      for (ExtractedImage subImage : subImages) {
        // Optionally: write to a file
        // subImage.writeToFile("/path/to/my/extracted/file/folder");

        PredictResponse<ReceiptV5> resultReceipt =
          mindeeClient.parse(ReceiptV5.class, subImage.asInputSource());
        System.out.println(resultReceipt.getDocument().toString());
      }
    }
  }
}
