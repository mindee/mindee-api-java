package com.mindee.parsing;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.Getter;
import org.apache.commons.codec.binary.Hex;

/**
 * A Mindee response saved locally.
 */
@Getter
public abstract class BaseLocalResponse {
  protected final byte[] file;

  /**
   * Load from a {@link String}.
   *
   * @param input Assumes UTF-8 encoding.
   */
  public BaseLocalResponse(String input) {
    if (input == null) {
      throw new IllegalArgumentException("Input string cannot be null.");
    }
    this.file = this.readToCleanUtf8Bytes(input.lines());
  }

  /**
   * Load from a byte array.
   *
   * @param input will be decoded as UTF-8.
   */
  public BaseLocalResponse(byte[] input) {
    if (input == null) {
      throw new IllegalArgumentException("Input byte array cannot be null.");
    }
    this.file = this.readToCleanUtf8Bytes(new String(input, StandardCharsets.UTF_8).lines());
  }

  /**
   * Load from an {@link InputStream}.
   * This method will not close the provided stream.
   *
   * @param input will be decoded as UTF-8.
   */
  public BaseLocalResponse(InputStream input) {
    if (input == null) {
      throw new IllegalArgumentException("Input stream cannot be null.");
    }
    this.file = this
      .readToCleanUtf8Bytes(
        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8)).lines()
      );
  }

  /**
   * Load from a {@link File}.
   *
   * @param input will be decoded as UTF-8.
   */
  public BaseLocalResponse(File input) throws IOException {
    if (input == null) {
      throw new IllegalArgumentException("Input file cannot be null.");
    }
    try (var lines = Files.lines(input.toPath(), StandardCharsets.UTF_8)) {
      this.file = this.readToCleanUtf8Bytes(lines);
    }
  }

  /**
   * Load from a {@link Path}.
   *
   * @param input will be decoded as UTF-8.
   */
  public BaseLocalResponse(Path input) throws IOException {
    if (input == null) {
      throw new IllegalArgumentException("Input path cannot be null.");
    }
    try (var lines = Files.lines(input, StandardCharsets.UTF_8)) {
      this.file = this.readToCleanUtf8Bytes(lines);
    }
  }

  private byte[] readToCleanUtf8Bytes(Stream<String> stream) {
    var cleanedString = stream.collect(Collectors.joining(""));
    if (cleanedString.trim().isEmpty()) {
      throw new IllegalArgumentException("Input cannot be empty or contain only whitespace.");
    }
    return cleanedString.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Get the HMAC signature of the payload.
   *
   * @param secretKey Your secret key from the Mindee platform.
   * @return The generated HMAC signature.
   */
  public String getHmacSignature(String secretKey) {
    String algorithm = "HmacSHA256";
    SecretKeySpec secretKeySpec = new SecretKeySpec(
      secretKey.getBytes(StandardCharsets.UTF_8),
      algorithm
    );
    Mac mac;
    try {
      mac = Mac.getInstance(algorithm);
    } catch (NoSuchAlgorithmException err) {
      // this should never happen as the algorithm is hard-coded.
      return "";
    }
    try {
      mac.init(secretKeySpec);
    } catch (InvalidKeyException err) {
      return "";
    }
    return Hex.encodeHexString(mac.doFinal(this.file));
  }

  /**
   * Verify that the payload's signature matches the one received from the server.
   *
   * @param secretKey Your secret key from the Mindee platform.
   * @param signature The signature from the "X-Signature" HTTP header.
   * @return true if the signatures match.
   */
  public boolean isValidHmacSignature(String secretKey, String signature) {
    if (signature == null || secretKey == null) {
      return false;
    }

    String expectedSignature = getHmacSignature(secretKey);
    if (expectedSignature.isEmpty()) {
      return false;
    }

    byte[] expectedBytes = expectedSignature.getBytes(StandardCharsets.UTF_8);
    byte[] actualBytes = signature
      .toLowerCase(java.util.Locale.ROOT)
      .getBytes(StandardCharsets.UTF_8);

    return MessageDigest.isEqual(expectedBytes, actualBytes);
  }

  /**
   * Print the file as a UTF-8 string.
   */
  @Override
  public String toString() {
    return new String(this.file, StandardCharsets.UTF_8);
  }
}
