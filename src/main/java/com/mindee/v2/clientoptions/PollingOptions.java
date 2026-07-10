package com.mindee.v2.clientoptions;

import com.mindee.clientoptions.BasePollingOptions;
import java.util.function.BooleanSupplier;
import lombok.Builder;
import lombok.Getter;

public class PollingOptions extends BasePollingOptions {

  /**
   * Multiplier applied to {@code intervalSec} after each poll attempt to implement
   * exponential backoff. Must be ≥ 1.0. A value of 1.0 disables backoff.
   */
  @Getter
  private final Double backoffMultiplier;

  /**
   * Upper bound (in seconds) for the polling interval after backoff is applied.
   * Must be ≥ {@code intervalSec}.
   */
  @Getter
  private final Double maxIntervalSec;

  /**
   * Optional cancellation signal. When it evaluates to {@code true}, polling is
   * aborted with a {@link java.util.concurrent.CancellationException}. Also,
   * interrupting the polling thread cancels the operation.
   */
  @Getter
  private final BooleanSupplier cancelToken;

  @Builder
  public PollingOptions(
      Double initialDelaySec,
      Double intervalSec,
      Integer maxRetries,
      Double backoffMultiplier,
      Double maxIntervalSec,
      BooleanSupplier cancelToken
  ) {
    super(initialDelaySec, intervalSec, maxRetries, 3.0, 1.5, 100, 1.0, 1.0, 2);
    this.backoffMultiplier = backoffMultiplier == null ? 1.5 : backoffMultiplier;
    if (this.backoffMultiplier < 1.0) {
      throw new IllegalArgumentException("Backoff multiplier must be ≥ 1.0");
    }
    this.maxIntervalSec = maxIntervalSec == null ? 60.0 : maxIntervalSec;
    if (this.maxIntervalSec < this.getIntervalSec()) {
      throw new IllegalArgumentException(
        "Max interval must be ≥ interval (" + this.getIntervalSec() + ")"
      );
    }
    this.cancelToken = cancelToken == null ? () -> false : cancelToken;
  }
}
