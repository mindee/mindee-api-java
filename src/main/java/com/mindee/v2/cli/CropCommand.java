package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.crop.CropResponse;
import com.mindee.v2.product.crop.params.CropParameters;
import picocli.CommandLine.Command;

/**
 * CLI command for the V2 crop utility.
 */
@Command(name = "crop", description = "Crop utility.", mixinStandardHelpOptions = true)
public class CropCommand extends BaseInferenceCommand {

  @Override
  protected CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        CropResponse.class,
        inputSource,
        CropParameters.builder(modelId).alias(alias).build()
      );
  }

  @Override
  protected String getSummary(CommonResponse response) {
    return ((CropResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return ((CropResponse) response).getInference().toString();
  }
}
