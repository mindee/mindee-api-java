package com.mindee;

import com.mindee.cli.CommandLineInterfaceProducts;
import com.mindee.cli.ProductProcessor;
import com.mindee.http.Endpoint;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.common.ocr.Ocr;
import com.mindee.product.custom.CustomV1;
import com.mindee.product.generated.GeneratedV1;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
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
    description = "Invoke Off The Shelf API for invoice, receipt, and passports",
    subcommands = { CommandLine.HelpCommand.class },
    mixinStandardHelpOptions = true
)
public class CommandLineInterface implements ProductProcessor {

  @Spec
  CommandSpec spec;

  @Option(
      names = { "-w", "--all-words" },
      scope = ScopeType.INHERIT,
      paramLabel = "WORDS",
      description = "Include all document words in the response"
  )
  private boolean words;

  @Option(
      names = { "-f", "--full-text" },
      scope = ScopeType.INHERIT,
      paramLabel = "FULL_TEXT",
      description = "Include full text response, if available"
  )
  private boolean fullText;

  private enum OutputChoices {
    summary,
    full,
    raw
  }

  @Option(
      names = { "-o", "--output-type" },
      scope = ScopeType.INHERIT,
      paramLabel = "OUTPUT_TYPE",
      description = "Output type, one of:\n"
        + "  summary - document predictions\n"
        + "  full - all predictions\n"
        + "  raw - raw response from the server",
      defaultValue = "summary"
  )
  private OutputChoices outputType;

  @Option(
      names = { "-k", "--api-key" },
      scope = ScopeType.INHERIT,
      paramLabel = "MINDEE_API_KEY",
      description = "API key, if not set, will use system property"
  )
  private String apiKey;

  @Option(
      names = { "-c", "--cut-doc" },
      scope = ScopeType.INHERIT,
      paramLabel = "<CutDoc>",
      description = "Keep only the first 5 pages of the document"
  )
  private boolean cutDoc;

  /**
   * Instantiates all products one by one in a separate picocli instance and relays the command to
   * them.
   *
   * @param args CLI args.
   */
  public static void main(String[] args) {
    CommandLineInterface cli = new CommandLineInterface();
    CommandLine commandLine = new CommandLine(cli);
    CommandLineInterfaceProducts products = new CommandLineInterfaceProducts(cli);

    for (Method method : CommandLineInterfaceProducts.class.getDeclaredMethods()) {
      if (method.isAnnotationPresent(CommandLine.Command.class)) {
        CommandLine.Command annotation = method.getAnnotation(CommandLine.Command.class);
        String subcommandName = annotation.name();
        CommandLine subCmd = new CommandLine(new ProductCommandHandler(products, method));
        subCmd.getCommandSpec().usageMessage().description(annotation.description());
        commandLine.addSubcommand(subcommandName, subCmd);
      }
    }

    int exitCode = commandLine.execute(args);
    System.exit(exitCode);
  }

  /**
   * Adds all commands from CommandLineInterfaceProducts automatically.
   */
  @CommandLine.Command(
      mixinStandardHelpOptions = true,
      description = "Auto-generated product command"
  )
  public static class ProductCommandHandler implements Callable<Integer> {
    private final CommandLineInterfaceProducts products;
    private final Method method;

    @CommandLine.Parameters(index = "0", paramLabel = "<path>")
    private File file;

    /**
     * Reads all products from the CommandLineInterfaceProducts file and makes them accessible.
     *
     * @param products List of all products to add.
     * @param method Handler of each product.
     */
    public ProductCommandHandler(CommandLineInterfaceProducts products, Method method) {
      this.products = products;
      this.method = method;
      this.method.setAccessible(true);
    }

    @Override
    public Integer call() throws Exception {
      method.invoke(products, file);
      return 0;
    }
  }

  @Command(
      name = "custom",
      description = "Invokes a Custom API (API Builder only, use 'generated' for regular custom APIs)"
  )
  void customMethod(
      @Option(
          names = { "-a", "--account" },
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "accountName",
          description = "The name of the account"
      ) String accountName,
      @Option(
          names = { "-e", "--endpointName" },
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "endpointName",
          description = "The name of the endpoint"
      ) String endpointName,
      @Parameters(index = "0", scope = ScopeType.LOCAL, paramLabel = "<path>") File file
  ) throws IOException {

    MindeeClient mindeeClient = new MindeeClient(apiKey);

    PredictResponse<CustomV1> document;
    Endpoint endpoint = new Endpoint(endpointName, accountName, "1");

    if (cutDoc) {
      document = mindeeClient.parse(new LocalInputSource(file), endpoint, getDefaultPageOptions());
    } else {
      document = mindeeClient.parse(new LocalInputSource(file), endpoint);
    }
    System.out.println(document.toString());
  }

