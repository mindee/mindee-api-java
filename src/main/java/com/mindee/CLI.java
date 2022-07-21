package com.mindee;

import com.mindee.documentparser.Client;
import com.mindee.documentparser.ParseParameters;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.ReceiptResponse;
import java.io.File;
import java.io.IOException;
import picocli.CommandLine;
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Model.CommandSpec;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import picocli.CommandLine.ParameterException;
import picocli.CommandLine.ScopeType;
import picocli.CommandLine.Spec;
import java.util.Locale;

@Command(name = "CLI",scope = ScopeType.INHERIT,
    subcommands = { CommandLine.HelpCommand.class },
    description = "Invoke Off The Shelf API for invoice, receipt, and passports")
public class CLI {
  @Spec CommandSpec spec;

  @Parameters(index = "0", paramLabel = "<path>", scope = ScopeType.INHERIT)
  File file;

  @Option(names = { "-w", "--words" }, scope = ScopeType.INHERIT,paramLabel = "<AllWords>", description = "Flag to set all words")
  boolean words;

  @Option(names = { "-C", "--no-cut-pdf" }, scope = ScopeType.INHERIT, paramLabel = "<NoCutPDF>", description = "Flag to not cut a PDF document")
  boolean noCutPdf;
  @Option(names = {"-p","--pdf-cut-mode"}, scope = ScopeType.INHERIT,  description = "PDF cut mode {1,2,3}")
  int mode;



  @Command(name = "invoice",description = "Invokes the invoice API via SDK")
  void invoiceMethod( @Option(names = { "--invoice-key"},
      paramLabel = "INVOICE_API_KEY",
      description = "invoice api key, if not set, will use system property")
      String invoiceApiKey) throws IOException {


    Client client = new Client();
    if(invoiceApiKey!=null)
      client.configureInvoice(invoiceApiKey);

    InvoiceResponse invoiceResponse = client.loadDocument(file).parse(InvoiceResponse.class, ParseParameters.builder()
            .documentType("invoice")
            .includeWords(words)
        .build());


    System.out.println(invoiceResponse.documentSummary());

  }

  @Command(name = "receipt",description = "Invokes the receipt API via SDK")
  void receiptMethod( @Option(names = { "--receipt-key"},
      paramLabel = "RECEIPT_API_KEY",
      description = "receipt api key, if not set, will use system property")
      String receiptApiKey) throws IOException {


    Client client = new Client();
    if(receiptApiKey!=null)
      client.configureReceipt(receiptApiKey);

    ReceiptResponse receiptResponse = client.loadDocument(file).parse(ReceiptResponse.class, ParseParameters.builder()
        .documentType("receipt")
        .includeWords(words)
        .build());


    System.out.println(receiptResponse.documentSummary());

  }

  @Command(name = "passport",description = "Invokes the passport API via SDK")
  void passportMethod( @Option(names = { "--passport-key"},
      paramLabel = "PASSPORT_API_KEY",
      description = "passport api key, if not set, will use system property")
      String passportApiKey) throws IOException {


    Client client = new Client();
    if(passportApiKey!=null)
      client.configurePassport(passportApiKey);

    PassportResponse passportResponse = client.loadDocument(file).parse(PassportResponse.class, ParseParameters.builder()
        .documentType("passport")
        .includeWords(words)
        .build());


    System.out.println(passportResponse.documentSummary());

  }

  public static void main(String[] args) {
    int exitCode = new CommandLine(new CLI()).execute(args);
    System.exit(exitCode);
  }
}

