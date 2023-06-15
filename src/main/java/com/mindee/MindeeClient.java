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
   * @param pdfOperation
   * @param mindeeApi
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

  public <T extends Inference> AsyncPredictResponse<T> parseQueued(Class<T> type, String jobId) {
    return this.mindeeApi.documentQueueGet(
        type,
        new Endpoint(type),
        jobId
    );
  }

  public <T extends Inference> AsyncPredictResponse<T> enqueue(Class<T> type, LocalInputSource localInputSource)
      throws IOException {
    return this.enqueue(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        Boolean.FALSE,
        null);
  }

  public <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords,
      PageOptions pageOptions
  ) throws IOException {
    return this.enqueue(
        type,
        new Endpoint(type),
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        allWords,
      null
    );
  }

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
        Boolean.FALSE,
        sourceUrl
    );
  }

  private <T extends Inference> AsyncPredictResponse<T> enqueue(
      Class<T> type,
      Endpoint endpoint,
      byte[] file,
      String filename,
      boolean allWords,
      URL urlInputSource
  ) throws IOException {
    RequestParameters params = RequestParameters.builder()
        .file(file)
        .fileName(filename)
        .allWords(allWords)
        .urlInputSource(urlInputSource)
        .build();
    return this.mindeeApi.predictAsyncPost(type, endpoint, params);
  }

  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        false,
        null
    );
  }

  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        localInputSource.getFile(),
        localInputSource.getFilename(),
        allWords,
        null
    );
  }

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
        false,
        null
    );
  }

  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
        type,
        new Endpoint(type),
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        allWords,
        null
    );
  }

  public <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      URL urlInputSource
  ) throws IOException {
    InputSourceUtils.validateUrl(urlInputSource);
    return this.parse(type, new Endpoint(type),null, null, false, urlInputSource);
  }

  private <T extends Inference> PredictResponse<T> parse(
      Class<T> type,
      Endpoint endpoint,
      byte[] file,
      String filename,
      boolean allWords,
      URL urlInputSource
  ) throws IOException {
    RequestParameters params = RequestParameters.builder()
        .file(file)
        .fileName(filename)
        .allWords(allWords)
        .urlInputSource(urlInputSource)
        .build();
    return this.mindeeApi.predictPost(type, endpoint, params);
  }

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

  public PredictResponse<CustomV1> parse(
      URL documentUrl,
      Endpoint endpoint
  ) throws IOException {
    InputSourceUtils.validateUrl(documentUrl);
    return this.parse(null, null, endpoint, documentUrl);
  }

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
    boolean isPDF = InputSourceUtils.getFileExtension(localInputSource.getFilename())
        .equalsIgnoreCase("pdf");
    if (pageOptions == null || !isPDF) {
      splitFile = localInputSource.getFile();
    } else {
      splitFile = pdfOperation.split(
          new SplitQuery(localInputSource.getFile(), pageOptions)).getFile();
    }
    return splitFile;
  }

}
