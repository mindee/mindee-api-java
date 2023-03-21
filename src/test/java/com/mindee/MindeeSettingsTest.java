package com.mindee;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.ClearEnvironmentVariable;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

public class MindeeSettingsTest extends TestCase {

  @Test
  @ClearEnvironmentVariable(key = "MINDEE_API_KEY")
  @ClearEnvironmentVariable(key = "MINDEE_API_URL")
  void clearedEnvironmentVariablesAndEmptyParams() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> new MindeeSettings("", ""));
  }

  @Test
  @SetEnvironmentVariable(key = "MINDEE_API_KEY", value = "abcd")
  @SetEnvironmentVariable(key = "MINDEE_API_URL", value = "https://example.com")
  void setEnvironmentVariablesAndEmptyParams() {
    MindeeSettings settings = new MindeeSettings("", "");
    Assertions.assertEquals(settings.getApiKey(), "abcd");
    Assertions.assertEquals(settings.getBaseUrl(), "https://example.com");
  }
}
