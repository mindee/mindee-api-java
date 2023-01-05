package com.mindee;

import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MindeeSettingsTest extends TestCase {

  @Test
  void givenAClientWithMissingConfigured_whenParsed_thenShouldThrowException() {
    Assertions.assertThrows(
      IllegalArgumentException.class,
      () -> new MindeeSettings("", ""));
  }
}