  @Command(name = "generated", description = "Invokes a Generated API")
  void generatedMethod(
      @Option(
          names = { "-a", "--account" },
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "accountName",
          description = "The name of the account"
      ) String accountName,
      @Option(
          names = { "-e", "--endpointName" },
          scope = ScopeType.LOCAL,
          required = true,
          paramLabel = "endpointName",
          description = "The name of the endpoint"
      ) String endpointName,
      @Option(
          names = { "-v", "--productVersion" },
          scope = ScopeType.LOCAL,
          paramLabel = "productVersion",
          description = "The version of the endpoint",
          defaultValue = "1"
      ) String productVersion,
      @Option(
          names = { "-m", "--parsingMode" },
          description = "Whether to parse the document in synchronous mode or polling.",
          scope = ScopeType.LOCAL,
          defaultValue = "async"
      ) String parsingMode,
      @Parameters(index = "0", scope = ScopeType.LOCAL, paramLabel = "<path>") File file
  ) throws IOException, InterruptedException {

    MindeeClient mindeeClient = new MindeeClient(apiKey);

    Endpoint endpoint = new Endpoint(endpointName, accountName, productVersion);

    if (Objects.equals(parsingMode, "sync")) {
      if (cutDoc) {
        PredictResponse<GeneratedV1> document = mindeeClient
          .parse(GeneratedV1.class, endpoint, new LocalInputSource(file), getDefaultPageOptions());
        System.out.println(document.toString());
      } else {
        PredictResponse<GeneratedV1> document = mindeeClient
          .parse(GeneratedV1.class, endpoint, new LocalInputSource(file));
        System.out.println(document.toString());
      }
    } else if (Objects.equals(parsingMode, "async")) {
      if (cutDoc) {
        AsyncPredictResponse<GeneratedV1> document = mindeeClient
          .enqueueAndParse(
            GeneratedV1.class,
            endpoint,
            new LocalInputSource(file),
            PredictOptions.builder().build(),
            getDefaultPageOptions(),
            AsyncPollingOptions.builder().build()
          );
        System.out.println(document.toString());
      } else {
        AsyncPredictResponse<GeneratedV1> document = mindeeClient
          .enqueueAndParse(GeneratedV1.class, endpoint, new LocalInputSource(file));
        System.out.println(document.toString());
      }
    } else {
      throw new MindeeException("Invalid parsing mode: " + parsingMode);
    }
  }

  protected PageOptions getDefaultPageOptions() {
    return new PageOptions.Builder()
      .pageIndexes(new Integer[] { 0, 1, 2, 3, 4 })
      .operation(PageOptionsOperation.KEEP_ONLY)
      .build();
  }

  private String wordsOutput(Ocr ocr) {
    StringBuilder output = new StringBuilder();
    output.append("\n#############\nDocument Text\n#############\n::\n  ");
    output
      .append(
        Arrays
          .stream(ocr.toString().split(String.format("%n")))
          .collect(Collectors.joining(String.format("%n  ")))
      );
    output.append("\n");
    return output.toString();
  }

  @Override
  public <T extends Inference<?, ?>> String standardProductOutput(
      Class<T> productClass,
      File file
  ) throws IOException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    PredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions
      .builder()
      .allWords(words)
      .fullText(fullText)
      .build();
    if (cutDoc) {
      response = mindeeClient
        .parse(productClass, inputSource, predictOptions, getDefaultPageOptions());
    } else {
      response = mindeeClient.parse(productClass, inputSource, predictOptions);
    }

    StringBuilder output = new StringBuilder();
    switch (outputType) {
      case full:
        output.append(response.getDocument().toString());
        break;
      case raw:
        output.append(response.getRawResponse());
        break;
      default:
        output.append(response.getDocument().getInference().getPrediction().toString());
    }
    if (words) {
      output.append(wordsOutput(response.getDocument().getOcr()));
    }
    return output.toString();
  }

  @Override
  public <T extends Inference<?, ?>> String standardProductAsyncOutput(
      Class<T> productClass,
      File file
  ) throws IOException, InterruptedException {
    MindeeClient mindeeClient = new MindeeClient(apiKey);
    LocalInputSource inputSource = new LocalInputSource(file);
    AsyncPredictResponse<T> response;
    PredictOptions predictOptions = PredictOptions
      .builder()
      .allWords(words)
      .fullText(fullText)
      .build();
    if (cutDoc) {
      response = mindeeClient
        .enqueueAndParse(productClass, inputSource, predictOptions, getDefaultPageOptions(), null);
    } else {
      response = mindeeClient
        .enqueueAndParse(productClass, inputSource, predictOptions, null, null);
    }

    StringBuilder output = new StringBuilder();
    switch (outputType) {
      case full:
        output.append(response.getDocument().toString());
        break;
      case raw:
        output.append(response.getRawResponse());
        break;
      default:
        output.append(response.getDocumentObj().getInference().getPrediction().toString());
    }
    if (words) {
      output.append(wordsOutput(response.getDocumentObj().getOcr()));
    }
    return output.toString();
  }
}
