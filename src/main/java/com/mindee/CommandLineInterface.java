package com.mindee;

import com.mindee.parsing.CustomEndpoint;
import com.mindee.parsing.PageOptions;
import com.mindee.parsing.PageOptionsOperation;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.custom.CustomV1Inference;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.parsing.passport.PassportV1Inference;
import com.mindee.parsing.receipt.ReceiptV4Inference;
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
      names = {"-w", "--words"},
      scope = ScopeType.INHERIT,
      paramLabel = "WORDS",
      description = "Include words in response"
  )
  private boolean words;

  private enum OutputChoices { summary, full }

  @Option(
      names = {"-o", "--output-type"},
      scope = ScopeType.INHERIT,
      paramLabel = "OUTPUT_TYPE",
      description = "Output type, one of:\n"
          + "  summary - document level predictions\n"
          + "  full - all parsed data"
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

  @Command(name = "invoice", description = "Invokes the invoice API")
  void invoiceMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(InvoiceV4Inference.class, file));
  }

  @Command(name = "receipt", description = "Invokes the receipt API")
  void receiptMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(ReceiptV4Inference.class, file));
  }

  @Command(name = "passport", description = "Invokes the passport API")
  void passportMethod(
      @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.LOCAL)
      File file
  ) throws IOException {
    System.out.println(standardProductOutput(PassportV1Inference.class, file));
  }

  @Command(name = "custom", description = "Invokes a builder API")
  void customMethod(
      @Option(
          names = {"-a", "--account"},
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "accountName",
          description = "The name of the account"
      )
      String accountName,
      @Parameters(index = "0", scope = ScopeType.LOCAL, paramLabel = "<productName>")
      String productName,
      @Parameters(index = "1", scope = ScopeType.LOCAL, paramLabel = "<path>")
      File file
  ) throws IOException {

    MindeeClient mindeeClient = MindeeClientInit.create(apiKey);

    Document<CustomV1Inference> document;
    CustomEndpoint customEndpoint = new CustomEndpoint(productName, accountName, "1");

    if (cutDoc) {
      document = mindeeClient.parse(
        new DocumentToParse(file),
        customEndpoint,
        getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(
        new DocumentToParse(file),
        customEndpoint);
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

    return new PageOptions(pageNumbers, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES);
  }

  private <T extends Inference<?, ?>> String standardProductOutput(
      Class<T> docClass,
      File file
  ) throws IOException {
    MindeeClient mindeeClient = MindeeClientInit.create(apiKey);
    DocumentToParse input = new DocumentToParse(file);
    Document<T> response;
    if (cutDoc) {
      response = mindeeClient.parse(docClass, input, words, getDefaultPageOptions());
    } else {
      response = mindeeClient.parse(docClass, input, words);
    }

    if (outputType == OutputChoices.full) {
      return response.toString();
    }
    return response.getInference().getDocumentPrediction().toString();
  }
}
