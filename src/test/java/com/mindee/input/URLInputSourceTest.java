package com.mindee.input;

import static org.junit.jupiter.api.Assertions.*;

import com.mindee.MindeeException;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.Setter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Test;

public class URLInputSourceTest {

  private static final String TEST_URL = "https://example.com/testfile.pdf";
  private TestableURLInputSource urlInputSource;

  @BeforeEach
  public void setUp() throws MalformedURLException {
    urlInputSource = new TestableURLInputSource(TEST_URL);
  }

  @AfterEach
  public void tearDown() {
    urlInputSource = null;
  }

  @Test
  void fetchFile_shouldSaveFileLocally() throws IOException {
    urlInputSource.fetchFile();

    var savedFile = new File(urlInputSource.getLocalFilename());
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
  void fetchFile_shouldHandleRedirects() throws IOException {
    urlInputSource.setMockResponseCode(HttpURLConnection.HTTP_OK);
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

  @Nested
  @DisplayName("validateSecure() – SSRF/loopback checks")
  class ValidateSecure {
    @Test
    void httpsPublicHost_isAccepted() throws MalformedURLException {
      URLInputSource.builder("https://example.com/file.pdf").build().validateSecure();
    }

    @Test
    void httpScheme_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("http://example.com/file.pdf").build();
      MindeeException e = assertThrows(MindeeException.class, src::validateSecure);
      assertTrue(e.getMessage().contains("HTTPS"));
    }

    @Test
    void userInfo_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://user:pass@example.com/file.pdf").build();
      MindeeException e = assertThrows(MindeeException.class, src::validateSecure);
      assertTrue(e.getMessage().contains("credentials"));
    }

    @Test
    void loopbackHostname_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://localhost/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void subLocalhostHostname_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://foo.localhost/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void loopbackIpv4_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://127.0.0.1/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void loopbackIpv6_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://[::1]/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void anyLocalIpv4_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://0.0.0.0/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void privateRfc1918_isRejected() throws MalformedURLException {
      for (String host : new String[] { "10.0.0.1", "172.16.0.1", "192.168.1.1" }) {
        var src = URLInputSource.builder("https://" + host + "/file.pdf").build();
        assertThrows(MindeeException.class, src::validateSecure, "expected rejection for " + host);
      }
    }

    @Test
    void linkLocalIpv4_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://169.254.169.254/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void cgnat_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://100.64.0.1/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void uniqueLocalIpv6_isRejected() throws MalformedURLException {
      var src = URLInputSource.builder("https://[fd00::1]/file.pdf").build();
      assertThrows(MindeeException.class, src::validateSecure);
    }

    @Test
    void unresolvableHost_isRejected() throws MalformedURLException {
      var src = URLInputSource
        .builder("https://this-host-should-not-exist.invalid/file.pdf")
        .build();
      assertThrows(MindeeException.class, src::validateSecure);
    }
  }

  static class TestableURLInputSource extends URLInputSource {

    @Setter
    private int mockResponseCode = HttpURLConnection.HTTP_OK;
    @Setter
    private String mockRedirectUrl;
    private boolean isRedirected = false;

    public TestableURLInputSource(String url) throws MalformedURLException {
      super(builder(url));
    }

    @Override
    protected HttpURLConnection createConnection(URL url) {
      boolean wasRedirected = isRedirected;

      if (!isRedirected && mockRedirectUrl != null) {
        isRedirected = true;
      }

      return new HttpURLConnection(url) {
        @Override
        public void disconnect() {
        }

        @Override
        public boolean usingProxy() {
          return false;
        }

        @Override
        public void connect() {
        }

        @Override
        public int getResponseCode() {
          if (mockRedirectUrl != null && !wasRedirected) {
            return HttpURLConnection.HTTP_MOVED_TEMP;
          }
          return mockResponseCode;
        }

        @Override
        public String getHeaderField(String name) {
          if ("Location".equalsIgnoreCase(name)) {
            if (mockRedirectUrl != null && !wasRedirected) {
              return mockRedirectUrl;
            }
          }
          return null;
        }

        @Override
        public java.io.InputStream getInputStream() throws IOException {
          Path path = Paths.get("src/test/resources/file_types/pdf/multipage.pdf");
          return Files.newInputStream(path);
        }
      };
    }
  }
}
