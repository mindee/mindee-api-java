package com.mindee;

import com.mindee.http.CustomEndpoint;
import com.mindee.http.MindeeApi;
import com.mindee.http.RequestParameters;
import com.mindee.parsing.PageOptions;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.custom.CustomV1Inference;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitQuery;
import com.mindee.utils.FileUtils;
import com.mindee.utils.MindeeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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

  public LocalInputSource loadDocument(
      InputStream fileStream,
      String fileName
  ) throws IOException {
    return new LocalInputSource(fileStream, fileName);
  }

  public LocalInputSource loadDocument(
      byte[] fileAsByteArray,
      String filename
  ) {
    return new LocalInputSource(fileAsByteArray, filename);
  }

  public LocalInputSource loadDocument(File file) throws IOException {
    return new LocalInputSource(file);
  }

  public LocalInputSource loadDocument(
      String fileInBase64Code,
      String filename
  ) {
    return new LocalInputSource(fileInBase64Code, filename);
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
    validateUrl(sourceUrl);
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
    validateUrl(urlInputSource);
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
    validateUrl(documentUrl);
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

  private void validateUrl(URL documentUrl) {
    if (!"https".equalsIgnoreCase(documentUrl.getProtocol())) {
      throw new MindeeException("Only HTTPS source URLs are allowed");
    }
  }

  private byte[] getSplitFile(
      LocalInputSource localInputSource,
      PageOptions pageOptions
  ) throws IOException {
    byte[] splitFile;
    boolean isPDF = FileUtils.getFileExtension(localInputSource.getFilename())
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
