package com.mindee.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.InvalidKeyException;
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
public class LocalResponse {
  private final byte[] file;

  /**
   * Load from an {@link InputStream}.
   * @param input will be decoded as UTF-8.
   */
  public LocalResponse(InputStream input) {
    this.file = this.getBytes(
        new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))
            .lines()
    );
  }

  /**
   * Load from a {@link String}.
   * @param input will be decoded as UTF-8.
   */
  public LocalResponse(String input) {
    this.file = input.getBytes(StandardCharsets.UTF_8);
  }

  /**
   * Load from a {@link File}.
   * @param input will be decoded as UTF-8.
   */
  public LocalResponse(File input) throws IOException {
    this.file = this.getBytes(
        Files.lines(input.toPath(), StandardCharsets.UTF_8)
    );
  }

  private byte[] getBytes(Stream<String> stream) {
    return stream.collect(Collectors.joining("")).getBytes();
  }

  /**
   * Get the HMAC signature of the payload.
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
   * @param secretKey Your secret key from the Mindee platform.
   * @param signature The signature from the "X-Mindee-Hmac-Signature" HTTP header.
   * @return true if the signatures match.
   */
  public boolean isValidHmacSignature(String secretKey, String signature) {
    return signature.equals(getHmacSignature(secretKey));
  }
}
