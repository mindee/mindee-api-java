package com.mindee;

import com.mindee.parsing.MindeeApi;
import com.mindee.parsing.MindeeSettings;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Inference;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Base64;

public class MindeeClient {

  private final MindeeApi mindeeApi;

  public MindeeClient() {
    this.mindeeApi = new MindeeApi(new MindeeSettings());
  }

  public MindeeClient(MindeeSettings mindeeSettings) {
    this.mindeeApi = new MindeeApi(mindeeSettings);
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

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    InputStream fileStream,
    String fileName) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(fileStream)
        .fileName(fileName)
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    byte[] fileAsByteArray,
    String filename) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(new ByteArrayInputStream(fileAsByteArray))
        .fileName(filename)
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    byte[] fileAsByteArray,
    String filename,
    boolean includeWords) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(new ByteArrayInputStream(fileAsByteArray))
        .fileName(filename)
        .includeWords(includeWords)
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    File file,
    boolean includeWords) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(Files.newInputStream(file.toPath()))
        .fileName(file.getName())
        .includeWords(includeWords)
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    File file) throws IOException {

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(Files.newInputStream(file.toPath()))
        .fileName(file.getName())
        .build());
  }

  public <T extends Inference> Document<T> parse(
    Class<T> type,
    String fileInBase64Code,
    String filename) throws IOException {

    byte[] fileRead = Base64.getDecoder().decode(fileInBase64Code.getBytes());

    return this.mindeeApi.predict(
      type,
      ParseParameter.builder()
        .fileStream(new ByteArrayInputStream(fileRead))
        .fileName(filename)
        .build());
  }
}
