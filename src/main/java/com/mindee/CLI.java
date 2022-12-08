package com.mindee;

import com.mindee.documentparser.Client;
import com.mindee.documentparser.ParseParameters;
import com.mindee.model.documenttype.PassportV1Response;
import com.mindee.model.documenttype.ReceiptV4Response;
import com.mindee.model.documenttype.invoice.InvoiceV4Response;

import java.io.File;
import java.io.IOException;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;

@Command(name = "CLI", scope = ScopeType.INHERIT, subcommands = {
    CommandLine.HelpCommand.class }, description = "Invoke Off The Shelf API for invoice, receipt, and passports")
public class CLI {

  @Spec
  CommandSpec spec;

  @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.INHERIT)
  File file;

  @Option(names = { "-w",
      "--words" }, scope = ScopeType.INHERIT, paramLabel = "<AllWords>", description = "Flag to set all words")
  boolean words;

  @Option(names = { "-C",
      "--no-cut-doc" }, scope = ScopeType.INHERIT, paramLabel = "<NoCutDoc>", description = "Flag to not cut a document")
  boolean noCutDoc;

  @Option(names = { "-p",
      "--doc-pages" }, scope = ScopeType.INHERIT, description = "Number of document pages to cut by")
  int cutPages;

  @Option(names = { "-k",
      "--api-key" }, scope = ScopeType.INHERIT, paramLabel = "MINDEE_API_KEY", description = "API key, if not set, will use system property")
  String apiKey;

  public static void main(String[] args) {
    int exitCode = new CommandLine(new CLI()).execute(args);
    System.exit(exitCode);
  }

  @Command(name = "invoice", description = "Invokes the invoice API")
  void invoiceMethod() throws IOException {
    Client client = initClient();

    InvoiceV4Response invoiceResponse = client.loadDocument(file)
        .parse(InvoiceV4Response.class, ParseParameters.builder()
            .documentType("invoice")
            .includeWords(words)
            .build());

    System.out.println(invoiceResponse.documentSummary());
  }

  @Command(name = "receipt", description = "Invokes the receipt API")
  void receiptMethod() throws IOException {
    Client client = initClient();

    ReceiptV4Response receiptResponse = client.loadDocument(file)
        .parse(ReceiptV4Response.class, ParseParameters.builder()
            .documentType("receipt")
            .includeWords(words)
            .build());

    System.out.println(receiptResponse.documentSummary());
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
