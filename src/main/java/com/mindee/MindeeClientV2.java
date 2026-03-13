package com.mindee;

import com.mindee.http.MindeeApiV2;
import com.mindee.http.MindeeHttpApiV2;
import com.mindee.http.MindeeHttpExceptionV2;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.parsing.v2.CommonResponse;
import com.mindee.parsing.v2.ErrorResponse;
import com.mindee.parsing.v2.InferenceResponse;
import com.mindee.parsing.v2.JobResponse;
import com.mindee.v2.clientOptions.BaseParameters;
import java.io.IOException;

/**
 * Entry point for the Mindee **V2** API features.
 */
public class MindeeClientV2 {
  private final MindeeApiV2 mindeeApi;

  /** Uses an API key read from the environment variables. */
  public MindeeClientV2() {
    this(createDefaultApiV2(""));
  }

  /** Uses the supplied API key. */
  public MindeeClientV2(String apiKey) {
    this(createDefaultApiV2(apiKey));
  }

  /** Inject both a PDF implementation and an HTTP implementation. */
  public MindeeClientV2(MindeeApiV2 mindeeApi) {
    this.mindeeApi = mindeeApi;
  }

  /**
   * @deprecated use `enqueue` instead.
   */
  public JobResponse enqueueInference(
      LocalInputSource inputSource,
      InferenceParameters params
  ) throws IOException {
    return enqueue(inputSource, params);
  }

  /**
   * @deprecated use `enqueue` instead.
   */
  public JobResponse enqueueInference(
      URLInputSource inputSource,
      InferenceParameters params
  ) throws IOException {
    return enqueue(inputSource, params);
  }

  /**
   * Enqueue a document in the asynchronous queue.
   *
   * @param inputSource The local input source to send.
   * @param params The parameters to send along with the file.
   */
  public JobResponse enqueue(
      LocalInputSource inputSource,
      BaseParameters params
  ) throws IOException {
    return mindeeApi.reqPostEnqueue(inputSource, params);
  }

  /**
   * Enqueue a document in the asynchronous queue.
   *
   * @param inputSource The URL input source to send.
   * @param params The parameters to send along with the file.
   */
  public JobResponse enqueue(URLInputSource inputSource, BaseParameters params) throws IOException {
    return mindeeApi.reqPostEnqueue(inputSource, params);
  }

  /**
   * Get the status of an inference that was previously enqueued.
   * Can be used for polling.
   */
  public JobResponse getJob(String jobId) {
    if (jobId == null || jobId.trim().isEmpty()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }
    return mindeeApi.reqGetJob(jobId);
  }

  /**
   * @deprecated use `getResult` instead.
   */
  public InferenceResponse getInference(String inferenceId) {
    return getResult(InferenceResponse.class, inferenceId);
  }

  /**
   * Get the result of an inference that was previously enqueued.
   * The inference will only be available after it has finished processing.
   */
  public <TResponse extends CommonResponse> TResponse getResult(
      Class<TResponse> responseClass,
      String inferenceId
  ) {
    if (inferenceId == null || inferenceId.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceId must not be null or blank.");
    }
    return mindeeApi.reqGetResult(responseClass, inferenceId);
  }

  /**
   * @deprecated use `enqueueAndGetResult` instead.
   */
  public InferenceResponse enqueueAndGetInference(
      LocalInputSource inputSource,
      InferenceParameters options
  ) throws IOException, InterruptedException {
    return enqueueAndGetResult(InferenceResponse.class, inputSource, options);
  }

  /**
   * @deprecated use `enqueueAndGetResult` instead.
   */
  public InferenceResponse enqueueAndGetInference(
      URLInputSource inputSource,
      InferenceParameters options
  ) throws IOException, InterruptedException {
    return enqueueAndGetResult(InferenceResponse.class, inputSource, options);
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   *
   * @param inputSource The local input source to send.
   * @param params The parameters to send along with the file.
   * @return an instance of {@link InferenceResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      LocalInputSource inputSource,
      BaseParameters params
  ) throws IOException, InterruptedException {
    validatePollingOptions(params.getPollingOptions());
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, params);
  }

  /**
   * Send a remote file to an async queue, poll, and parse when complete.
   *
   * @param inputSource The URL input source to send.
   * @param params The parameters to send along with the file.
   * @return an instance of {@link InferenceResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      URLInputSource inputSource,
      BaseParameters params
  ) throws IOException, InterruptedException {
    validatePollingOptions(params.getPollingOptions());
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, params);
  }

  /**
   * Common logic for polling an asynchronous job for local & url files.
   *
   * @param initialJob The initial job response.
   * @return an instance of {@link InferenceResponse}.
   * @throws InterruptedException Throws if interrupted.
   */
  private <TResponse extends CommonResponse> TResponse pollAndFetch(
      Class<TResponse> responseClass,
      JobResponse initialJob,
      BaseParameters options
  ) throws InterruptedException {
    Thread.sleep((long) (options.getPollingOptions().getInitialDelaySec() * 1000));

    JobResponse resp = initialJob;
    int attempts = 0;
    int max = options.getPollingOptions().getMaxRetries();

    while (attempts < max) {
      Thread.sleep((long) (options.getPollingOptions().getIntervalSec() * 1000));
      resp = getJob(initialJob.getJob().getId());

      if (resp.getJob().getStatus().equals("Failed")) {
        attempts = max;
      }
      if (resp.getJob().getStatus().equals("Processed")) {
        return getResult(responseClass, resp.getJob().getId());
      }
      attempts++;
    }

    ErrorResponse error = resp.getJob().getError();
    if (error != null) {
      throw new MindeeHttpExceptionV2(error.getStatus(), error.getDetail());
    }
    throw new RuntimeException("Max retries exceeded (" + max + ").");
  }

  private static MindeeApiV2 createDefaultApiV2(String apiKey) {
    MindeeSettingsV2 settings = apiKey == null || apiKey.trim().isEmpty()
        ? new MindeeSettingsV2()
        : new MindeeSettingsV2(apiKey);
    return MindeeHttpApiV2.builder().mindeeSettings(settings).build();
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
