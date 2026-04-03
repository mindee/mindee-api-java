package com.mindee.v2;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.SetEnvironmentVariable;

public class MindeeSettingsTest {

  @Test
  @SetEnvironmentVariable(key = "MINDEE_V2_API_KEY", value = "abcd")
  @SetEnvironmentVariable(key = "MINDEE_V2_API_URL", value = "https://example.com")
  void setEnvironmentVariablesAndEmptyParams() {
    var settings = new MindeeSettings("", "");
    Assertions.assertEquals("abcd", settings.getApiKey().orElse(""));
    Assertions.assertEquals("https://example.com", settings.getBaseUrl());
  }
}
