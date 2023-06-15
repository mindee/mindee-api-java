package com.mindee;

import com.mindee.http.CustomEndpoint;
import com.mindee.http.MindeeApi;
import com.mindee.http.MindeeHttpApi;
import com.mindee.http.RequestParameters;
import com.mindee.input.InputSourceUtils;
import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.pdf.PdfBoxApi;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.product.custom.CustomV1Inference;
import java.io.IOException;
import java.net.URL;

/**
 * Main entrypoint for Mindee operations.
 */
public class MindeeClient {

  private final MindeeApi mindeeApi;
  private final PdfOperation pdfOperation;

  public MindeeClient(PdfOperation pdfOperation, MindeeApi mindeeApi) {
    this.pdfOperation = pdfOperation;
    this.mindeeApi = mindeeApi;
  }
  /**
   * Create a default MindeeClient.
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
   * Create a default MindeeApi.
   *
   * @param apiKey The api key to use.
   */
  public static MindeeApi createDefaultApi(String apiKey) {

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

  public <T extends Inference> PredictResponse<T> parseQueued(Class<T> type, String jobId) {
    return this.mindeeApi.checkJobStatus(type, jobId);
  }

  public <T extends Inference> PredictResponse<T> enqueue(Class<T> type, LocalInputSource localInputSource)
      throws IOException {
    return this.enqueue(type, localInputSource.getFile(), localInputSource.getFilename(),
        Boolean.FALSE, null);
  }

  public <T extends Inference> PredictResponse<T> enqueue(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords,
      PageOptions pageOptions
  ) throws IOException {
    return this.enqueue(
        type,
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        allWords,
      null
    );
  }

  public <T extends Inference> PredictResponse<T> enqueue(
      Class<T> type,
      URL sourceUrl
  ) throws IOException {
    InputSourceUtils.validateUrl(sourceUrl);
    return this.enqueue(type, null,
        null, Boolean.FALSE, sourceUrl);
  }

  private <T extends Inference> PredictResponse<T> enqueue(
      Class<T> type,
      byte[] file,
      String filename,
      boolean allWords,
      URL urlInputSource
  ) throws IOException {
    return this.mindeeApi.predict(
            type,
            RequestParameters.builder()
                .file(file)
                .fileName(filename)
                .allWords(allWords)
                .urlInputSource(urlInputSource)
                .asyncCall(Boolean.TRUE)
                .build());
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      LocalInputSource localInputSource
  ) throws IOException {
    return this.parse(type, localInputSource.getFile(), localInputSource.getFilename(), false, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords
  ) throws IOException {
    return this.parse(type, localInputSource.getFile(), localInputSource.getFilename(), allWords,
        null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(type, getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(), false, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      LocalInputSource localInputSource,
      boolean allWords,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(type, getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(), allWords, null);
  }

  public <T extends Inference> Document<T> parse(
      Class<T> type,
      URL urlInputSource
  ) throws IOException {
    InputSourceUtils.validateUrl(urlInputSource);
    return this.parse(type, null, null, false, urlInputSource);
  }

  private <T extends Inference> Document<T> parse(
      Class<T> type,
      byte[] file,
      String filename,
      boolean allWords,
      URL urlInputSource
  ) throws IOException {
    return this.mindeeApi.predict(
            type,
            RequestParameters.builder()
                .file(file)
                .fileName(filename)
                .allWords(allWords)
                .urlInputSource(urlInputSource)
                .build()).getDocument()
        .orElseThrow(() -> new MindeeException("No Document Returned by endpoint"));
  }

  public Document<CustomV1Inference> parse(
      LocalInputSource localInputSource,
      CustomEndpoint customEndpoint
  ) throws IOException {
    return this.parse(
        localInputSource.getFile(),
        localInputSource.getFilename(),
        customEndpoint,
        null);
  }

  public Document<CustomV1Inference> parse(
      URL documentUrl,
      CustomEndpoint customEndpoint
  ) throws IOException {
    InputSourceUtils.validateUrl(documentUrl);
    return this.parse(null, null, customEndpoint, documentUrl);
  }

  public Document<CustomV1Inference> parse(
      LocalInputSource localInputSource,
      CustomEndpoint customEndpoint,
      PageOptions pageOptions
  ) throws IOException {
    return this.parse(
        getSplitFile(localInputSource, pageOptions),
        localInputSource.getFilename(),
        customEndpoint, null
    );
  }

  private Document<CustomV1Inference> parse(
      byte[] file,
      String filename,
      CustomEndpoint customEndpoint,
      URL urlInputSource
  ) throws IOException {
    return this.mindeeApi.predict(
            CustomV1Inference.class,
            customEndpoint,
            RequestParameters.builder()
                .file(file)
                .fileName(filename)
                .urlInputSource(urlInputSource)
                .build()).getDocument()
        .orElseThrow(() -> new MindeeException("No Document Returned by endpoint"));
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
