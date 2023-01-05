package com.mindee;

import com.mindee.documentparser.Client;
import com.mindee.documentparser.ParseParameters;
import com.mindee.model.documenttype.PassportV1Response;
import com.mindee.http.MindeeHttpApi;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.invoice.InvoiceV4Inference;
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

  @Option(names = {"-C",
    "--no-cut-doc"},
    scope = ScopeType.INHERIT,
    paramLabel = "<NoCutDoc>",
    description = "Flag to not cut a document")
  boolean noCutDoc;

  @Option(names = {"-p",
    "--doc-pages"},
    scope = ScopeType.INHERIT,
    description = "Number of document pages to cut by")
  int cutPages;

  @Option(names = {"-k",
    "--api-key"},
    scope = ScopeType.INHERIT,
    paramLabel = "MINDEE_API_KEY",
    description = "API key, if not set, will use system property")
  String apiKey;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new CLI()).execute(args);
    System.exit(exitCode);
  }

  @Command(name = "invoice", description = "Invokes the invoice API")
  void invoiceMethod() throws IOException {
    MindeeSettings mindeeSettings;
    if (apiKey != null && !apiKey.trim().isEmpty()) {
      mindeeSettings = new MindeeSettings(apiKey);
    } else {
      mindeeSettings = new MindeeSettings();
    }

    MindeeClient mindeeClient = getMindeeClient(mindeeSettings);

    Document<InvoiceV4Inference> document = mindeeClient.parse(
      InvoiceV4Inference.class,
      new DocumentToParse(file),
      words);

    System.out.println(document.toString());
  }

  @Command(name = "receipt", description = "Invokes the receipt API")
  void receiptMethod() throws IOException {
    MindeeSettings mindeeSettings;
    if (apiKey != null && !apiKey.trim().isEmpty()) {
      mindeeSettings = new MindeeSettings(apiKey);
    } else {
      mindeeSettings = new MindeeSettings();
    }

    MindeeClient mindeeClient = getMindeeClient(mindeeSettings);

    Document<ReceiptV4Inference> document = mindeeClient.parse(
      ReceiptV4Inference.class,
      new DocumentToParse(file),
      words);

    System.out.println(document.toString());
  }

  @Command(name = "passport", description = "Invokes the passport API")
  void passportMethod() throws IOException {
    Client client = initClient();

    PassportV1Response passportV1Response = client.loadDocument(file)
      .parse(PassportV1Response.class, ParseParameters.builder()
        .documentType("passport")
        .includeWords(words)
        .build());

    System.out.println(passportV1Response.documentSummary());
  }

  MindeeClient getMindeeClient(MindeeSettings mindeeSettings) {
    return new MindeeClient(new MindeeHttpApi(mindeeSettings));
  }


  Client initClient() {
    Client client = null;
    if (apiKey != null) {
      client = new Client(apiKey);
    } else {
      client = new Client();
    }
    return client;
  }
}
