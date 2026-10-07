package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.product.crop.CropResponse;
import com.mindee.v2.product.crop.params.CropParameters;
import picocli.CommandLine.Command;

/**
 * CLI command for the V2 crop product.
 */
@Command(name = "crop", description = "Crop product.", mixinStandardHelpOptions = true)
public class CropCommand extends BaseInferenceCommand {

  @Override
  protected BaseResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        CropResponse.class,
        inputSource,
        CropParameters.builder(modelId).alias(alias).webhookIds(getWebhookIds()).build()
      );
  }

  @Override
  protected String getSummaryOutput(BaseResponse response) {
    return ((CropResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(BaseResponse response) {
    return ((CropResponse) response).getInference().toString();
  }
}
