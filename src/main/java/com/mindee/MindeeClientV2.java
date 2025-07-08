package com.mindee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.http.MindeeApiV2;
import com.mindee.http.MindeeHttpApiV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.AsyncInferenceResponse;
import com.mindee.parsing.v2.AsyncJobResponse;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfOperation;
import java.io.IOException;

/**
 * Entry point for the Mindee **V2** API features.
 */
public class MindeeClientV2 extends CommonClient {
  private final MindeeApiV2 mindeeApi;

  /** Uses an API-key read from the environment variables. */
  public MindeeClientV2() {
    this(new PdfBoxApi(), createDefaultApiV2(""));
  }

  /** Uses the supplied API-key. */
  public MindeeClientV2(String apiKey) {
    this(new PdfBoxApi(), createDefaultApiV2(apiKey));
  }

  /** Directly inject an already configured {@link MindeeApiV2}. */
  public MindeeClientV2(MindeeApiV2 mindeeApi) {
    this(new PdfBoxApi(), mindeeApi);
  }

  /** Inject both a PDF implementation and a HTTP implementation. */
  public MindeeClientV2(PdfOperation pdfOperation, MindeeApiV2 mindeeApi) {
    this.pdfOperation = pdfOperation;
    this.mindeeApi = mindeeApi;
  }

  /**
   * Enqueue a document in the asynchronous “Generated” queue.
   */
  public AsyncJobResponse enqueue(
      LocalInputSource inputSource,
      InferencePredictOptions options) throws IOException {
    LocalInputSource finalInput;
    if (options.getPageOptions() != null) {
      finalInput = new LocalInputSource(getSplitFile(inputSource, options.getPageOptions()), inputSource.getFilename());
    } else {
      finalInput = inputSource;
    }
    return mindeeApi.enqueuePost(finalInput, options);
  }

  /**
   * Retrieve results for a previously enqueued document.
   */
  public CommonResponse parseQueued(String jobId) {
    if (jobId == null || jobId.trim().isEmpty()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }
    return mindeeApi.getInferenceFromQueue(jobId);
  }

  /**
   * Convenience helper: enqueue, poll, and return the final inference.
   */
  public AsyncInferenceResponse enqueueAndParse(
      LocalInputSource inputSource,
      InferencePredictOptions options,
      AsyncPollingOptions polling) throws IOException, InterruptedException {

    if (polling == null) {
      polling = AsyncPollingOptions.builder().build(); // default values
    }
    validatePollingOptions(polling);

    AsyncJobResponse job = enqueue(inputSource, options);

    Thread.sleep((long) (polling.getInitialDelaySec() * 1000));

    int attempts = 0;
    int max = polling.getMaxRetries();
    while (attempts < max) {
      Thread.sleep((long) (polling.getIntervalSec() * 1000));
      CommonResponse resp = parseQueued(job.getJob().getId());
      if (resp instanceof AsyncInferenceResponse) {
        return (AsyncInferenceResponse) resp;
      }
      attempts++;
    }
    throw new RuntimeException("Max retries exceeded (" + max + ").");
  }

  /**
   * Deserialize a webhook payload (or any saved response) into
   * {@link AsyncInferenceResponse}.
   */
  public AsyncInferenceResponse loadInference(LocalResponse localResponse) throws IOException {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    AsyncInferenceResponse model =
        mapper.readValue(localResponse.getFile(), AsyncInferenceResponse.class);
    model.setRawResponse(localResponse.toString());
    return model;
  }

  private static MindeeApiV2 createDefaultApiV2(String apiKey) {
    MindeeSettingsV2 settings = apiKey == null || apiKey.trim().isEmpty()
        ? new MindeeSettingsV2()
        : new MindeeSettingsV2(apiKey);
    return MindeeHttpApiV2.builder()
        .mindeeSettings(settings)
        .build();
  }

  private static void validatePollingOptions(AsyncPollingOptions p) {
    if (p.getInitialDelaySec() < 1) {
      throw new IllegalArgumentException("Initial delay must be ≥ 1 s");
    }
    if (p.getIntervalSec() < 1) {
      throw new IllegalArgumentException("Interval must be ≥ 1 s");
    }
    if (p.getMaxRetries() < 2) {
      throw new IllegalArgumentException("Max retries must be ≥ 2");
    }
  }
}
