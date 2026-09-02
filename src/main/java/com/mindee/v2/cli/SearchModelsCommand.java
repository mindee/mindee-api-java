package com.mindee.v2.cli;

import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.search.models.ModelSearchParameters;
import com.mindee.v2.search.models.ModelSearchResponse;
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
public class SearchModelsCommand extends BaseCommand {

  @Option(
      names = { "-n", "--name" },
      description = "Filter by model name partial match (case insensitive)."
  )
  private String name;

  public enum ModelType {
    extraction,
    crop,
    classification,
    ocr,
    split
  }

  @Option(
      names = { "-m", "--model-type" },
      description = "Filter by exact model type.%nAvailable options: ${COMPLETION-CANDIDATES}"
  )
  private ModelType modelType;

  @Override
  public Integer call() throws Exception {
    var client = new MindeeClient(apiKey);
    var response = client
      .search(
        ModelSearchResponse.class,
        ModelSearchParameters
          .builder()
          .name(name)
          .modelType(modelType != null ? modelType.name() : null)
          .build()
      );
    printOutput(response);
    return 0;
  }

  @Override
  protected String getSummaryOutput(CommonResponse response) {
    return ((ModelSearchResponse) response).getModels().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return response.toString();
  }
}
