package com.mindee.v2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import java.io.File;
import java.util.concurrent.Callable;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Abstract base class for V2 inference CLI commands.
 * Handles common options (path, model-id, api-key, alias, output) and output formatting.
 */
public abstract class BaseInferenceCommand implements Callable<Integer> {

  @Parameters(index = "0", paramLabel = "<path>", description = "The path of the file to parse")
  protected File file;

  @Option(names = { "-m", "--model-id" }, description = "ID of the model to use", required = true)
  protected String modelId;

  @Option(names = { "-k", "--api-key" }, description = "Mindee V2 API key.")
  protected String apiKey;

  @Option(names = { "-a", "--alias" }, description = "Alias for the file")
  protected String alias;

  /** Output format for the command. */
  public enum OutputType {
    summary,
    full,
    raw
  }

  @Option(
      names = { "-o", "--output" },
      description = "Specify how to output the data.\n"
        + "- summary: a basic summary (default)\n"
        + "- full: detail extraction results, including options\n"
        + "- raw: full JSON object",
      defaultValue = "summary"
  )
  protected OutputType output;

  /**
   * Executes the inference request and returns the product response.
   *
   * @param client the V2 Mindee client
   * @param inputSource the prepared local input source
   * @return the product response
   * @throws Exception on IO or API error
   */
  protected abstract CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception;

  /**
   * Returns the summary (result-only) string for the given response.
   * Override in each product command.
   *
   * @param response the product response
   * @return the summary string
   */
  protected abstract String getSummary(CommonResponse response);

  /**
   * Returns the full (inference + options + result) string for the given response.
   * Defaults to the same as {@link #getSummary}; override for richer output.
   *
   * @param response the product response
   * @return the full output string
   */
  protected String getFullOutput(CommonResponse response) {
    return getSummary(response);
  }

  @Override
  public Integer call() throws Exception {
    var client = new MindeeClient(apiKey != null ? apiKey : "");
    var inputSource = new LocalInputSource(file);
    var response = executeRequest(client, inputSource);
    printOutput(response);
    return 0;
  }

  private void printOutput(CommonResponse response) throws Exception {
    switch (output) {
      case full:
        System.out.println(getFullOutput(response));
        break;
      case raw:
        var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        var jsonNode = mapper.readTree(response.getRawResponse());
        System.out.println(mapper.writeValueAsString(jsonNode));
        break;
      default:
        System.out.println(getSummary(response));
        break;
    }
  }
}
