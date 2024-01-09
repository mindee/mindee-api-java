package com.mindee;

import com.mindee.http.Endpoint;
import com.mindee.http.MindeeApi;
import com.mindee.http.MindeeHttpApi;
import com.mindee.http.RequestParameters;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.product.custom.CustomV1;
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

  private void validateAsyncParams(AsyncPollingOptions pollingOptions) throws MindeeException {
    Double minimumInitialDelaySec = 1.0;
    Double minimumIntervalSec = 2.0;
    Integer minimumRetry = 2;
    if (pollingOptions.getInitialDelaySec() < minimumInitialDelaySec) {
      throw new MindeeException(String.format(
        "Cannot set initial delay to less than %.0f seconds", minimumInitialDelaySec
      ));
    }
    if (pollingOptions.getIntervalSec() < minimumIntervalSec) {
      throw new MindeeException(String.format(
        "Cannot set auto-poll delay to less than %.0f seconds", minimumIntervalSec
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
      parseResponse = parseQueued(type, jobId);
      if (parseResponse.getDocument().isPresent()) {
        return parseResponse;
      }
      retryCount++;
      Thread.sleep(intervalSec);
    }
    throw new RuntimeException("Max retries exceeded. Failed to get the document.");
  }

  /**
   * Send a local file to a Standard prediction API and parse the results.
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
