package com.mindee.v2.clientoptions;

import com.mindee.clientoptions.BasePollingOptions;
import java.util.function.BooleanSupplier;
import lombok.Builder;
import lombok.Getter;

public class PollingOptions extends BasePollingOptions {
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
      BooleanSupplier cancelToken
  ) {
    super(initialDelaySec, intervalSec, maxRetries, 3.0, 1.5, 100, 1.0, 1.0, 2);

    this.cancelToken = cancelToken == null ? () -> false : cancelToken;
  }
}
