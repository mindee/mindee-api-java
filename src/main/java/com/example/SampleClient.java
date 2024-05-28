package com.example;

import com.example.product.balticinvoice.BalticInvoiceV1;
import com.example.product.invoice.InvoiceEst;
import com.example.product.invoice.InvoiceLut;
import com.example.product.invoice.InvoiceLva;
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.PredictResponse;
import java.io.File;
import java.io.IOException;


public class SampleClient {
  MindeeClient mindeeClient;

  public SampleClient() {
    // Init a new client
    mindeeClient = new MindeeClient();
    //String apiKey = "my-api-key";
    //mindeeClient = new MindeeClient(apiKey);
  }

  public void testRun() throws IOException, InterruptedException {
    String filePath = "/home/ianare/Documents/sample_baltic_invoice.pdf";

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    /*
     * Combine standard Invoice and custom Baltic model.
     * Each country has its own rules for combining the data.
     * As a result each has its own class and its own helper method in this example class.
     */
    parseEstInvoice(inputSource);
    parseLvaInvoice(inputSource);
    parseLutInvoice(inputSource);
  }


  /**
   * Handle LVA invoices.
   */
  public void parseLvaInvoice(
      LocalInputSource inputSource
  ) throws IOException, InterruptedException {
    // Initial parse as invoice
    PredictResponse<InvoiceLva> invoiceResponse = mindeeClient.parse(
      InvoiceLva.class,
      inputSource
    );
    // Parse as baltic invoice
    AsyncPredictResponse<BalticInvoiceV1> balticResponse = parseBaltic(
      inputSource
    );
    // Combine the two
    invoiceResponse.getDocument().getInference().combineWithBaltic(
      balticResponse.getDocumentObj().getInference()
    );
    // Access some data
    System.out.println(
      invoiceResponse.getDocument().getInference().getPrediction().getCustomerName()
    );
  }

  /**
   * Handle EST invoices.
   */
  public void parseEstInvoice(
    LocalInputSource inputSource
  ) throws IOException, InterruptedException {
    // Initial parse as invoice
    PredictResponse<InvoiceEst> invoiceResponse = mindeeClient.parse(
      InvoiceEst.class,
      inputSource
    );
    //System.out.println(invoiceResponse.getDocument().getInference());

    // Parse as baltic invoice
    AsyncPredictResponse<BalticInvoiceV1> balticResponse = parseBaltic(
      inputSource
    );
    //System.out.println(balticResponse.getDocument());

    // Combine the two
    invoiceResponse.getDocument().getInference().combineWithBaltic(
      balticResponse.getDocumentObj().getInference()
    );
    // Access some data
    System.out.println(
      invoiceResponse.getDocument().getInference().getPrediction().getCustomerName()
    );
  }

  /**
   * Handle LUT invoices.
   */
  public void parseLutInvoice(
    LocalInputSource inputSource
  ) throws IOException, InterruptedException {
    // Initial parse as invoice
    PredictResponse<InvoiceLut> invoiceResponse = mindeeClient.parse(
      InvoiceLut.class,
      inputSource
    );
    // Parse as baltic invoice
    AsyncPredictResponse<BalticInvoiceV1> balticResponse = parseBaltic(
      inputSource
    );
    // Combine the two
    invoiceResponse.getDocument().getInference().combineWithBaltic(
      balticResponse.getDocumentObj().getInference()
    );
    // Access some data
    System.out.println(
      invoiceResponse.getDocument().getInference().getPrediction().getCustomerName()
    );
  }

  private AsyncPredictResponse<BalticInvoiceV1> parseBaltic(
    LocalInputSource inputSource
  ) throws InterruptedException, IOException {
    return mindeeClient.enqueueAndParse(BalticInvoiceV1.class, inputSource);
  }
}
