package com.mindee.v2;

import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientOptions.BaseParameters;
import com.mindee.v2.clientOptions.PollingOptions;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.http.MindeeHttpApiV2;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.ErrorResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;

/**
 * Entry point for the Mindee **V2** API features.
 */
public class MindeeClient {
  private final MindeeApiV2 mindeeApi;

  /** Uses an API key read from the environment variables. */
  public MindeeClient() {
    this(createDefaultApiV2(""));
  }

  /** Uses the supplied API key. */
  public MindeeClient(String apiKey) {
    this(createDefaultApiV2(apiKey));
  }

  /** Inject a custom HTTP API implementation. */
  public MindeeClient(MindeeApiV2 mindeeApi) {
    this.mindeeApi = mindeeApi;
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
   * Send a local file to an async queue, poll, and parse when complete.
   * Use default polling options.
   *
   * @param inputSource The local input source to send.
   * @param params The product parameters to send along with the file.
   * @return an instance of {@link ExtractionResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      LocalInputSource inputSource,
      BaseParameters params
  ) throws IOException, InterruptedException {
    return enqueueAndGetResult(
      responseClass,
      inputSource,
      params,
      PollingOptions.builder().build()
    );
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   * Specify polling options.
   *
   * @param inputSource The local input source to send.
   * @param params The product parameters to send along with the file.
   * @param pollingOptions The polling options to use.
   * @return an instance of {@link ExtractionResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      LocalInputSource inputSource,
      BaseParameters params,
      PollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, pollingOptions);
  }

  /**
   * Send a remote file to an async queue, poll, and parse when complete.
   * Use default polling options.
   *
   * @param inputSource The URL input source to send.
   * @param params The product parameters to send along with the file.
   * @return an instance of {@link ExtractionResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      URLInputSource inputSource,
      BaseParameters params
  ) throws IOException, InterruptedException {
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, PollingOptions.builder().build());
  }

  /**
   * Send a remote file to an async queue, poll, and parse when complete.
   * Specify polling options.
   *
   * @param inputSource The URL input source to send.
   * @param params The product parameters to send along with the file.
   * @param pollingOptions The polling options to use.
   * @return an instance of {@link ExtractionResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  public <TResponse extends CommonResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      URLInputSource inputSource,
      BaseParameters params,
      PollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, pollingOptions);
  }

  /**
   * Common logic for polling an asynchronous job for local & url files.
   *
   * @param initialJob The initial job response.
   * @return an instance of {@link ExtractionResponse}.
   * @throws InterruptedException Throws if interrupted.
   */
  private <TResponse extends CommonResponse> TResponse pollAndFetch(
      Class<TResponse> responseClass,
      JobResponse initialJob,
      PollingOptions pollingOptions
  ) throws InterruptedException {
    Thread.sleep((long) (pollingOptions.getInitialDelaySec() * 1000));

    JobResponse resp = initialJob;
    int attempts = 0;
    int max = pollingOptions.getMaxRetries();

    while (attempts < max) {
      Thread.sleep((long) (pollingOptions.getIntervalSec() * 1000));
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
    MindeeSettings settings = apiKey == null || apiKey.trim().isEmpty()
        ? new MindeeSettings()
        : new MindeeSettings(apiKey);
    return MindeeHttpApiV2.builder().mindeeSettings(settings).build();
  }
}
