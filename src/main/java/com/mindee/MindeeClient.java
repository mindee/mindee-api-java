package com.mindee;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.http.Endpoint;
import com.mindee.http.MindeeApi;
import com.mindee.http.MindeeHttpApi;
import com.mindee.http.RequestParameters;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.input.LocalResponse;
import com.mindee.input.PageOptions;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.common.WorkflowResponse;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.product.custom.CustomV1;
import com.mindee.product.generated.GeneratedV1;
import java.io.IOException;
import java.net.URL;

/**
 * Main entrypoint for Mindee operations.
 */
public class MindeeClient {

  private final MindeeApi mindeeApi;
  private final PdfOperation pdfOperation;

  /**
   * Create a default MindeeClient.
   * You'll need to set the API key in the environment for this approach to work properly.
   */
  public MindeeClient() {
    this.pdfOperation = new PdfBoxApi();
    this.mindeeApi = createDefaultApi("");
  }

  /**
   * Create a default MindeeClient.
   *
   * @param apiKey The api key to use.
   */
  public MindeeClient(String apiKey) {
    this.pdfOperation = new PdfBoxApi();
    this.mindeeApi = createDefaultApi(apiKey);
  }

  /**
   * Create a MindeeClient using a MindeeApi.
   *
   * @param mindeeApi The MindeeApi implementation to be used by the created MindeeClient.
   */
  public MindeeClient(MindeeApi mindeeApi) {
    this.pdfOperation = new PdfBoxApi();
    this.mindeeApi = mindeeApi;
  }

  /**
   * Create a MindeeClient.
   * @param pdfOperation The PdfOperation implementation to be used by the created MindeeClient.
   * @param mindeeApi The MindeeApi implementation to be used by the created MindeeClient.
   */
  public MindeeClient(PdfOperation pdfOperation, MindeeApi mindeeApi) {
    this.pdfOperation = pdfOperation;
    this.mindeeApi = mindeeApi;
  }

  private static MindeeApi createDefaultApi(String apiKey) {
    MindeeSettings mindeeSettings;
    if (apiKey != null && !apiKey.trim().isEmpty()) {
      mindeeSettings = new MindeeSettings(apiKey);
    } else {
      mindeeSettings = new MindeeSettings();
    }
    return MindeeHttpApi.builder()
      .mindeeSettings(mindeeSettings)
      .build();
  }

  /**
   * Parse a document from an async queue.
   * @param <T> Type of inference.
   * @param type Type of inference;
   * @param jobId ID of the job.
   * @return A valid prediction.
   */
  public <T extends Inference> AsyncPredictResponse<T> parseQueued(
      Class<T> type,
      String jobId
  ) {
    return this.mindeeApi.documentQueueGet(
        type,
        new Endpoint(type),
        jobId
    );
  }

