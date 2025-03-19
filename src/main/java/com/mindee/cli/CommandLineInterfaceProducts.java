package com.mindee.cli;

import com.mindee.product.barcodereader.BarcodeReaderV1;
import com.mindee.product.billoflading.BillOfLadingV1;
import com.mindee.product.businesscard.BusinessCardV1;
import com.mindee.product.cropper.CropperV1;
import com.mindee.product.deliverynote.DeliveryNoteV1;
import com.mindee.product.driverlicense.DriverLicenseV1;
import com.mindee.product.financialdocument.FinancialDocumentV1;
import com.mindee.product.fr.bankaccountdetails.BankAccountDetailsV2;
import com.mindee.product.fr.cartegrise.CarteGriseV1;
import com.mindee.product.fr.energybill.EnergyBillV1;
import com.mindee.product.fr.healthcard.HealthCardV1;
import com.mindee.product.fr.idcard.IdCardV2;
import com.mindee.product.fr.payslip.PayslipV3;
import com.mindee.product.ind.indianpassport.IndianPassportV1;
import com.mindee.product.internationalid.InternationalIdV2;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import com.mindee.product.nutritionfactslabel.NutritionFactsLabelV1;
import com.mindee.product.passport.PassportV1;
import com.mindee.product.receipt.ReceiptV5;
import com.mindee.product.resume.ResumeV1;
import com.mindee.product.us.bankcheck.BankCheckV1;
import com.mindee.product.us.healthcarecard.HealthcareCardV1;
import com.mindee.product.us.usmail.UsMailV3;
import com.mindee.product.us.w9.W9V1;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;

/**
 * Product-wrapper class for CLI use.
 */
public class CommandLineInterfaceProducts {
  private final ProductProcessor processor;

  /**
   * @param processor Processor instance to render the products.
   */
  public CommandLineInterfaceProducts(ProductProcessor processor) {
    this.processor = processor;
  }

  @CommandLine.Command(name = "fr-bank-account-details", description = "Parse using FR Bank Account Details")
  void bankAccountDetailsV2Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(BankAccountDetailsV2.class, file));
  }

  @CommandLine.Command(name = "us-bank-check", description = "Parse using US Bank Check")
  void bankCheckV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(BankCheckV1.class, file));
  }

  @CommandLine.Command(name = "barcode-reader", description = "Parse using Barcode Reader")
  void barcodeReaderV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(BarcodeReaderV1.class, file));
  }

  @CommandLine.Command(name = "bill-of-lading", description = "Parse using Bill of Lading")
  void billOfLadingV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(BillOfLadingV1.class, file));
  }

  @CommandLine.Command(name = "business-card", description = "Parse using Business Card")
  void businessCardV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(BusinessCardV1.class, file));
  }

  @CommandLine.Command(name = "fr-carte-grise", description = "Parse using FR Carte Grise")
  void carteGriseV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(CarteGriseV1.class, file));
  }

  @CommandLine.Command(name = "cropper", description = "Parse using Cropper")
  void cropperV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(CropperV1.class, file));
  }

  @CommandLine.Command(name = "delivery-note", description = "Parse using Delivery note")
  void deliveryNoteV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(DeliveryNoteV1.class, file));
  }

  @CommandLine.Command(name = "driver-license", description = "Parse using Driver License")
  void driverLicenseV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(DriverLicenseV1.class, file));
  }

  @CommandLine.Command(name = "fr-energy-bill", description = "Parse using FR Energy Bill")
  void energyBillV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(EnergyBillV1.class, file));
  }

  @CommandLine.Command(name = "financial-document", description = "Parse using Financial Document")
  void financialDocumentV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(FinancialDocumentV1.class, file));
  }

  @CommandLine.Command(name = "fr-health-card", description = "Parse using FR Health Card")
  void healthCardV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(HealthCardV1.class, file));
  }

  @CommandLine.Command(name = "us-healthcare-card", description = "Parse using US Healthcare Card")
  void healthcareCardV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(HealthcareCardV1.class, file));
  }

  @CommandLine.Command(name = "fr-carte-nationale-d-identite", description = "Parse using FR Carte Nationale d'Identité")
  void idCardV2Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(IdCardV2.class, file));
  }

  @CommandLine.Command(name = "ind-passport-india", description = "Parse using IND Passport - India")
  void indianPassportV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(IndianPassportV1.class, file));
  }

  @CommandLine.Command(name = "international-id", description = "Parse using International ID")
  void internationalIdV2Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InternationalIdV2.class, file));
  }

  @CommandLine.Command(name = "invoice-splitter", description = "Parse using Invoice Splitter")
  void invoiceSplitterV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InvoiceSplitterV1.class, file));
  }

  @CommandLine.Command(name = "invoice", description = "Parse using Invoice")
  void invoiceV4Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(InvoiceV4.class, file));
  }

  @CommandLine.Command(name = "multi-receipts-detector", description = "Parse using Multi Receipts Detector")
  void multiReceiptsDetectorV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(MultiReceiptsDetectorV1.class, file));
  }

  @CommandLine.Command(name = "nutrition-facts-label", description = "Parse using Nutrition Facts Label")
  void nutritionFactsLabelV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(NutritionFactsLabelV1.class, file));
  }

  @CommandLine.Command(name = "passport", description = "Parse using Passport")
  void passportV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(PassportV1.class, file));
  }

  @CommandLine.Command(name = "fr-payslip", description = "Parse using FR Payslip")
  void payslipV3Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(PayslipV3.class, file));
  }

  @CommandLine.Command(name = "receipt", description = "Parse using Receipt")
  void receiptV5Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(ReceiptV5.class, file));
  }

  @CommandLine.Command(name = "resume", description = "Parse using Resume")
  void resumeV1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(ResumeV1.class, file));
  }

  @CommandLine.Command(name = "us-us-mail", description = "Parse using US US Mail")
  void usMailV3Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductAsyncOutput(UsMailV3.class, file));
  }

  @CommandLine.Command(name = "us-w9", description = "Parse using US W9")
  void w9V1Method(
      @CommandLine.Parameters(index = "0", paramLabel = "<path>", scope = CommandLine.ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(processor.standardProductOutput(W9V1.class, file));
  }
}
