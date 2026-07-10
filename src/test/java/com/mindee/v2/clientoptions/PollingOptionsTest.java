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
    Assertions.assertEquals(1.5, pollingOptions.getBackoffMultiplier());
    Assertions.assertEquals(60.0, pollingOptions.getMaxIntervalSec());
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
      .backoffMultiplier(2.0)
      .maxIntervalSec(30.0)
      .cancelToken(cancelled::get)
      .build();

    Assertions.assertEquals(4.0, pollingOptions.getInitialDelaySec());
    Assertions.assertEquals(2.5, pollingOptions.getIntervalSec());
    Assertions.assertEquals(50, pollingOptions.getMaxRetries());
    Assertions.assertEquals(2.0, pollingOptions.getBackoffMultiplier());
    Assertions.assertEquals(30.0, pollingOptions.getMaxIntervalSec());

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

  @Test
  void shouldThrowWhenBackoffMultiplierIsTooLow() {
    IllegalArgumentException exception = Assertions
      .assertThrows(
        IllegalArgumentException.class,
        () -> PollingOptions.builder().backoffMultiplier(0.9).build()
      );
    Assertions.assertEquals("Backoff multiplier must be ≥ 1.0", exception.getMessage());
  }

  @Test
  void shouldThrowWhenMaxIntervalIsBelowInterval() {
    IllegalArgumentException exception = Assertions
      .assertThrows(
        IllegalArgumentException.class,
        () -> PollingOptions.builder().intervalSec(5.0).maxIntervalSec(2.0).build()
      );
    Assertions.assertTrue(exception.getMessage().startsWith("Max interval must be ≥ interval"));
  }
}