  /**
   * Send a local file to an async queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.enqueue(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        null,
        null);
  }

  /**
   * Send a local file to an async queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions
  ) throws IOException {
    return this.enqueue(
        type,
        new Endpoint(type),
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        predictOptions,
      null
    );
  }

  /**
   * Send a remote file to an async queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param sourceUrl A URL to a remote file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      URL sourceUrl
  ) throws IOException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueue(
        type,
        new Endpoint(type),
        null,
        null,
        null,
        sourceUrl
    );
  }

  /**
   * Send a remote file to an async queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param sourceUrl A URL to a remote file.
   * @param predictOptions Prediction options for the enqueuing.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      URL sourceUrl,
      PredictOptions predictOptions
  ) throws IOException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueue(
      type,
      new Endpoint(type),
      null,
      null,
      predictOptions,
      sourceUrl
    );
  }

  private <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      byte[] file,
      String filename,
      PredictOptions predictOptions,
      URL urlInputSource
  ) throws IOException {
    RequestParameters params = RequestParameters.builder()
        .file(file)
        .fileName(filename)
        .predictOptions(predictOptions)
        .urlInputSource(urlInputSource)
        .build();
    return this.mindeeApi.predictAsyncPost(type, endpoint, params);
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in case of timeout.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      LocalInputSource localInputSource
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      new Endpoint(type),
      null,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null);
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param pollingOptions Options for async call parameters
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      LocalInputSource localInputSource,
      AsyncPollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      new Endpoint(type),
      pollingOptions,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null
    );
  }

  /**
   * Send a local file to an async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @param pollingOptions Options for async call parameters.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions,
      AsyncPollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      new Endpoint(type),
      pollingOptions,
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      predictOptions,
      null
    );
  }

  /**
   * Send a remote file to an async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param sourceUrl A URL to a remote file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends Inference> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      URL sourceUrl
  ) throws IOException, InterruptedException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueueAndParse(
      type,
      new Endpoint(type),
      null,
      null,
      null,
      null,
      sourceUrl
    );
  }

  /**
   * @param pollingOptions Options for async call parameters
   * @throws MindeeException Throws if settings aren't set properly.
   */
  private void validateAsyncParams(AsyncPollingOptions pollingOptions) {
    Double minimumInitialDelaySec = 1.0;
    Double minimumIntervalSec = 1.0;
    Integer minimumRetry = 2;
    if (pollingOptions.getInitialDelaySec() < minimumInitialDelaySec) {
      throw new MindeeException(String.format(
        "Cannot set initial delay to less than %.0f second(s)", minimumInitialDelaySec
      ));
    }
    if (pollingOptions.getIntervalSec() < minimumIntervalSec) {
      throw new MindeeException(String.format(
        "Cannot set auto-poll delay to less than %.0f second(s)", minimumIntervalSec
      ));
    }
    if (pollingOptions.getMaxRetries() < minimumRetry) {
      throw new MindeeException(String.format(
        "Cannot set async retries to less than %d attempts", minimumRetry
      ));
    }
  }

