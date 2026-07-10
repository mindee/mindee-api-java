package com.mindee.input;

import com.mindee.MindeeException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Locale;
import lombok.Getter;

/**
 * Input source wrapper to load remote files locally.
 */
public class URLInputSource {
  @Getter
  private final URL url;
  private final String username;
  private final String password;
  @Getter
  private String localFilename;
  private final String token;

  /**
   * Private constructor.
   */
  URLInputSource(Builder builder) {
    this.url = builder.url;
    this.username = builder.username;
    this.password = builder.password;
    this.token = builder.token;
    this.localFilename = builder.localFilename;
  }

  /**
   * Creates a new builder for an URLInputSource.
   *
   * @param url URL to fetch the file from.
   * @return An instance of {@link URLInputSource}.
   */
  public static Builder builder(String url) throws MalformedURLException {
    return new Builder(new URL(url));
  }

  public static Builder builder(URL url) {
    return new Builder(url);
  }

  /**
   * Ensures the URL can be safely sent to the Mindee server.
   *
   * <p>
   * Rejects any URL that could be used for Server-Side Request Forgery (SSRF):
   * <ul>
   * <li>non-HTTPS schemes,</li>
   * <li>embedded userinfo (e.g. {@code https://user:pass@host}),</li>
   * <li>loopback hostnames ({@code localhost}, {@code *.localhost}),</li>
   * <li>hosts that resolve to loopback, link-local, site-local (RFC 1918),
   * any-local ({@code 0.0.0.0}), multicast, IPv6 unique-local
   * ({@code fc00::/7}) or carrier-grade NAT ({@code 100.64.0.0/10})
   * addresses.</li>
   * </ul>
   */
  public void validateSecure() {
    if (!"https".equalsIgnoreCase(this.url.getProtocol())) {
      throw new MindeeException("Only HTTPS source URLs are allowed");
    }
    String userInfo = this.url.getUserInfo();
    if (userInfo != null && !userInfo.isEmpty()) {
      throw new MindeeException("Source URLs must not embed user credentials");
    }
    String host = this.url.getHost();
    if (host == null || host.isEmpty()) {
      throw new MindeeException("Source URL is missing a host");
    }
    String lowerHost = host.toLowerCase(Locale.ROOT);
    if (
      "localhost".equals(lowerHost)
        || lowerHost.endsWith(".localhost")
        || "ip6-localhost".equals(lowerHost)
        || "ip6-loopback".equals(lowerHost)
    ) {
      throw new MindeeException("Loopback hostnames are not allowed: " + host);
    }

    InetAddress[] addresses;
    try {
      addresses = InetAddress.getAllByName(host);
    } catch (UnknownHostException e) {
      throw new MindeeException("Unable to resolve source URL host: " + host, e);
    }
    for (InetAddress addr : addresses) {
      if (isDisallowedAddress(addr)) {
        throw new MindeeException(
          "Source URL host resolves to a disallowed address: " + addr.getHostAddress()
        );
      }
    }
  }

  private static boolean isDisallowedAddress(InetAddress addr) {
    return addr.isLoopbackAddress()
      || addr.isLinkLocalAddress()
      || addr.isSiteLocalAddress()
      || addr.isAnyLocalAddress()
      || addr.isMulticastAddress()
      || isUniqueLocalIpv6(addr)
      || isCarrierGradeNat(addr);
  }

  private static boolean isUniqueLocalIpv6(InetAddress addr) {
    if (!(addr instanceof Inet6Address)) {
      return false;
    }
    byte[] raw = addr.getAddress();
    return (raw[0] & 0xFE) == 0xFC;
  }

  private static boolean isCarrierGradeNat(InetAddress addr) {
    if (!(addr instanceof Inet4Address)) {
      return false;
    }
    byte[] raw = addr.getAddress();
    return (raw[0] & 0xFF) == 100 && (raw[1] & 0xC0) == 0x40;
  }

  /**
   * Fetches the file from a remote source.
   *
   * @throws IOException Throws if the file can't be fetched.
   */
  public void fetchFile() throws IOException {
    HttpURLConnection connection = prepareConnection();

    try (InputStream in = connection.getInputStream()) {
      saveTempFile(in);
    }
  }

  private HttpURLConnection prepareConnection() throws IOException {
    HttpURLConnection connection = createConnection(url);
    connection = handleRedirects(connection);

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("Failed to fetch file: " + responseCode);
    }

