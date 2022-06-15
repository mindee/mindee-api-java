package com.mindee.documentparser;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ParseParameters {

  private String documentType;
  private String accountName;
  private Boolean includeWords;
  @Builder.Default
  private Integer cutMode = 1;
}
