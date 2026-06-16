package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.split.SplitResponse;
import com.mindee.v2.product.split.params.SplitParameters;
import picocli.CommandLine.Command;

/**
 * CLI command for the V2 split utility.
 */
@Command(name = "split", description = "Split utility.", mixinStandardHelpOptions = true)
public class SplitCommand extends BaseInferenceCommand {

  @Override
  protected CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        SplitResponse.class,
        inputSource,
        SplitParameters.builder(modelId).alias(alias).build()
      );
  }

  @Override
  protected String getSummary(CommonResponse response) {
    return ((SplitResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return ((SplitResponse) response).getInference().toString();
  }
}
