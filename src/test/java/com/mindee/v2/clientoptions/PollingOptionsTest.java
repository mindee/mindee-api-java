package com.mindee.v2.clientoptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PollingOptionsTest {

  @Test
  void shouldSetDefaultValues() {
    PollingOptions pollingOptions = PollingOptions.builder().build();

    Assertions.assertEquals(3.0, pollingOptions.getInitialDelaySec());
    Assertions.assertEquals(1.5, pollingOptions.getIntervalSec());
    Assertions.assertEquals(100, pollingOptions.getMaxRetries());
  }

  @Test
  void shouldSetCustomValues() {
    PollingOptions pollingOptions = PollingOptions
      .builder()
      .initialDelaySec(4.0)
      .intervalSec(2.5)
      .maxRetries(50)
      .build();

    Assertions.assertEquals(4.0, pollingOptions.getInitialDelaySec());
    Assertions.assertEquals(2.5, pollingOptions.getIntervalSec());
    Assertions.assertEquals(50, pollingOptions.getMaxRetries());
  }

  @Test
  void shouldThrowWhenInitialDelayIsTooLow() {
    IllegalArgumentException exception = Assertions
      .assertThrows(
        IllegalArgumentException.class,
        () -> PollingOptions.builder().initialDelaySec(0.1).build()
      );
    Assertions.assertEquals("Initial delay must be ≥ 1.0", exception.getMessage());
  }
}