  private <T extends Inference> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      Endpoint endpoint,
      AsyncPollingOptions pollingOptions,
      byte[] file,
      String filename,
      PredictOptions predictOptions,
      URL urlInputSource
  ) throws IOException, InterruptedException {
    if (pollingOptions == null) {
      pollingOptions = AsyncPollingOptions.builder().build();
    }
    this.validateAsyncParams(pollingOptions);
    final int initialDelaySec = (int) (pollingOptions.getInitialDelaySec() * 1000);
    final int intervalSec = (int) (pollingOptions.getIntervalSec() * 1000);

    AsyncPredictResponse<T> enqueueResponse = enqueue(
        type,
        endpoint,
        file,
        filename,
        predictOptions,
        urlInputSource
    );

    String jobId = enqueueResponse.getJob().getId();

    AsyncPredictResponse<T> parseResponse;
    int retryCount = 0;

    Thread.sleep(initialDelaySec);

    while (retryCount < pollingOptions.getMaxRetries()) {
      parseResponse = this.mindeeApi.documentQueueGet(type, endpoint, jobId);
      if (parseResponse.getDocument().isPresent()) {
        return parseResponse;
      }
      retryCount++;
      Thread.sleep(intervalSec);
    }
    throw new RuntimeException("Max retries exceeded: " + retryCount +". Failed to get the document.");
  }

  /**
   * Send a local file to a workflow execution.
   * @param workflowId ID of the workflow to send the document to.
   * @param localInputSource A local input source file.
   * @param workflowOptions Options for the workflow execution.
   * @return A workflow response.
   * @throws IOException Throws if the file can't be accessed.
   */
  public WorkflowResponse<GeneratedV1> executeWorkflow(
      String workflowId,
      LocalInputSource localInputSource,
      WorkflowOptions workflowOptions
  ) throws IOException {
    return this.mindeeApi.executeWorkflowPost(
        GeneratedV1.class,
        workflowId,
        RequestParameters.builder()
            .file(localInputSource.getFile())
            .fileName(localInputSource.getFilename())
            .workflowOptions(workflowOptions)
            .build()
    );
  }
  /**
   * Send a local file to a workflow execution.
   * @param workflowId ID of the workflow to send the document to.
   * @param localInputSource A local input source file.
   * @return A workflow response.
   * @throws IOException Throws if the file can't be accessed.
   */
  public WorkflowResponse<GeneratedV1> executeWorkflow(
      String workflowId,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.mindeeApi.executeWorkflowPost(
        GeneratedV1.class,
        workflowId,
        RequestParameters.builder()
            .file(localInputSource.getFile())
            .fileName(localInputSource.getFilename())
            .workflowOptions(WorkflowOptions.builder().build())
            .build()
    );
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        null,
        null
    );
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      PredictOptions predictOptions
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        predictOptions,
        null
    );
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param pageOptions Page options for PDF documents.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        null,
        null
    );
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        predictOptions,
        null
    );
  }

  /**
   * Send a remote file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param urlInputSource A URL to a remote file.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      URL urlInputSource
  ) throws IOException {
    InputSourceUtils.validateUrl(urlInputSource);
    return this.parse(type, new Endpoint(type), null, null, null, urlInputSource);
  }

  /**
   * Send a remote file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param urlInputSource A URL to a remote file.
   * @param predictOptions Prediction options for the enqueuing.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      URL urlInputSource,
      PredictOptions predictOptions
  ) throws IOException {
    InputSourceUtils.validateUrl(urlInputSource);
    return this.parse(type, new Endpoint(type), null, null, predictOptions, urlInputSource);
  }

  private <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      byte[] file,
      String filename,
      PredictOptions predictOptions,
      URL urlInputSource
  ) throws IOException {
    RequestParameters params = RequestParameters.builder()
        .file(file)
        .fileName(filename)
        .predictOptions(predictOptions)
        .urlInputSource(urlInputSource)
        .build();
    return this.mindeeApi.predictPost(type, endpoint, params);
  }

  /**
   * Send a local file to a Custom prediction API and parse the results.
   * @param localInputSource A local input source file.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PredictResponse<CustomV1> parse(
      LocalInputSource localInputSource,
      Endpoint endpoint
  ) throws IOException {
    return this.parse(
      localInputSource.getFile(),
      localInputSource.getFilename(),
      endpoint,
    null);
  }

  /**
   * Send a local file to a Custom prediction API and parse the results.
   * @param localInputSource A local input source file.
   * @param endpoint Custom endpoint to send the document to.
   * @param pageOptions Page options for PDF documents.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PredictResponse<CustomV1> parse(
      LocalInputSource localInputSource,
      Endpoint endpoint,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      endpoint, null
    );
  }

  /**
   * Send a remote file to a Custom prediction API and parse the results.
   * @param documentUrl A URL to a remote file.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public PredictResponse<CustomV1> parse(
      URL documentUrl,
      Endpoint endpoint
  ) throws IOException {
    InputSourceUtils.validateUrl(documentUrl);
    return this.parse(null, null, endpoint, documentUrl);
  }

  private PredictResponse<CustomV1> parse(
      byte[] file,
      String filename,
      Endpoint endpoint,
      URL urlInputSource
  ) throws IOException {
    return this.mindeeApi.predictPost(
      CustomV1.class,
      endpoint,
      RequestParameters.builder()
          .file(file)
          .fileName(filename)
          .urlInputSource(urlInputSource)
          .build());
  }

  /**
   * Send a local file to a Generated prediction async API queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.enqueue(
      type,
      endpoint,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null
    );
  }

  /**
   * Send a local file to a Generated prediction async API queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions
  ) throws IOException {
    return this.enqueue(
      type,
      endpoint,
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      predictOptions,
      null
    );
  }

  /**
   * Send a remote file to a Generated prediction async API queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param endpoint Custom endpoint to send the document to.
   * @param sourceUrl A URL to a remote file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      URL sourceUrl
  ) throws IOException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueue(
      type,
      endpoint,
      null,
      null,
      null,
      sourceUrl
    );
  }

  /**
   * Send a remote file to a Generated prediction async API queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param sourceUrl A URL to a remote file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      URL sourceUrl,
      PredictOptions predictOptions
  ) throws IOException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueue(
      type,
      endpoint,
      null,
      null,
      predictOptions,
      sourceUrl
    );
  }


  /**
   * Send a local file to a Generated prediction API async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      endpoint,
      null,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null);
  }

  /**
   * Send a local file to a Generated prediction API async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param pollingOptions Options for async call parameters.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      AsyncPollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      endpoint,
      pollingOptions,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null
    );
  }

  /**
   * Send a local file to a Generated prediction API async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @param pollingOptions Options for async call parameters.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions,
      AsyncPollingOptions pollingOptions
  ) throws IOException, InterruptedException {
    return this.enqueueAndParse(
      type,
      endpoint,
      pollingOptions,
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      predictOptions,
      null
    );
  }

  /**
   * Send a remote file to a Generated prediction API async queue, poll, and parse when complete.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param endpoint Custom endpoint to send the document to.
   * @param sourceUrl A URL to a remote file.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   * @throws InterruptedException Throws in the event of a timeout.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> enqueueAndParse(
      Class<T> type,
      Endpoint endpoint,
      URL sourceUrl
  ) throws IOException, InterruptedException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueueAndParse(
      type,
      endpoint,
      null,
      null,
      null,
      null,
      sourceUrl
    );
  }

  /**
   * Send a local file to a Generated prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.parse(
      type,
      endpoint,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      null,
      null
    );
  }

  /**
   * Send a local file to a Generated prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      PredictOptions predictOptions
  ) throws IOException {
    return this.parse(
      type,
      endpoint,
      localInputSource.getFile(),
      localInputSource.getFilename(),
      predictOptions,
      null
    );
  }

  /**
   * Send a local file to a Generated prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param pageOptions Page options for PDF documents.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
      type,
      endpoint,
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      null,
      null
    );
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localInputSource A local input source file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param pageOptions Page options for PDF documents.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      LocalInputSource localInputSource,
      PredictOptions predictOptions,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
      type,
      endpoint,
      getSplitFile(localInputSource, pageOptions),
      localInputSource.getFilename(),
      predictOptions,
      null
    );
  }

  /**
   * Send a remote file to a Generated prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param endpoint Custom endpoint to send the document to.
   * @param documentUrl A URL to a remote file.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      URL documentUrl
  ) throws IOException {
    InputSourceUtils.validateUrl(documentUrl);
    return this.parse(type, endpoint, null, null, null, documentUrl);
  }

  /**
   * Send a remote file to a Generated prediction API and parse the results.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param documentUrl A URL to a remote file.
   * @param predictOptions Prediction options for the enqueuing.
   * @param endpoint Custom endpoint to send the document to.
   * @return an instance of {@link PredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends GeneratedV1> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      URL documentUrl,
      PredictOptions predictOptions
  ) throws IOException {
    InputSourceUtils.validateUrl(documentUrl);
    return this.parse(type, endpoint, null, null, predictOptions, documentUrl);
  }

  /**
   * Parse a document from a Generated prediction API async queue.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param endpoint Custom endpoint to send the document to.
   * @param jobId ID of the job.
   * @return an instance of {@link AsyncPredictResponse}.
   */
  public <T extends GeneratedV1> AsyncPredictResponse<T> parseQueued(
      Class<T> type,
      Endpoint endpoint,
      String jobId
  ) {
    return this.mindeeApi.documentQueueGet(
      type,
      endpoint,
      jobId
    );
  }

  /**
   * Load a local prediction.
   * Typically used when wanting to load from a webhook callback.
   * However, any kind of Mindee response may be loaded.
   * @param <T> Type of inference.
   * @param type Type of inference.
   * @param localResponse A loaded local response.
   * @return an instance of {@link AsyncPredictResponse}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public <T extends Inference> AsyncPredictResponse<T> loadPrediction(
      Class<T> type,
      LocalResponse localResponse
  ) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();
    JavaType parametricType = objectMapper.getTypeFactory().constructParametricType(
        AsyncPredictResponse.class,
        type
    );
    return objectMapper.readValue(localResponse.getFile(), parametricType);
  }

  private byte[] getSplitFile(
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    byte[] splitFile;
    if (pageOptions == null || !localInputSource.isPdf()) {
      splitFile = localInputSource.getFile();
    } else {
      splitFile = pdfOperation.split(
          new SplitQuery(localInputSource.getFile(), pageOptions)
      ).getFile();
    }
    return splitFile;
  }

}
