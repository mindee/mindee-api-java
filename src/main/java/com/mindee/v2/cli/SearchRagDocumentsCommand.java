package com.mindee.v2.cli;

import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.search.ragdocuments.RagDocumentSearchParameters;
import com.mindee.v2.search.ragdocuments.RagDocumentSearchResponse;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * CLI command for searching available V2 RAG documents.
 */
@Command(
    name = "search-rag-docs",
    description = "Search available RAG documents for a given model.",
    mixinStandardHelpOptions = true
)
public class SearchRagDocumentsCommand extends BaseCommand {

  @Option(names = { "-m", "--model-id" }, description = "Filter by model ID.", required = true)
  private String modelId;

  @Option(
      names = { "-f", "--filename" },
      description = "Filter by file name partial match (case insensitive)."
  )
  private String filename;

  @Override
  protected String getSummaryOutput(CommonResponse response) {
    return ((RagDocumentSearchResponse) response).getRagDocuments().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return response.toString();
  }

  @Override
  public Integer call() throws Exception {
    var client = new MindeeClient(apiKey);
    var response = client
      .search(RagDocumentSearchParameters.builder(modelId).filename(filename).build());
    printOutput(response);
    return 0;
  }
}