    return connection;
  }

  protected HttpURLConnection createConnection(URL url) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setInstanceFollowRedirects(true);

    if (username != null && password != null) {
      String encodedCredentials = Base64
        .getEncoder()
        .encodeToString((username + ":" + password).getBytes());
      connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
    }
    if (token != null) {
      connection.setRequestProperty("Authorization", "Bearer " + token);
    }
    connection.setRequestMethod("GET");
    return connection;
  }

  private HttpURLConnection handleRedirects(HttpURLConnection connection) throws IOException {
    int status = connection.getResponseCode();
    if (
      status == HttpURLConnection.HTTP_MOVED_TEMP
        || status == HttpURLConnection.HTTP_MOVED_PERM
        || status == HttpURLConnection.HTTP_SEE_OTHER
        || status == 307
        || status == 308
    ) {
      String newUrl = connection.getHeaderField("Location");
      connection.disconnect();

      HttpURLConnection newConnection = createConnection(new URL(newUrl));
      return handleRedirects(newConnection); // Recursive call to handle multiple redirects
    }
    return connection;
  }

  private void saveTempFile(InputStream in) throws IOException {
    String prefix = generateDefaultFilename();

    Path tempFile = Files.createTempFile(prefix, ".tmp");
    localFilename = tempFile.toString();

    try (
        InputStream inputStream = in;
        OutputStream outputStream = Files.newOutputStream(tempFile)
    ) {
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }
  }

  private void saveFile(InputStream in, String filepath) throws IOException {
    File outputFile = new File(filepath);

    try (FileOutputStream out = new FileOutputStream(outputFile)) {
      byte[] buffer = new byte[4096];
      int bytesRead;
      while ((bytesRead = in.read(buffer)) != -1) {
        out.write(buffer, 0, bytesRead);
      }
    }
  }

  /**
   * Create a LocalInputSource instance from this object.
   *
   * @return An instance of a {@link LocalInputSource}.
   * @throws IOException Throws if the file can't be accessed.
   */
  public LocalInputSource toLocalInputSource() throws IOException {
    File file = new File(localFilename);
    return new LocalInputSource(file);
  }

  private String generateDefaultFilename() {
    return "mindee_temp_"
      + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
  }

  /**
   * Fetches the file from the URL and saves it to the specified filepath.
   *
   * @param filepath The local path where the file should be saved.
   * @throws IOException If there's an error fetching or saving the file.
   */
  public void saveToFile(String filepath) throws IOException {
    HttpURLConnection connection = prepareConnection();

    try (InputStream in = connection.getInputStream()) {
      File file = new File(filepath);
      saveFile(in, filepath);
      this.localFilename = file.getName();
    }
  }

  public void cleanup() {

    File fileToDelete = new File(this.localFilename);

    if (fileToDelete.exists()) {
      boolean deleted = fileToDelete.delete();
      if (!deleted) {
        System.err.println("Failed to delete file: " + this.localFilename);
      } else {
        System.out.println("Successfully deleted file: " + this.localFilename);
      }
    } else {
      System.out.println("No file found to delete: " + this.localFilename);
    }
  }

  /**
   * Builder class for an URLInputSource.
   */
  public static class Builder {
    private final URL url;
    private String username;
    private String password;
    private String localFilename;
    private String token;

    /**
     * String constructor.
     *
     * @param url Remote URL resource.
     */
    public Builder(String url) throws MalformedURLException {
      this.url = new URL(url);
    }

    /**
     * URL constructor.
     *
     * @param url Remote URL resource.
     */
    public Builder(URL url) {
      this.url = url;
    }

    /**
     * Builder method to set the token for remote access.
     *
     * @param token Token for remote access requiring an authentication Token.
     * @return An instance of the builder.
     */
    public Builder withToken(String token) {
      this.token = token;
      return this;
    }

    /**
     * Builder method to set the username and password for remote authentication.
     *
     * @param username Username for remote authentication.
     * @param password Password for remote authentication.
     * @return An instance of the builder.
     */
    public Builder withCredentials(String username, String password) {
      this.username = username;
      this.password = password;
      return this;
    }

    /**
     * Builder method to set the local filename for the downloaded file.
     *
     * @param filename Filename to give to the file.
     * @return An instance of the builder.
     */
    public Builder withLocalFilename(String filename) {
      this.localFilename = filename;
      return this;
    }

    /**
     * Build the {@link URLInputSource} object.
     *
     * @return A valid {@link URLInputSource} object.
     */
    public URLInputSource build() {
      return new URLInputSource(this);
    }
  }
}
