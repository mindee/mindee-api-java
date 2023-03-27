package com.mindee;

import com.mindee.parsing.CustomEndpoint;
import com.mindee.parsing.PageOptions;
import com.mindee.parsing.PageOptionsOperation;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.custom.CustomV1Inference;
import com.mindee.parsing.invoice.InvoiceV4Inference;
import com.mindee.parsing.passport.PassportV1Inference;
import com.mindee.parsing.receipt.ReceiptV4Inference;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Command(name = "CLI",
  scope = ScopeType.INHERIT,
  subcommands = {CommandLine.HelpCommand.class},
  description = "Invoke Off The Shelf API for invoice, receipt, and passports")
public class CLI {

  @Spec
  CommandSpec spec;

  @Parameters(index = "0",
    paramLabel = "<path>",
    scope = ScopeType.INHERIT)
  File file;

  @Option(names = {"-w",
    "--words"},
    scope = ScopeType.INHERIT,
    paramLabel = "<AllWords>",
    description = "Flag to set all words")
  boolean words;

  @Option(names = {"-k",
    "--api-key"},
    scope = ScopeType.INHERIT,
    paramLabel = "MINDEE_API_KEY",
    description = "API key, if not set, will use system property")
  String apiKey;

  @Option(names = {"-c",
    "--cut-doc"},
    scope = ScopeType.INHERIT,
    paramLabel = "<CutDoc>",
    description = "Keep only the first 5 pages of the document")
  boolean cutDoc;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new CLI()).execute(args);
    System.exit(exitCode);
  }

  @Command(name = "invoice", description = "Invokes the invoice API")
  void invoiceMethod() throws IOException {

    MindeeClient mindeeClient = MindeeClientInit.create(apiKey);

    Document<InvoiceV4Inference> document;

    if (cutDoc) {
      document = mindeeClient.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(file),
        words,
        getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(
        InvoiceV4Inference.class,
        new DocumentToParse(file),
        words);
    }

    System.out.println(document.toString());
  }

  @Command(name = "receipt", description = "Invokes the receipt API")
  void receiptMethod() throws IOException {

    MindeeClient mindeeClient = MindeeClientInit.create(apiKey);

    Document<ReceiptV4Inference> document;

    if (cutDoc) {
      document = mindeeClient.parse(
        ReceiptV4Inference.class,
        new DocumentToParse(file),
        words,
        getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(
        ReceiptV4Inference.class,
        new DocumentToParse(file),
        words);
    }

    System.out.println(document.toString());
  }

  @Command(name = "passport", description = "Invokes the passport API")
  void passportMethod() throws IOException {

    MindeeClient mindeeClient = MindeeClientInit.create(apiKey);

    Document<PassportV1Inference> document;

    if (cutDoc) {
      document = mindeeClient.parse(
        PassportV1Inference.class,
        new DocumentToParse(file),
        words,
        getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(
        PassportV1Inference.class,
        new DocumentToParse(file),
        words);
    }

    System.out.println(document.toString());
  }

  @Command(name = "custom", description = "Invokes a builder API")
  void customMethod(
    @Parameters(
      index = "0",
      scope = ScopeType.INHERIT,
      paramLabel = "<path>") File file,
    @Parameters(
      index = "1",
      scope = ScopeType.LOCAL,
      paramLabel = "<productName>") String productName,
    @Option(names = {"-an",
      "--account-name"},
      scope = ScopeType.LOCAL,
      required = true,
      paramLabel = "accountName",
      description = "The name of the account") String accountName
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

  PageOptions getDefaultPageOptions() {

    List<Integer> pageNumbers = new ArrayList<>();
    pageNumbers.add(0);
    pageNumbers.add(1);
    pageNumbers.add(2);
    pageNumbers.add(3);
    pageNumbers.add(4);

    return new PageOptions(pageNumbers, PageOptionsOperation.KEEP_ONLY_LISTED_PAGES);
  }
}
