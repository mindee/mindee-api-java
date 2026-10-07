package com.mindee.v2;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.clientoptions.PollingOptions;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.http.MindeeHttpApiV2;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.parsing.BaseResponse;
import com.mindee.v2.parsing.JobResponse;
import com.mindee.v2.parsing.error.ErrorResponse;
import com.mindee.v2.parsing.search.BaseSearchResponse;
import com.mindee.v2.parsing.search.SearchResponse;
import com.mindee.v2.product.extraction.ExtractionResponse;
import com.mindee.v2.search.models.ModelSearchParameters;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.function.BooleanSupplier;

/**
 * Entry point for the Mindee **V2** API features.
 */
public class MindeeClient {
  private static final System.Logger logger = System.getLogger(MindeeClient.class.getName());

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
      BaseProductParameters params
  ) throws IOException {
    logger.log(System.Logger.Level.INFO, "Enqueuing: local source");
    return mindeeApi.reqPostProductEnqueue(inputSource, params);
  }

  /**
   * Enqueue a document in the asynchronous queue.
   *
   * @param inputSource The URL input source to send.
   * @param params The parameters to send along with the file.
   */
  public JobResponse enqueue(
      URLInputSource inputSource,
      BaseProductParameters params
  ) throws IOException {
    logger.log(System.Logger.Level.INFO, "Enqueuing: URL source");
    inputSource.validateSecure();
    return mindeeApi.reqPostProductEnqueue(inputSource, params);
  }

  /**
   * Get the status of an inference that was previously enqueued.
   * Can be used for polling.
   */
  public JobResponse getJobFromUrl(String pollingUrl) {
    logger.log(System.Logger.Level.INFO, "Getting Job at: {0}", pollingUrl);
    if (pollingUrl == null || pollingUrl.isBlank()) {
      throw new IllegalArgumentException("Job URL cannot be null or blank.");
    }
    return mindeeApi.reqGetJobByUrl(pollingUrl);
  }

  /**
   * Get the status of an inference that was previously enqueued.
   * Can be used for polling.
   */
  public JobResponse getJob(String jobId) {
    logger.log(System.Logger.Level.INFO, "Getting job ID: {0}", jobId);
    if (jobId == null || jobId.isBlank()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }
    return mindeeApi.reqGetJobById(jobId);
  }

  /**
   * Get the result of an inference that was previously enqueued.
   * The inference will only be available after it has finished processing.
   */
  public <TResponse extends BaseResponse> TResponse getResult(
      Class<TResponse> responseClass,
      String inferenceId
  ) {
    logger.log(System.Logger.Level.INFO, "Getting result with ID: {0}", inferenceId);
    if (inferenceId == null || inferenceId.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceId must not be null or blank.");
    }
    return mindeeApi.reqGetResultById(responseClass, inferenceId);
  }

  /**
   * Get the result of an inference from a given URL.
   * The inference will only be available after it has finished processing.
   */
  public <TResponse extends BaseResponse> TResponse getResultFromUrl(
      Class<TResponse> responseClass,
      String inferenceUrl
  ) {
    logger.log(System.Logger.Level.INFO, "Getting result at: {0}", inferenceUrl);
    if (inferenceUrl == null || inferenceUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("inferenceUrl must not be null or blank.");
    }
    return mindeeApi.reqGetResultByUrl(responseClass, inferenceUrl);
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
  public <TResponse extends BaseResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      LocalInputSource inputSource,
      BaseProductParameters params
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
  public <TResponse extends BaseResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      LocalInputSource inputSource,
      BaseProductParameters params,
      PollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    JobResponse job = enqueue(inputSource, params);
    logger
      .log(
        System.Logger.Level.INFO,
        "Successfully enqueued document with job ID {0}",
        job.getJob().getId()
      );
    return pollForResult(responseClass, job, pollingOptions);
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
  public <TResponse extends BaseResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      URLInputSource inputSource,
      BaseProductParameters params
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
  public <TResponse extends BaseResponse> TResponse enqueueAndGetResult(
      Class<TResponse> responseClass,
      URLInputSource inputSource,
      BaseProductParameters params,
      PollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    inputSource.validateSecure();
    JobResponse job = enqueue(inputSource, params);
    logger
      .log(
        System.Logger.Level.INFO,
        "Successfully enqueued document with job ID {0}",
        job.getJob().getId()
      );
    return pollForResult(responseClass, job, pollingOptions);
  }

  /**
   * Search for resources matching the given criteria.
   *
   * @param searchParameters Search parameters
   */
  public <TSearchResponse extends BaseSearchResponse> TSearchResponse search(
      BaseSearchParameters<TSearchResponse> searchParameters
  ) {
    Objects.requireNonNull(searchParameters);
    return mindeeApi.reqGetSearch(searchParameters);
  }

  /**
   * Return all models.
   *
   * @return an instance of {@link SearchResponse}
   * @deprecated Use {@link #search} instead.
   */
  @Deprecated
  public SearchResponse searchModels() {
    return mindeeApi.reqGetSearch(ModelSearchParameters.builder().build());
  }

  /**
   * Search for models by name.
   *
   * @param modelName name of the model to search for
   * @return an instance of {@link SearchResponse}
   * @deprecated Use {@link #search} instead.
   */
  @Deprecated
  public SearchResponse searchModels(String modelName) {
    return mindeeApi.reqGetSearch(ModelSearchParameters.builder().name(modelName).build());
  }

  /**
   * Search for models by name and type.
   *
   * @param modelName name of the model to search for
   * @param modelType type of the model to search for
   * @return an instance of {@link SearchResponse}
   * @deprecated Use {@link #search} instead.
   */
  @Deprecated
  public SearchResponse searchModels(String modelName, String modelType) {
    return mindeeApi
      .reqGetSearch(ModelSearchParameters.builder().name(modelName).modelType(modelType).build());
  }

  /**
   * Common logic for polling an asynchronous job for local & url files.
   *
   * @param initialResponse The initial job response.
   * @return an instance of {@link ExtractionResponse}.
   * @throws InterruptedException Throws if interrupted.
   */
  private <TResponse extends BaseResponse> TResponse pollForResult(
      Class<TResponse> responseClass,
      JobResponse initialResponse,
      PollingOptions pollingOptions
  ) throws InterruptedException {
    logger
      .log(
        System.Logger.Level.DEBUG,
        "Waiting {0} seconds before attempting to retrieve the result...",
        pollingOptions.getInitialDelaySec()
      );
    interruptibleSleep(
      (long) (pollingOptions.getInitialDelaySec() * 1000),
      pollingOptions.getCancelToken()
    );

    int tryCounter = 0;
    int max = pollingOptions.getMaxRetries();
    long intervalMillis = (long) (pollingOptions.getIntervalSec() * 1000);

    while (tryCounter < max) {
      logger.log(System.Logger.Level.DEBUG, "Poll attempt {0} of {1}", tryCounter + 1, max);
      var jobResponse = getJobFromUrl(initialResponse.getJob().getPollingUrl());

      if (jobResponse.getJob().getStatus().equals("Processed")) {
        logger
          .log(
            System.Logger.Level.DEBUG,
            "Job ID {0} completed processing at: {1}",
            jobResponse.getJob().getId(),
            jobResponse.getJob().getCompletedAt()
          );
        return getResultFromUrl(responseClass, jobResponse.getJob().getResultUrl());
      }

      // normally the API handler will throw an error, this is a fallback
      if (jobResponse.getJob().getStatus().equals("Failed")) {
        ErrorResponse errorResponse = jobResponse.getJob().getError();
        if (errorResponse != null) {
          throw new MindeeHttpExceptionV2(errorResponse);
        } else {
          throw new MindeeException(
            "Parsing failed for job "
              + jobResponse.getJob().getId()
              + ": No error detail available."
          );
        }
      }
      tryCounter++;
      interruptibleSleep(intervalMillis, pollingOptions.getCancelToken());
    }

    throw new MindeeException("Couldn't retrieve the result after " + tryCounter + " tries.");
  }

  /**
   * Sleeps for the requested duration, honouring both thread interruption and the
   * caller-supplied cancellation token. The cancel token is checked before sleeping
   * and after each 100 ms tick so long waits stay responsive.
   */
  private static void interruptibleSleep(
      long millis,
      BooleanSupplier cancellationToken
  ) throws InterruptedException {
    if (cancellationToken.getAsBoolean()) {
      throw new CancellationException("Polling cancelled");
    }
    long remaining = millis;
    while (remaining > 0) {
      long chunk = Math.min(remaining, 100L);
      Thread.sleep(chunk);
      remaining -= chunk;
      if (cancellationToken.getAsBoolean()) {
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
