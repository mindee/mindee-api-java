package com.mindee.v2.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mindee.v2.MindeeClient;
import java.util.concurrent.Callable;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * CLI command for searching available V2 models.
 */
@Command(
    name = "search-models",
    description = "Search available models.",
    mixinStandardHelpOptions = true
)
public class SearchModelsCommand implements Callable<Integer> {

  @Option(names = { "-k", "--api-key" }, description = "Mindee V2 API key.")
  private String apiKey;

  @Option(
      names = { "-n", "--name" },
      description = "Filter by model name partial match (case insensitive)."
  )
  private String name;

  @Option(
      names = { "-m", "--model-type" },
      description = "Filter by exact model type (case sensitive)."
  )
  private String modelType;

  @Option(
      names = { "-r", "--raw-json" },
      description = "Whether to output the raw JSON response.",
      defaultValue = "false"
  )
  private boolean rawJson;

  @Override
  public Integer call() throws Exception {
    var client = new MindeeClient(apiKey != null ? apiKey : "");
    var response = client.searchModels(name, modelType);
    if (rawJson) {
      var mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
      var jsonNode = mapper.readTree(response.getRawResponse());
      System.out.println(mapper.writeValueAsString(jsonNode));
    } else {
      System.out.println(response);
    }
    return 0;
  }
}
