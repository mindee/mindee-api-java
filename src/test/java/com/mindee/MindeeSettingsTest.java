package com.mindee;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

public class MindeeSettingsTest {

  @Test
  @SetEnvironmentVariable(key = "MINDEE_API_KEY", value = "abcd")
  @SetEnvironmentVariable(key = "MINDEE_API_URL", value = "https://example.com")
  void setEnvironmentVariablesAndEmptyParams() {
    MindeeSettings settings = new MindeeSettings("", "");
    Assertions.assertEquals(settings.getApiKey().orElse(""), "abcd");
    Assertions.assertEquals(settings.getBaseUrl(), "https://example.com");
  }
}
