package com.mindee;

import java.net.URL;
import lombok.Builder;
import lombok.Value;

/**
 * Parameters for the `parse` method.
 */
@Value
@Builder
public class ParseParameter {

  byte[] file;
  URL fileUrl;
  String fileName;
  @Builder.Default
  Boolean includeWords = Boolean.FALSE;
  PageOptions pageOptions;
}
