package com.mindee;

import com.mindee.documentparser.PageOptions;
import lombok.Builder;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class ParseParameter {

  InputStream fileStream;
  String fileName;
  @Builder.Default
  Boolean includeWords = Boolean.FALSE;
  PageOptions pageOptions;
}
