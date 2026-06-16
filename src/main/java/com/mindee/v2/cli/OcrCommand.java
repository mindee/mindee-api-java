package com.mindee.v2.cli;

import com.mindee.input.LocalInputSource;
import com.mindee.v2.MindeeClient;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.product.ocr.OcrResponse;
import com.mindee.v2.product.ocr.params.OcrParameters;
import picocli.CommandLine.Command;

/**
 * CLI command for the V2 OCR utility.
 */
@Command(name = "ocr", description = "OCR utility.", mixinStandardHelpOptions = true)
public class OcrCommand extends BaseInferenceCommand {

  @Override
  protected CommonResponse executeRequest(
      MindeeClient client,
      LocalInputSource inputSource
  ) throws Exception {
    return client
      .enqueueAndGetResult(
        OcrResponse.class,
        inputSource,
        OcrParameters.builder(modelId).alias(alias).build()
      );
  }

  @Override
  protected String getSummary(CommonResponse response) {
    return ((OcrResponse) response).getInference().getResult().toString();
  }

  @Override
  protected String getFullOutput(CommonResponse response) {
    return ((OcrResponse) response).getInference().toString();
  }
}
