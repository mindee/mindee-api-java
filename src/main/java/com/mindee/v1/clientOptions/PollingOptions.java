package com.mindee.v1.clientOptions;

import com.mindee.clientOptions.BasePollingOptions;
import lombok.Builder;

public class PollingOptions extends BasePollingOptions {

  @Builder
  public PollingOptions(Double initialDelaySec, Double intervalSec, Integer maxRetries) {
    super(initialDelaySec, intervalSec, maxRetries, 2.0, 1.5, 80, 1.0, 1.0, 2);
  }
}
