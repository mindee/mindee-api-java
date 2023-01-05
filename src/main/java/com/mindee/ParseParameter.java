package com.mindee;

import com.mindee.documentparser.PageOptions;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParseParameter {

  byte[] file;
  String fileName;
  @Builder.Default
  Boolean includeWords = Boolean.FALSE;
  PageOptions pageOptions;
}
