package com.mindee.clientOptions;

import lombok.Getter;

/**
 * Options to pass for asynchronous parsing with polling.
 */
public abstract class BasePollingOptions {
  /**
   * Wait this many seconds before the first polling attempt.
   */
  @Getter
  Double initialDelaySec;

  /**
   * Wait this many seconds between each polling attempt.
   */
  @Getter
  Double intervalSec;

  /**
   * Maximum number of times to poll.
   */
  @Getter
  Integer maxRetries;

  protected BasePollingOptions(
      Double initialDelaySec,
      Double intervalSec,
      Integer maxRetries,
      double defaultInitialDelaySec,
      double defaultIntervalSec,
      int defaultMaxRetries,
      double minInitialDelaySec,
      double minIntervalSec,
      int minMaxRetries
  ) {
    this.initialDelaySec = initialDelaySec == null ? defaultInitialDelaySec : initialDelaySec;
    this.intervalSec = intervalSec == null ? defaultIntervalSec : intervalSec;
    this.maxRetries = maxRetries == null ? defaultMaxRetries : maxRetries;

    if (this.initialDelaySec < minInitialDelaySec) {
      throw new IllegalArgumentException("Initial delay must be ≥ " + minInitialDelaySec);
    }
    if (this.intervalSec < minIntervalSec) {
      throw new IllegalArgumentException("Interval must be ≥ " + minIntervalSec);
    }
    if (this.maxRetries < minMaxRetries) {
      throw new IllegalArgumentException("Max retries must be ≥ " + minMaxRetries);
    }
  }
}
