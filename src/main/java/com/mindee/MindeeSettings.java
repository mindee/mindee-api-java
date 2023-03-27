package com.mindee;

import lombok.Getter;

/**
 * Mindee API configuration.
 */
@Getter
public class MindeeSettings {

  private static final String DEFAULT_MINDEE_API_URL = "https://api.mindee.net/v1";
  private final String apiKey;
  private final String baseUrl;

  public MindeeSettings() {
    this("", "");
  }

  public MindeeSettings(String apiKey) {
    this(apiKey, "");
  }

  public MindeeSettings(String apiKey, String baseUrl) {

    if (apiKey == null || apiKey.trim().isEmpty()) {
      String apiKeyFromEnv = System.getenv("MINDEE_API_KEY");
      if (apiKeyFromEnv == null || apiKeyFromEnv.trim().isEmpty()) {
        throw new IllegalArgumentException("API KEY is null or empty.");
      }
      this.apiKey = apiKeyFromEnv;
    } else {
      this.apiKey = apiKey;
    }

    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      String baseUrlFromEnv = System.getenv("MINDEE_API_URL");
      if (baseUrlFromEnv != null && !baseUrlFromEnv.trim().isEmpty()) {
        this.baseUrl = baseUrlFromEnv;
      } else {
        this.baseUrl = DEFAULT_MINDEE_API_URL;
      }
    } else {
      this.baseUrl = baseUrl;
    }
  }
}
