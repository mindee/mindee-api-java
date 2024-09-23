import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.extraction.ExtractedPDF;
import com.mindee.extraction.PDFExtractor;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class AutoInvoiceSplitterExtractionExample {
  private static final String API_KEY = "my-api-key";
  private static final MindeeClient mindeeClient = new MindeeClient(API_KEY);

  public static void main(String[] args) throws IOException, InterruptedException {
    String filePath = "/path/to/the/file.ext";
    invoiceSplitterAutoExtraction(filePath);
  }

  private static void invoiceSplitterAutoExtraction(String filePath) throws IOException, InterruptedException {
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    if (inputSource.isPdf() && new PDFExtractor(inputSource).getPageCount() > 1) {
      parseMultiPage(inputSource);
    } else {
      parseSinglePage(inputSource);
    }
  }

  private static void parseSinglePage(LocalInputSource inputSource) throws IOException, InterruptedException {
    AsyncPredictResponse<InvoiceV4> invoiceResult = mindeeClient.enqueueAndParse(InvoiceV4.class, inputSource);
    System.out.println(invoiceResult.getDocumentObj().toString());
  }

  private static void parseMultiPage(LocalInputSource inputSource) throws IOException, InterruptedException {
    PDFExtractor extractor = new PDFExtractor(inputSource);
    AsyncPredictResponse<InvoiceSplitterV1> invoiceSplitterResponse =
      mindeeClient.enqueueAndParse(InvoiceSplitterV1.class, inputSource);

    List<ExtractedPDF> extractedPdfs = extractor.extractInvoices(
      invoiceSplitterResponse.getDocumentObj().getInference().getPrediction().getInvoicePageGroups(),
      false
    );

    for (ExtractedPDF extractedPdf : extractedPdfs) {
      // Optional: Save the files locally
      // extractedPdf.writeToFile("output/path");

      AsyncPredictResponse<InvoiceV4> invoiceResult =
        mindeeClient.enqueueAndParse(InvoiceV4.class, extractedPdf.asInputSource());
      System.out.println(invoiceResult.getDocumentObj().toString());
    }
  }
}
