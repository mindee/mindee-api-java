package com.mindee.documentparser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParseParameters {

  private String documentType;
  private String accountName;
  @Builder.Default
  private Boolean includeWords = Boolean.FALSE;
  private PageOptions pageOptions;
}
