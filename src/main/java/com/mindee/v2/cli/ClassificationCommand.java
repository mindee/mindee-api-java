package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.classification.ClassificationResponse;
import com.mindee.v2.product.classification.params.ClassificationParameters;
import picocli.CommandLine.Command;

/**
 * CLI command for the V2 classification utility.
 */
@Command(
    name = "classification",
    description = "Classification utility.",
    mixinStandardHelpOptions = true
)
public class ClassificationCommand extends BaseInferenceCommand {

  @Override
  protected CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        ClassificationResponse.class,
        inputSource,
        ClassificationParameters.builder(modelId).alias(alias).build()
      );
  }

  @Override
  protected String getSummary(CommonResponse response) {
    return ((ClassificationResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return ((ClassificationResponse) response).getInference().toString();
  }
}
