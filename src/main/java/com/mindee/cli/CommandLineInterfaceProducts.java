package com.mindee.cli;

import com.mindee.product.barcodereader.BarcodeReaderV1;
import com.mindee.product.cropper.CropperV1;
import com.mindee.product.driverlicense.DriverLicenseV1;
import com.mindee.product.financialdocument.FinancialDocumentV1;
import com.mindee.product.fr.bankaccountdetails.BankAccountDetailsV2;
import com.mindee.product.fr.cartegrise.CarteGriseV1;
import com.mindee.product.fr.idcard.IdCardV2;
import com.mindee.product.ind.indianpassport.IndianPassportV1;
import com.mindee.product.internationalid.InternationalIdV2;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.passport.PassportV1;
import com.mindee.product.receipt.ReceiptV5;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;

/**
 * Product-wrapper class for CLI use.
 */
public class CommandLineInterfaceProducts {
  private final ProductProcessor processor;

  /**
   * Default constructor.
   *
   * @param processor Processor instance to render the products.
   */
  public CommandLineInterfaceProducts(ProductProcessor processor) {
    this.processor = processor;
  }

  @CommandLine.Command(
      name = "fr-bank-account-details",
      description = "Parse using FR Bank Account Details"
  )
  void bankAccountDetailsV2Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(BankAccountDetailsV2.class, file));
  }

  @CommandLine.Command(name = "barcode-reader", description = "Parse using Barcode Reader")
  void barcodeReaderV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(BarcodeReaderV1.class, file));
  }

  @CommandLine.Command(name = "fr-carte-grise", description = "Parse using FR Carte Grise")
  void carteGriseV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(CarteGriseV1.class, file));
  }

  @CommandLine.Command(name = "cropper", description = "Parse using Cropper")
  void cropperV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(CropperV1.class, file));
  }

  @CommandLine.Command(name = "driver-license", description = "Parse using Driver License")
  void driverLicenseV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(DriverLicenseV1.class, file));
  }

  @CommandLine.Command(name = "financial-document", description = "Parse using Financial Document")
  void financialDocumentV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(FinancialDocumentV1.class, file));
  }

  @CommandLine.Command(
      name = "fr-carte-nationale-d-identite",
      description = "Parse using FR Carte Nationale d'Identité"
  )
  void idCardV2Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(IdCardV2.class, file));
  }

  @CommandLine.Command(
      name = "ind-passport-india",
      description = "Parse using IND Passport - India"
  )
  void indianPassportV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(IndianPassportV1.class, file));
  }

  @CommandLine.Command(name = "international-id", description = "Parse using International ID")
  void internationalIdV2Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InternationalIdV2.class, file));
  }

  @CommandLine.Command(name = "invoice-splitter", description = "Parse using Invoice Splitter")
  void invoiceSplitterV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InvoiceSplitterV1.class, file));
  }

  @CommandLine.Command(name = "invoice", description = "Parse using Invoice")
  void invoiceV4Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InvoiceV4.class, file));
  }

  @CommandLine.Command(
      name = "multi-receipts-detector",
      description = "Parse using Multi Receipts Detector"
  )
  void multiReceiptsDetectorV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(MultiReceiptsDetectorV1.class, file));
  }

  @CommandLine.Command(name = "passport", description = "Parse using Passport")
  void passportV1Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(PassportV1.class, file));
  }

  @CommandLine.Command(name = "receipt", description = "Parse using Receipt")
  void receiptV5Method(
      @CommandLine.Parameters(
          index = "0",
          paramLabel = "<path>",
          scope = CommandLine.ScopeType.LOCAL
      ) File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(ReceiptV5.class, file));
  }
}
