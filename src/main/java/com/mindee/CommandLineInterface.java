package com.mindee;

import com.mindee.cli.CommandLineInterfaceProducts;
import com.mindee.cli.ProductProcessor;
import com.mindee.http.Endpoint;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.custom.CustomV1;
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
public class CommandLineInterface implements ProductProcessor {

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

  /**
   * @param args CLI args.
   */
  public static void main(String[] args) {
    CommandLineInterface cli = new CommandLineInterface();
    CommandLine commandLine = new CommandLine(cli);
    cli.setupCommands(commandLine);

    int exitCode = new CommandLine(new CommandLineInterface()).execute(args);
    System.exit(exitCode);
  }

  /**
   * @param commandLine Product to add to the CLI.
   */
  public void setupCommands(CommandLine commandLine) {
    CommandLineInterfaceProducts productCommands = new CommandLineInterfaceProducts(this);
    commandLine.addSubcommand(productCommands);
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

  @Override
  public <T extends Inference<?, ?>> String standardProductOutput(Class<T> productClass, File file) throws IOException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    PredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions.builder().allWords(words).fullText(fullText).build();
    if (cutDoc) {
      response = mindeeClient.parse(productClass, inputSource, predictOptions, getDefaultPageOptions());
    } else {
      response = mindeeClient.parse(productClass, inputSource, predictOptions);
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

  @Override
  public <T extends Inference<?, ?>> String standardProductAsyncOutput(Class<T> productClass, File file) throws IOException, InterruptedException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    AsyncPredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions.builder().allWords(words).fullText(fullText).build();
    if (cutDoc) {
      response = mindeeClient.enqueueAndParse(
        productClass, inputSource, predictOptions, getDefaultPageOptions(), null
      );
    } else {
      response = mindeeClient.enqueueAndParse(
        productClass, inputSource, predictOptions, null, null
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
