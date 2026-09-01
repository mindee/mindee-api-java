package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import java.io.File;
import java.util.List;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

/**
 * Abstract base class for V2 inference CLI commands.
 * Handles common options (path, model-id, api-key, alias, output) and output formatting.
 */
public abstract class BaseInferenceCommand extends BaseCommand {

  @Parameters(index = "0", paramLabel = "<path>", description = "The path of the file to parse")
  protected File file;

  @Option(names = { "-m", "--model-id" }, description = "ID of the model to use", required = true)
  protected String modelId;

  @Option(names = { "-a", "--alias" }, description = "Alias for the file")
  protected String alias;

  @Option(
      names = { "-w", "--webhook-id" },
      description = "Specify a webhook by ID. May be used multiple times."
  )
  private List<String> webhookIds;

  /**
   * @return The properly formatted webhook IDs.
   */
  protected String[] getWebhookIds() {
    return (webhookIds != null ? webhookIds.toArray(new String[0]) : new String[] {});
  }

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

  @Override
  public Integer call() throws Exception {
    var client = new MindeeClient(String.valueOf(apiKey));
    var inputSource = new LocalInputSource(file);
    var response = executeRequest(client, inputSource);
    printOutput(response);
    return 0;
  }
}
