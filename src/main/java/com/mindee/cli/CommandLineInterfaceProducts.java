package com.mindee.cli;

import com.mindee.product.financialdocument.FinancialDocumentV1;
import com.mindee.product.internationalid.InternationalIdV2;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.passport.PassportV1;
import com.mindee.product.receipt.ReceiptV4;
import com.mindee.product.us.usmail.UsMailV3;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;

/**
 * Product-wrapper class for CLI use.
 */

@CommandLine.Command(
    name = "CLI",
    scope = CommandLine.ScopeType.INHERIT
)
public class CommandLineInterfaceProducts {
  private final ProductProcessor processor;

  /**
   * @param processor Processor instance to render the products.
   */
  public CommandLineInterfaceProducts(ProductProcessor processor) {
    this.processor = processor;
  }

  @CommandLine.Command(name = "invoice", description = "Parse using Invoice")
  void invoiceMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(processor.standardProductOutput(InvoiceV4.class, file));
  }

  @CommandLine.Command(name = "receipt", description = "Parse using Expense Receipt")
  void receiptMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(processor.standardProductOutput(ReceiptV4.class, file));
  }

  @CommandLine.Command(name = "financial-document", description = "Parse using Financial Document")
  void financialDocumentMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(processor.standardProductOutput(FinancialDocumentV1.class, file));
  }

  @CommandLine.Command(name = "multi-receipt-detector", description = "Parse using Multi Receipts Detector")
  void multiReceiptDetectorMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(processor.standardProductOutput(MultiReceiptsDetectorV1.class, file));
  }

  @CommandLine.Command(name = "passport", description = "Parse using Passport")
  void passportMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(processor.standardProductOutput(PassportV1.class, file));
  }

  @CommandLine.Command(name = "invoice-splitter", description = "Parse using Invoice Splitter")
  void invoiceSplitterMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InvoiceSplitterV1.class, file));
  }

  @CommandLine.Command(name = "international-id", description = "Parse using International ID")
  void internationalIdMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InternationalIdV2.class, file));
  }

  @CommandLine.Command(name = "us-mail", description = "Parse using US Mail")
  void usMailMethod(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(UsMailV3.class, file));
  }
}
