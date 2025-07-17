package com.mindee;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.http.MindeeApiV2;
import com.mindee.http.MindeeHttpApiV2;
import com.mindee.http.MindeeHttpExceptionV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import java.io.IOException;

/**
 * Entry point for the Mindee **V2** API features.
 */
public class MindeeClientV2 {
  private final MindeeApiV2 mindeeApi;

  /** Uses an API-key read from the environment variables. */
  public MindeeClientV2() {
    this(createDefaultApiV2(""));
  }

  /** Uses the supplied API-key. */
  public MindeeClientV2(String apiKey) {
    this(createDefaultApiV2(apiKey));
  }


  /** Inject both a PDF implementation and a HTTP implementation. */
  public MindeeClientV2(MindeeApiV2 mindeeApi) {
    this.mindeeApi = mindeeApi;
  }

  /**
   * Enqueue a document in the asynchronous “Generated” queue.
   */
  public JobResponse enqueue(
      LocalInputSource inputSource,
      InferenceParameters params) throws IOException {
    return mindeeApi.reqPostInferenceEnqueue(inputSource, params);
  }

  /**
   * Poll queue for a previously enqueued document.
   */
  public JobResponse pollQueue(String jobId) {
    if (jobId == null || jobId.trim().isEmpty()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }
    return mindeeApi.reqGetJob(jobId);
  }

  /**
   * Retrieve results for a previously enqueued document.
   */
  public InferenceResponse parseQueued(String jobId) {
    if (jobId == null || jobId.trim().isEmpty()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }

    return mindeeApi.reqGetInference(jobId);
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   * @param inputSource The input source to send.
   * @param options The options to send along with the file.
   * @return an instance of {@link InferenceResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public InferenceResponse enqueueAndParse(
      LocalInputSource inputSource,
      InferenceParameters options) throws IOException, InterruptedException {

    validatePollingOptions(options.getPollingOptions());

    JobResponse job = enqueue(inputSource, options);

    Thread.sleep((long) (options.getPollingOptions().getInitialDelaySec() * 1000));
    JobResponse resp = job;
    int attempts = 0;
    int max = options.getPollingOptions().getMaxRetries();
    while (attempts < max) {
      Thread.sleep((long) (options.getPollingOptions().getIntervalSec() * 1000));
      resp = pollQueue(job.getJob().getId());
      if (resp.getJob().getStatus().equals("Failed")) {
        break;
      }
      else if (resp.getJob().getStatus().equals("Processed")) {
        return parseQueued(resp.getJob().getId());
      }
      attempts++;
    }
    ErrorResponse error = resp.getJob().getError();
    if (error != null) {
      throw new MindeeHttpExceptionV2(error.getStatus(), error.getDetail());
    }
    throw new RuntimeException("Max retries exceeded (" + max + ").");
  }

  /**
   * Deserialize a webhook payload (or any saved response) into
   * {@link InferenceResponse}.
   */
  public InferenceResponse loadInference(LocalResponse localResponse) throws IOException {
    ObjectMapper mapper = new ObjectMapper().findAndRegisterModules();
    InferenceResponse model =
        mapper.readValue(localResponse.getFile(), InferenceResponse.class);
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
