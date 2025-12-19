package com.mindee.input;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Setter;
import org.junit.jupiter.api.*;

public class URLInputSourceTest {

  private static final String TEST_URL = "https://example.com/testfile.pdf";
  private TestableURLInputSource urlInputSource;

  @BeforeEach
  public void setUp() {
    urlInputSource = new TestableURLInputSource(TEST_URL);
  }


  @AfterEach
  public void tearDown() {
    urlInputSource = null;
  }

  @Test
  void fetchFile_shouldSaveFileLocally() throws IOException {
    urlInputSource.fetchFile();

    File savedFile = new File(urlInputSource.getLocalFilename());
    assertTrue(savedFile.exists(), "The file should be saved locally");

    Files.deleteIfExists(savedFile.toPath());
  }

  @Test
  void fetchFile_shouldThrowIOException_onFailedFetch() {
    urlInputSource.setMockResponseCode(HttpURLConnection.HTTP_NOT_FOUND);

    IOException exception = assertThrows(IOException.class, urlInputSource::fetchFile);
    assertEquals("Failed to fetch file: 404", exception.getMessage());
  }

  @Test
  void fetchFile_shouldHandleRedirects() throws IOException {urlInputSource.setMockResponseCode(HttpURLConnection.HTTP_MOVED_TEMP);
    urlInputSource.setMockRedirectUrl("https://example.com/redirectedfile.pdf");

    urlInputSource.setMockResponseCode(HttpURLConnection.HTTP_OK);

    urlInputSource.fetchFile();

    File savedFile = new File(urlInputSource.getLocalFilename());
    assertTrue(savedFile.exists(), "The file should be saved after following redirects");

    Files.deleteIfExists(savedFile.toPath());
  }

  @Test
  void saveTo_shouldSaveToSpecifiedPath() throws IOException {
    String outputPath = "output_testfile.pdf";

    urlInputSource.saveToFile(outputPath);

    File savedFile = new File(outputPath);
    assertTrue(savedFile.exists(), "The file should be saved to the specified path");

    Files.deleteIfExists(Paths.get(outputPath));
    urlInputSource.cleanup();
  }

  @Test
  void toLocalInputSource_shouldCreateLocalInputSource() throws IOException {
    urlInputSource.fetchFile();

    LocalInputSource localInputSource = urlInputSource.toLocalInputSource();

    assertNotNull(localInputSource, "Should create a LocalInputSource from URLInputSource");

    urlInputSource.cleanup();
  }


  class TestableURLInputSource extends URLInputSource {

    @Setter
    private int mockResponseCode = HttpURLConnection.HTTP_OK;
    @Setter
    private String mockRedirectUrl;
    private boolean isRedirected = false;

    public TestableURLInputSource(String url) {
      super(builder(url));
    }


    @Override
    protected HttpURLConnection createConnection(String urlString) throws IOException {
      HttpURLConnection mockConnection = mock(HttpURLConnection.class);

      when(mockConnection.getResponseCode()).thenReturn(mockResponseCode);

      Path path = Paths.get("src/test/resources/file_types/pdf/multipage.pdf");
      if (isRedirected) {
        when(mockConnection.getHeaderField("Location")).thenReturn(null);
        when(mockConnection.getInputStream()).thenReturn(Files.newInputStream(path));
      } else {
        when(mockConnection.getHeaderField("Location")).thenReturn(mockRedirectUrl);
        when(mockConnection.getResponseCode()).thenReturn(HttpURLConnection.HTTP_MOVED_TEMP);
        isRedirected = true;
        return mockConnection;
      }

      when(mockConnection.getInputStream()).thenReturn(Files.newInputStream(path));
      return mockConnection;
    }
  }
}
