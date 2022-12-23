package com.mindee;

import com.mindee.parsing.MindeeApi;
import com.mindee.parsing.MindeeSettings;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MindeeClient {

  private final MindeeApi mindeeApi;

  public MindeeClient() {
    this.mindeeApi = new MindeeApi(new MindeeSettings());
  }

  public MindeeClient(MindeeSettings mindeeSettings) {
    this.mindeeApi = new MindeeApi(mindeeSettings);
  }

  public DocumentToParse loadDocument(InputStream fileStream,
                                      String fileName) {
    return new DocumentToParse(fileStream, fileName);
  }

  public DocumentToParse loadDocument(byte[] fileAsByteArray,
                                      String filename) {
    return new DocumentToParse(fileAsByteArray, filename);
  }

  public DocumentToParse loadDocument(File file) throws IOException {
    return new DocumentToParse(file);
  }

  public DocumentToParse loadDocument(String fileInBase64Code,
                                      String filename) {
    return new DocumentToParse(fileInBase64Code, filename);
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(documentToParse.getFile())
        .fileName(documentToParse.getFilename())
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    DocumentToParse documentToParse,
    boolean includeWords) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(documentToParse.getFile())
        .fileName(documentToParse.getFilename())
        .includeWords(includeWords)
        .build());
  }
}
