package com.mindee.parsing.common.field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class CompanyRegistrationField extends BaseField {

  @JsonProperty("value")
  private String value;
  @JsonProperty("type")
  private String type;

}
