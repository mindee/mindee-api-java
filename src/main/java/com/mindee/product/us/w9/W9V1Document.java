package com.mindee.product.us.w9;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for US W9, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class W9V1Document {


  @Override
  public String toString() {
    return "";
  }
}
