package com.mindee.v2.parsing;

import static com.mindee.TestingUtilities.getV2ProductPath;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.MindeeException;
import com.mindee.v2.product.extraction.ExtractionResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 – Load Local Response")
public class LocalResponseTest {
  private static final String SIGNATURE = "e51bdf80f1a08ed44ee161100fc30a25cb35b4ede671b0a575dc9064a3f5dbf1";
  private static final String DUMMY_SECRET_KEY = "ogNjY44MhvKPGTtVsI8zG82JqWQa68woYQH";
  private static final String FILE_PATH = "extraction/standard_field_types.json";

  private static void assertLocalResponse(LocalResponse localResponse, String fileContent) {
    assertEquals(SIGNATURE, localResponse.getHmacSignature(DUMMY_SECRET_KEY));

    assertFalse(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, "invalid signature"));
    assertFalse(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, null));
    assertFalse(localResponse.isValidHmacSignature(null, SIGNATURE));
    assertFalse(localResponse.isValidHmacSignature(null, null));
    assertFalse(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, ""));
    assertTrue(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, SIGNATURE));
    assertTrue(localResponse.isValidHmacSignature(DUMMY_SECRET_KEY, SIGNATURE.toUpperCase()));

    ExtractionResponse response = localResponse.deserializeResponse(ExtractionResponse.class);

    assertNotNull(response);
    assertNotNull(response.getInference());

    assertEquals("test-model-id", response.getInference().getModel().getId());
    assertEquals(
      "field_simple_string-value",
      response
        .getInference()
        .getResult()
        .getFields()
        .getSimpleField("field_simple_string")
        .getStringValue()
    );

    assertEquals(fileContent.replace("\r", "").replace("\n", ""), localResponse.toString());
  }

  @Test
  @DisplayName("should load a response from a JSON string")
  void validString_mustLoadValidLocalResponse() throws IOException {
    var fileContent = Files.readString(getV2ProductPath(FILE_PATH));
    var localResponse = new LocalResponse(fileContent);
    assertLocalResponse(localResponse, fileContent);
  }

  @Test
  @DisplayName("should load a response from a buffer")
  void validBuffer_mustLoadValidLocalResponse() throws IOException {
    var filePath = getV2ProductPath(FILE_PATH);
    var localResponse = new LocalResponse(Files.readAllBytes(filePath));
    assertLocalResponse(localResponse, Files.readString(filePath));
  }

  @Test
  @DisplayName("should load a response from a JSON file path")
  void validPath_mustLoadValidLocalResponse() throws IOException {
    var filePath = getV2ProductPath(FILE_PATH);
    var localResponse = new LocalResponse(filePath);
    assertLocalResponse(localResponse, Files.readString(filePath));
  }

  @Test
  @DisplayName("should load a response from a JSON file")
  void validFile_mustLoadValidLocalResponse() throws IOException {
    var filePath = getV2ProductPath(FILE_PATH);
    var localResponse = new LocalResponse(new File(filePath.toString()));
    assertLocalResponse(localResponse, Files.readString(filePath));
  }

  @Test
  @DisplayName("should load a response from a stream")
  void validStream_mustLoadValidLocalResponse() throws IOException {
    var file = new File(getV2ProductPath(FILE_PATH).toString());

    try (var stream = new BufferedInputStream((new FileInputStream(file)))) {
      // Required for the `reset()` later
      stream.mark((int) file.length() + 1024);

      var localResponse = new LocalResponse(stream);
      assertLocalResponse(localResponse, Files.readString(file.toPath()));

      // Explicitly verify the stream is not closed by the LocalResponse constructor
      stream.reset();
      assertNotEquals(-1, stream.read());
    }
  }

  @Test
  @DisplayName("should raise an exception when given an invalid JSON string")
  void invalidString_mustRaiseException() {
    var localResponse = new LocalResponse("{invalid json");
    var err = assertThrows(
      MindeeException.class,
      () -> localResponse.deserializeResponse(ExtractionResponse.class)
    );
    assertEquals("Invalid JSON payload.", err.getMessage());
  }

  @Test
  @DisplayName("should raise an exception when given an empty value")
  void emptyValue_mustRaiseException() {
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse(""));
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse(new byte[0]));
    assertThrows(
      IllegalArgumentException.class,
      () -> new LocalResponse(InputStream.nullInputStream())
    );
  }

  @Test
  @DisplayName("should raise an exception when given a null value")
  void nullValue_mustRaiseException() {
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse((String) null));
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse((byte[]) null));
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse((InputStream) null));
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse((File) null));
    assertThrows(IllegalArgumentException.class, () -> new LocalResponse((Path) null));
  }
}
