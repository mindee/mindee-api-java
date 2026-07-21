package com.mindee.v2;

import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientoptions.BaseParameters;
import com.mindee.v2.clientoptions.PollingOptions;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.http.MindeeHttpApiV2;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.parsing.CommonResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.error.ErrorResponse;
import com.mindee.v2.parsing.search.SearchResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.IOException;
import java.util.concurrent.CancellationException;

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
    inputSource.validateSecure();
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
   * Get the result of an inference from a given URL.
   * The inference will only be available after it has finished processing.
   */
  public <TResponse extends CommonResponse> TResponse getResultFromUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  ) {
    if (inferenceUrl == null || inferenceUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceUrl must not be null or blank.");
    }
    return mindeeApi.reqGetResultFromUrl(responseClass, inferenceUrl);
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
    return enqueueAndGetResult(
      responseClass,
      inputSource,
      params,
      PollingOptions.builder().build()
    );
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
    inputSource.validateSecure();
    JobResponse job = enqueue(inputSource, params);
    return pollAndFetch(responseClass, job, pollingOptions);
  }

  /**
   * Return all models.
   *
   * @return an instance of {@link SearchResponse}
   */
  public SearchResponse searchModels() {
    return searchModels(null, null);
  }

  /**
   * Search for models by name.
   *
   * @param modelName name of the model to search for
   * @return an instance of {@link SearchResponse}
   */
  public SearchResponse searchModels(String modelName) {
    return searchModels(modelName, null);
  }

  /**
   * Search for models by name and type.
   *
   * @param modelName name of the model to search for
   * @param modelType type of the model to search for
   * @return an instance of {@link SearchResponse}
   */
  public SearchResponse searchModels(String modelName, String modelType) {
    return mindeeApi.reqGetSearchModels(modelName, modelType);
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
    interruptibleSleep((long) (pollingOptions.getInitialDelaySec() * 1000), pollingOptions);

    JobResponse resp = initialJob;
    int attempts = 0;
    int max = pollingOptions.getMaxRetries();
    double currentIntervalSec = pollingOptions.getIntervalSec();
    double maxIntervalSec = pollingOptions.getMaxIntervalSec();
    double backoffMultiplier = pollingOptions.getBackoffMultiplier();

    while (attempts < max) {
      interruptibleSleep((long) (currentIntervalSec * 1000), pollingOptions);
      resp = getJob(initialJob.getJob().getId());

      if (resp.getJob().getStatus().equals("Failed")) {
        attempts = max;
      }
      if (resp.getJob().getStatus().equals("Processed")) {
        return getResult(responseClass, resp.getJob().getId());
      }
      currentIntervalSec = Math.min(currentIntervalSec * backoffMultiplier, maxIntervalSec);
      attempts++;
    }

    ErrorResponse error = resp.getJob().getError();
    if (error != null) {
      throw new MindeeHttpExceptionV2(error.getStatus(), error.getDetail());
    }
    throw new RuntimeException("Max retries exceeded (" + max + ").");
  }

  /**
   * Sleeps for the requested duration, honouring both thread interruption and the
   * caller-supplied cancellation token. The cancel token is checked before sleeping
   * and after each 100 ms tick so long waits stay responsive.
   */
  private static void interruptibleSleep(
      long millis,
      PollingOptions options
  ) throws InterruptedException {
    if (options.getCancelToken().getAsBoolean()) {
      throw new CancellationException("Polling cancelled");
    }
    long remaining = millis;
    while (remaining > 0) {
      long chunk = Math.min(remaining, 100L);
      Thread.sleep(chunk);
      remaining -= chunk;
      if (options.getCancelToken().getAsBoolean()) {
        throw new CancellationException("Polling cancelled");
      }
    }
  }

  private static MindeeApiV2 createDefaultApiV2(String apiKey) {
    MindeeSettings settings = apiKey == null || apiKey.trim().isEmpty()
        ? new MindeeSettings()
        : new MindeeSettings(apiKey);
    return MindeeHttpApiV2.builder().mindeeSettings(settings).build();
  }
}
