package com.mindee;

import com.mindee.http.Endpoint;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.custom.CustomV1;
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
import java.util.ArrayList;
import java.util.List;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;

/**
 * Command Line Interface for the Mindee client.
 */
@Command(
    name = "CLI",
    scope = ScopeType.INHERIT,
    subcommands = {CommandLine.HelpCommand.class},
    description = "Invoke Off The Shelf API for invoice, receipt, and passports"
)
public class CommandLineInterface {

  @Spec
  CommandSpec spec;

  @Option(
      names = {"-w", "--all-words"},
      scope = ScopeType.INHERIT,
      paramLabel = "WORDS",
      description = "Include all document words in the response"
  )
  private boolean words;

  @Option(
      names = {"-f", "--full-text"},
      scope = ScopeType.INHERIT,
      paramLabel = "FULL_TEXT",
      description = "Include full text response, if available"
  )
  private boolean fullText;

  private enum OutputChoices { summary, full, raw }

  @Option(
      names = {"-o", "--output-type"},
      scope = ScopeType.INHERIT,
      paramLabel = "OUTPUT_TYPE",
      description = "Output type, one of:\n"
          + "  summary - document predictions\n"
          + "  full - all predictions\n"
          + "  raw - raw response from the server"
  )
  private OutputChoices outputType;

  @Option(
      names = {"-k", "--api-key"},
      scope = ScopeType.INHERIT,
      paramLabel = "MINDEE_API_KEY",
      description = "API key, if not set, will use system property")
  private String apiKey;

  @Option(
      names = {"-c", "--cut-doc"},
      scope = ScopeType.INHERIT,
      paramLabel = "<CutDoc>",
      description = "Keep only the first 5 pages of the document")
  private boolean cutDoc;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new CommandLineInterface()).execute(args);
    System.exit(exitCode);
  }

  @Command(name = "invoice", description = "Parse using Invoice")
  void invoiceMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(InvoiceV4.class, file));
  }

  @Command(name = "receipt", description = "Parse using Expense Receipt")
  void receiptMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(ReceiptV4.class, file));
  }

  @Command(name = "financial-document", description = "Parse using Financial Document")
  void financialDocumentMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(FinancialDocumentV1.class, file));
  }

  @Command(name = "multi-receipt-detector", description = "Parse using Multi Receipts Detector")
  void multiReceiptDetectorMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(MultiReceiptsDetectorV1.class, file));
  }

  @Command(name = "passport", description = "Parse using Passport")
  void passportMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(PassportV1.class, file));
  }

  @Command(name = "invoice-splitter", description = "Parse using Invoice Splitter")
  void invoiceSplitterMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(standardProductAsyncOutput(InvoiceSplitterV1.class, file));
  }

  @Command(name = "international-id", description = "Parse using International ID")
  void internationalIdMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(standardProductAsyncOutput(InternationalIdV2.class, file));
  }

  @Command(name = "us-mail", description = "Parse using US Mail")
  void usMailMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException, InterruptedException {
    System.out.println(standardProductAsyncOutput(UsMailV3.class, file));
  }

  @Command(name = "custom", description = "Invokes a Custom API")
  void customMethod(
      @Option(
          names = {"-a", "--account"},
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "accountName",
          description = "The name of the account"
      )
      String accountName,
      @Option(
        names = {"-e", "--endpointName"},
        scope = ScopeType.LOCAL,
        required = true,
        paramLabel = "endpointName",
        description = "The name of the endpoint"
      )
      String endpointName,
      @Parameters(index = "0", scope = ScopeType.LOCAL, paramLabel = "<path>")
      File file
  ) throws IOException {

    MindeeClient mindeeClient = new MindeeClient(apiKey);

    PredictResponse<CustomV1> document;
    Endpoint endpoint = new Endpoint(endpointName, accountName, "1");

    if (cutDoc) {
      document = mindeeClient.parse(
        new LocalInputSource(file),
        endpoint,
        getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(
        new LocalInputSource(file),
        endpoint);
    }
    System.out.println(document.toString());
  }

  protected PageOptions getDefaultPageOptions() {

    List<Integer> pageNumbers = new ArrayList<>();
    pageNumbers.add(0);
    pageNumbers.add(1);
    pageNumbers.add(2);
    pageNumbers.add(3);
    pageNumbers.add(4);

    return new PageOptions(pageNumbers, PageOptionsOperation.KEEP_ONLY);
  }

  private <T extends Inference<?, ?>> String standardProductOutput(
      Class<T> docClass,
      File file
  ) throws IOException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    PredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions.builder().allWords(words).fullText(fullText).build();
    if (cutDoc) {
      response = mindeeClient.parse(docClass, inputSource, predictOptions, getDefaultPageOptions());
    } else {
      response = mindeeClient.parse(docClass, inputSource, predictOptions);
    }

    if (outputType == OutputChoices.full) {
      return response.getDocument().toString();
    }
    if (outputType == OutputChoices.raw) {
      return response.getRawResponse();
    }
    Document<T> document = response.getDocument();
    return document.getInference().getPrediction().toString();
  }

  private <T extends Inference<?, ?>> String standardProductAsyncOutput(
      Class<T> docClass,
      File file
  ) throws IOException, InterruptedException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    AsyncPredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions.builder().allWords(words).fullText(fullText).build();
    if (cutDoc) {
      response = mindeeClient.enqueueAndParse(
        docClass, inputSource, predictOptions, getDefaultPageOptions(), null
      );
    } else {
      response = mindeeClient.enqueueAndParse(
        docClass, inputSource, predictOptions, null, null
      );
    }

    if (outputType == OutputChoices.full) {
      return response.getDocumentObj().toString();
    }
    if (outputType == OutputChoices.raw) {
      return response.getRawResponse();
    }
    Document<T> document = response.getDocumentObj();
    return document.getInference().getPrediction().toString();
  }
}
