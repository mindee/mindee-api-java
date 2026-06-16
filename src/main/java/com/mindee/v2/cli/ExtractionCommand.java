package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.extraction.ExtractionInference;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.product.extraction.params.ExtractionParameters;
import java.util.StringJoiner;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/**
 * CLI command for the V2 generic all-purpose extraction utility.
 */
@Command(
    name = "extraction",
    description = "Generic all-purpose extraction.",
    mixinStandardHelpOptions = true
)
public class ExtractionCommand extends BaseInferenceCommand {

  @Option(
      names = { "-g", "--rag" },
      description = "Enable RAG context. Only valid for 'extraction' product.",
      defaultValue = "false"
  )
  private boolean rag;

  @Option(
      names = { "-r", "--raw-text" },
      description = "To get all the words in the current document. False by default.",
      defaultValue = "false"
  )
  private boolean rawText;

  @Option(
      names = { "-c", "--confidence" },
      description = "To retrieve confidence scores from the extraction. False by default.",
      defaultValue = "false"
  )
  private boolean confidence;

  @Option(
      names = { "-p", "--polygon" },
      description = "To retrieve bounding boxes from the extraction. False by default.",
      defaultValue = "false"
  )
  private boolean polygon;

  @Option(
      names = { "-t", "--text-context" },
      description = "To add text context to your API call. False by default."
  )
  private String textContext;

  @Override
  protected CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        ExtractionResponse.class,
        inputSource,
        ExtractionParameters
          .builder(modelId)
          .alias(alias)
          .rag(rag)
          .rawText(rawText)
          .confidence(confidence)
          .polygon(polygon)
          .textContext(textContext)
          .build()
      );
  }

  @Override
  protected String getSummary(CommonResponse response) {
    return ((ExtractionResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    ExtractionInference inference = ((ExtractionResponse) response).getInference();
    var joiner = new StringJoiner("\n");

    if (
      rawText
        && inference.getActiveOptions().getRawText()
        && inference.getResult().getRawText() != null
    ) {
      joiner.add("#############\nRaw Text\n#############\n::");
      var rawTextStr = inference.getResult().getRawText().toString().replace("\n", "\n  ");
      joiner.add("  " + rawTextStr + "\n");
    }

    if (rag && inference.getActiveOptions().getRag() && inference.getResult().getRag() != null) {
      joiner.add("#############\nRetrieval-Augmented Generation\n#############\n::");
      var ragStr = inference.getResult().getRag().toString().replace("\n", "\n  ");
      joiner.add("  " + ragStr + "\n");
    }

    joiner.add(inference.toString());
    return joiner.toString();
  }
}
