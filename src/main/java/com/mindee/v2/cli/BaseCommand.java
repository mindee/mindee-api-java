package com.mindee.v2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mindee.v2.parsing.CommonResponse;
import java.util.concurrent.Callable;
import picocli.CommandLine;

/**
 * Abstract base class for V2 inference CLI commands.
 * Handles common options (path, model-id, api-key, alias, output) and output formatting.
 */
public abstract class BaseCommand implements Callable<Integer> {
  @CommandLine.Option(names = { "-k", "--api-key" }, description = "Mindee V2 API key.")
  protected String apiKey;

  /** Output format for the command. */
  public enum OutputType {
    summary,
    full,
    raw
  }

  @CommandLine.Option(
      names = { "-o", "--output" },
      description = "Specify how to output the data.\n"
        + "- summary: a basic summary (default)\n"
        + "- full: detail extraction results, including options\n"
        + "- raw: full JSON object",
      defaultValue = "summary"
  )
  protected OutputType output;

  /**
   * Returns the summary string for the given response.
   * Override in each command.
   *
   * @param response the response
   * @return the summary string
   */
  protected abstract String getSummary(CommonResponse response);

  /**
   * Returns the full string for the given response.
   *
   * @param response the product response
   * @return the full output string
   */
  protected abstract String getFullOutput(CommonResponse response);

  /**
   * Prints the output to the console, taking into account the output type.
   */
  protected void printOutput(CommonResponse response) throws Exception {
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
