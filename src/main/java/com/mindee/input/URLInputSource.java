package com.mindee.input;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;

/**
 * Input source wrapper to load remote files locally.
 */
public class URLInputSource {
  private final String url;
  private final String username;
  private final String password;
  private String localFilename;
  private final String token;

  URLInputSource(Builder builder) {
    this.url = builder.url;
    this.username = builder.username;
    this.password = builder.password;
    this.token = builder.token;
    this.localFilename = builder.localFilename;
  }

  /**
   * @param url URL to fetch the file from.
   * @return An instance of {@link URLInputSource}.
   */
  public static Builder builder(String url) {
    return new Builder(url);
  }

  /**
   * @throws IOException Throws if the file can't be fetched.
   */
  public void fetchFile() throws IOException {
    HttpURLConnection connection = createConnection(url);
    connection = handleRedirects(connection);

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("Failed to fetch file: " + responseCode);
    }

    try (InputStream in = connection.getInputStream()) {
      saveTempFile(in);
    }
  }

  protected HttpURLConnection createConnection(String urlString) throws IOException {
    HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
    connection.setInstanceFollowRedirects(true);

    if (username != null && password != null) {
      String encodedCredentials =
          Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
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
    if (status == HttpURLConnection.HTTP_MOVED_TEMP
        || status == HttpURLConnection.HTTP_MOVED_PERM
        || status == HttpURLConnection.HTTP_SEE_OTHER
        || status == 307
        || status == 308
    ) {
      String newUrl = connection.getHeaderField("Location");
      connection.disconnect();

      HttpURLConnection newConnection = createConnection(newUrl);
      return handleRedirects(newConnection);  // Recursive call to handle multiple redirects
    }
    return connection;
  }

  private void saveTempFile(InputStream in) throws IOException {
    String filename = generateDefaultFilename();

    Path tempFile = Files.createTempFile(filename, ".tmp");
    localFilename = tempFile.toString();

    try (InputStream inputStream = in;
         OutputStream outputStream = Files.newOutputStream(tempFile)) {
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
    return "mindee_temp_" +
        LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
  }

  /**
   * Fetches the file from the URL and saves it to the specified filepath.
   *
   * @param filepath The local path where the file should be saved.
   * @throws IOException If there's an error fetching or saving the file.
   */
  public void saveTo(String filepath) throws IOException {
    HttpURLConnection connection = createConnection(url);
    connection = handleRedirects(connection);

    int responseCode = connection.getResponseCode();
    if (responseCode != HttpURLConnection.HTTP_OK) {
      throw new IOException("Failed to fetch file: " + responseCode);
    }

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
    private final String url;
    private String username;
    private String password;
    private String localFilename;
    private String token;

    /**
     * @param url Remote URL resource.
     */
    public Builder(String url) {
      this.url = url;
    }

    /**
     * @param token Token for remote access requiring an authentication Token.
     * @return An instance of the builder.
     */
    public Builder withToken(String token) {
      this.token = token;
      return this;
    }

    /**
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
     * @param filename Filename to give to the file.
     * @return An instance of the builder.
     */
    public Builder withLocalFilename(String filename) {
      this.localFilename = filename;
      return this;
    }


    /**
     * @return A valid {@link URLInputSource} object.
     */
    public URLInputSource build() {
      return new URLInputSource(this);
    }
  }
}
