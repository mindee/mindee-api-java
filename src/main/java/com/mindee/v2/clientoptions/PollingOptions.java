package com.mindee.v2.clientoptions;

import com.mindee.clientoptions.BasePollingOptions;
import lombok.Builder;

public class PollingOptions extends BasePollingOptions {
  @Builder
  public PollingOptions(Double initialDelaySec, Double intervalSec, Integer maxRetries) {
    super(initialDelaySec, intervalSec, maxRetries, 3.0, 1.5, 100, 1.0, 1.0, 2);
  }
}
