package com.mindee.v2;

import com.mindee.MindeeException;
import com.mindee.input.LocalInputSource;
import com.mindee.input.URLInputSource;
import com.mindee.v2.clientoptions.BaseAnnotationParameters;
import com.mindee.v2.clientoptions.BaseProductParameters;
import com.mindee.v2.clientoptions.BaseRagDocumentUploadParameters;
import com.mindee.v2.clientoptions.BaseSearchParameters;
import com.mindee.v2.clientoptions.PollingOptions;
import com.mindee.v2.http.MindeeApiV2;
import com.mindee.v2.http.MindeeHttpApiV2;
import com.mindee.v2.http.MindeeHttpExceptionV2;
import com.mindee.v2.parsing.BaseRagAnnotationResponse;
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
    if (pollingUrl == null || pollingUrl.isBlank()) {
      throw new IllegalArgumentException("Job URL cannot be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Getting Job at: {0}", pollingUrl);
    return mindeeApi.reqGetJobByUrl(pollingUrl);
  }

  /**
   * Get the status of an inference that was previously enqueued.
   * Can be used for polling.
   */
  public JobResponse getJob(String jobId) {
    if (jobId == null || jobId.isBlank()) {
      throw new IllegalArgumentException("jobId must not be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Getting job ID: {0}", jobId);
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
    if (inferenceId == null || inferenceId.isBlank()) {
      throw new IllegalArgumentException("inferenceId must not be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Getting result with ID: {0}", inferenceId);
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
    if (inferenceUrl == null || inferenceUrl.isBlank()) {
      throw new IllegalArgumentException("inferenceUrl must not be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Getting result at: {0}", inferenceUrl);
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
   * Not recommended for general use, prefer {@link #uploadAndGetRagDocument}.
   * You will need to poll until the document is ready for use.
   * Add a document to the RAG database.
   *
   * @param inputSource The file to upload.
   * @param parameters RAG document upload parameters.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse uploadRagDocument(
      LocalInputSource inputSource,
      BaseRagDocumentUploadParameters<TAnnotationResponse> parameters
  ) throws IOException {
    logger.log(System.Logger.Level.INFO, "Adding a document to the RAG database");
    return mindeeApi.reqPostRagDocument(parameters, inputSource);
  }

  /**
   * Add a document to the RAG database, poll, and return the initial annotation.
   *
   * @param inputSource The file to upload.
   * @param parameters RAG document upload parameters.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse uploadAndGetRagDocument(
      LocalInputSource inputSource,
      BaseRagDocumentUploadParameters<TAnnotationResponse> parameters
  ) throws IOException, InterruptedException {
    return uploadAndGetRagDocument(inputSource, parameters, null);
  }

  /**
   * Add a document to the RAG database, poll, and return the initial annotation.
   *
   * @param inputSource The file to upload.
   * @param parameters RAG document upload parameters.
   * @param pollingOptions Polling options (if null, default options are used).
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse uploadAndGetRagDocument(
      LocalInputSource inputSource,
      BaseRagDocumentUploadParameters<TAnnotationResponse> parameters,
      PollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    if (pollingOptions == null) {
      pollingOptions = PollingOptions.builder().build();
    }
    TAnnotationResponse initialResponse = uploadRagDocument(inputSource, parameters);
    if (!"Processing".equals(initialResponse.getStatus())) {
      return initialResponse;
    }
    return pollForRagDocument(parameters.getResponseClass(), initialResponse, pollingOptions);
  }

  /**
   * Not recommended for general use, prefer {@link #getReadyRagDocument}.
   * You will need to poll until the document is ready for use.
   * Get a document's info and annotations from the RAG database.
   *
   * @param responseClass The class of the response.
   * @param documentId The document's ID.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse getRagDocument(
      Class<TAnnotationResponse> responseClass,
      String documentId
  ) {
    if (documentId == null || documentId.isBlank()) {
      throw new IllegalArgumentException("documentId must not be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Getting RAG document ID: {0}", documentId);
    return mindeeApi.reqGetRagAnnotation(responseClass, documentId);
  }

  /**
   * Get a document's info and annotations from the RAG database, polling if it is still processing.
   *
   * @param responseClass The class of the response.
   * @param documentId The document's ID.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse getReadyRagDocument(
      Class<TAnnotationResponse> responseClass,
      String documentId
  ) throws InterruptedException {
    return getReadyRagDocument(responseClass, documentId, null);
  }

  /**
   * Get a document's info and annotations from the RAG database, polling if it is still processing.
   *
   * @param responseClass The class of the response.
   * @param documentId The document's ID.
   * @param pollingOptions Polling options (if null, default options are used).
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse getReadyRagDocument(
      Class<TAnnotationResponse> responseClass,
      String documentId,
      PollingOptions pollingOptions
  ) throws InterruptedException {
    TAnnotationResponse initialResponse = getRagDocument(responseClass, documentId);
    if (!"Processing".equals(initialResponse.getStatus())) {
      return initialResponse;
    }

    if (pollingOptions == null) {
      pollingOptions = PollingOptions.builder().build();
    }
    return pollForRagDocument(responseClass, initialResponse, pollingOptions);
  }

  /**
   * Not recommended for general use, prefer {@link #updateAndGetRagAnnotation}.
   * You will need to poll until the document is ready for use.
   * Update a document's annotations in the RAG database.
   *
   * @param parameters Annotation parameters.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse updateRagAnnotation(
      BaseAnnotationParameters<TAnnotationResponse> parameters
  ) {
    logger
      .log(System.Logger.Level.INFO, "Updating RAG document ID: {0}", parameters.getDocumentId());
    return mindeeApi.reqPatchRagAnnotation(parameters);
  }

  /**
   * Update a document's annotations in the RAG database and poll until complete.
   *
   * @param parameters Annotation parameters.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse updateAndGetRagAnnotation(
      BaseAnnotationParameters<TAnnotationResponse> parameters
  ) throws InterruptedException {
    return updateAndGetRagAnnotation(parameters, null);
  }

  /**
   * Update a document's annotations in the RAG database and poll until complete.
   *
   * @param parameters Annotation parameters.
   * @param pollingOptions Polling options (if null, default options are used).
   * @return an instance of {@link BaseRagAnnotationResponse}.
   */
  public <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse updateAndGetRagAnnotation(
      BaseAnnotationParameters<TAnnotationResponse> parameters,
      PollingOptions pollingOptions
  ) throws InterruptedException {
    TAnnotationResponse initialResponse = updateRagAnnotation(parameters);
    if (!"Processing".equals(initialResponse.getStatus())) {
      return initialResponse;
    }

    if (pollingOptions == null) {
      pollingOptions = PollingOptions.builder().build();
    }
    return pollForRagDocument(parameters.getResponseClass(), initialResponse, pollingOptions);
  }

  /**
   * Delete a document from the RAG database.
   * For extraction models only.
   *
   * @param documentId The document's ID.
   * @return true if successful.
   */
  public boolean deleteExtractionRagDocument(String documentId) {
    if (documentId == null || documentId.isBlank()) {
      throw new IllegalArgumentException("documentId must not be null or blank.");
    }
    logger.log(System.Logger.Level.INFO, "Deleting RAG document ID: {0}", documentId);
    return mindeeApi.reqDeleteExtractionRagDocument(documentId);
  }

  /**
   * Poll until the RAG document is finished processing or the max number of attempts is reached.
   *
   * @param responseClass The class of the response.
   * @param initialResponse The initial annotation response.
   * @param pollingOptions Polling options.
   * @return an instance of {@link BaseRagAnnotationResponse}.
   * @throws InterruptedException Throws if the thread is interrupted.
   */
  private <TAnnotationResponse extends BaseRagAnnotationResponse> TAnnotationResponse pollForRagDocument(
      Class<TAnnotationResponse> responseClass,
      BaseRagAnnotationResponse initialResponse,
      PollingOptions pollingOptions
  ) throws InterruptedException {
    logger
      .log(System.Logger.Level.INFO, "Polling for RAG document ID: {0}", initialResponse.getId());
    int maxRetries = pollingOptions.getMaxRetries() + 1;

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

    String documentId = initialResponse.getId();
    long intervalMillis = (long) (pollingOptions.getIntervalSec() * 1000);
    int retryCount = 1;

    while (retryCount < maxRetries) {
      logger.log(System.Logger.Level.DEBUG, "Poll attempt {0} of {1}", retryCount, maxRetries);

      TAnnotationResponse response = getRagDocument(responseClass, documentId);
      retryCount++;

      String status = response.getStatus();
      if ("Processing".equals(status)) {
        interruptibleSleep(intervalMillis, pollingOptions.getCancelToken());
      } else if ("Failed".equals(status)) {
        throw new MindeeException("RAG failed without an error payload.");
      } else {
        return response;
      }
    }
    throw new MindeeException("RAG polling not complete after " + retryCount + " attempts.");
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
