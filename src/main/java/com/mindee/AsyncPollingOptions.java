package com.mindee;

import lombok.Builder;
import lombok.Value;

/**
 * Options to pass for asynchronous parsing with polling.
 */
@Value
public class AsyncPollingOptions {
  /**
   * Wait this many seconds before the first polling attempt.
   */
  Double initialDelaySec;
  /**
   * Wait this many seconds between each polling attempt.
   */
  Double intervalSec;
  /**
   * Maximum number of times to poll.
   */
  Integer maxRetries;

  @Builder
  private AsyncPollingOptions(
      Double initialDelaySec,
      Double intervalSec,
      Integer maxRetries
  ) {
    this.initialDelaySec = initialDelaySec == null ? 2.0 : initialDelaySec;
    this.intervalSec = intervalSec == null ? 1.5 : intervalSec;
    this.maxRetries = maxRetries == null ? 80 : maxRetries;
  }
}
