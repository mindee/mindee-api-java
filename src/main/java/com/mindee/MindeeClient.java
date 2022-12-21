package com.mindee;

import com.mindee.parsing.MindeeApi;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;

import java.io.IOException;
import java.io.InputStream;

public class MindeeClient {

  private final MindeeApi mindeeApi;

  public MindeeClient() {
    this.mindeeApi = new MindeeApi();
  }

  public MindeeClient(String apiKey) {
    this.mindeeApi = new MindeeApi(apiKey);
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    InputStream fileStream,
    String fileName,
    boolean includeWords) throws IOException {

      return this.mindeeApi.predict(
        type,
        ParseParameter.builder()
          .fileStream(fileStream)
          .fileName(fileName)
          .includeWords(includeWords)
          .build());
  }
}
