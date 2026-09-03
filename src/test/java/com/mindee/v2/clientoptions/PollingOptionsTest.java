package com.mindee.v2.clientoptions;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PollingOptionsTest {

  @Test
  void shouldSetDefaultValues() {
    PollingOptions pollingOptions = PollingOptions.builder().build();

    Assertions.assertEquals(3.0, pollingOptions.getInitialDelaySec());
    Assertions.assertEquals(1.5, pollingOptions.getIntervalSec());
    Assertions.assertEquals(100, pollingOptions.getMaxRetries());
    Assertions.assertNotNull(pollingOptions.getCancelToken());
    Assertions.assertFalse(pollingOptions.getCancelToken().getAsBoolean());
  }

  @Test
  void shouldSetCustomValues() {
    AtomicBoolean cancelled = new AtomicBoolean(false);
    PollingOptions pollingOptions = PollingOptions
      .builder()
      .initialDelaySec(4.0)
      .intervalSec(2.5)
      .maxRetries(50)
      .cancelToken(cancelled::get)
      .build();

    Assertions.assertEquals(4.0, pollingOptions.getInitialDelaySec());
    Assertions.assertEquals(2.5, pollingOptions.getIntervalSec());
    Assertions.assertEquals(50, pollingOptions.getMaxRetries());

    Assertions.assertFalse(pollingOptions.getCancelToken().getAsBoolean());
    cancelled.set(true);
    Assertions.assertTrue(pollingOptions.getCancelToken().getAsBoolean());
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
