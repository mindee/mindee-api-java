package com.mindee;

import com.mindee.documentparser.Client;
import com.mindee.documentparser.ParseParameters;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.ReceiptResponse;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;

@Command(name = "CLI", scope = ScopeType.INHERIT,
  subcommands = {CommandLine.HelpCommand.class},
  description = "Invoke Off The Shelf API for invoice, receipt, and passports")
public class CLI {

  @Spec
  CommandSpec spec;

  @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.INHERIT)
  File file;

  @Option(
          names = {"-w", "--words"},
          scope = ScopeType.INHERIT,
          paramLabel = "<AllWords>",
          description = "Flag to set all words")
  boolean words;

  @Option(
          names = {"-C", "--no-cut-doc"},
          scope = ScopeType.INHERIT,
          paramLabel = "<NoCutDoc>",
          description = "Flag to not cut a document")
  boolean noCutDoc;

  @Option(
          names = {"-p", "--doc-pages"},
          scope = ScopeType.INHERIT,
          description = "Number of document pages to cut by")
  int cutPages;

  @Option(
          names = {"-k", "--api-key"},
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
    Client client = initClient();

    InvoiceResponse invoiceResponse = client.loadDocument(file)
      .parse(InvoiceResponse.class, ParseParameters.builder()
        .documentType("invoice")
        .includeWords(words)
        .build());

    System.out.println(invoiceResponse.documentSummary());
  }

  @Command(name = "receipt", description = "Invokes the receipt API")
  void receiptMethod() throws IOException {
    Client client = initClient();

    ReceiptResponse receiptResponse = client.loadDocument(file)
      .parse(ReceiptResponse.class, ParseParameters.builder()
        .documentType("receipt")
        .includeWords(words)
        .build());

    System.out.println(receiptResponse.documentSummary());
  }

  @Command(name = "passport", description = "Invokes the passport API")
  void passportMethod() throws IOException {
    Client client = initClient();

    PassportResponse passportResponse = client.loadDocument(file)
      .parse(PassportResponse.class, ParseParameters.builder()
        .documentType("passport")
        .includeWords(words)
        .build());

    System.out.println(passportResponse.documentSummary());
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
