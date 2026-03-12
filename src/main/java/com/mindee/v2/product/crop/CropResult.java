package com.mindee.v2.product.crop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of a crop utility inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class CropResult {
  /**
   * List of objects and their cropping coordinates identified in the source document.
   */
  @JsonProperty("crops")
  private ArrayList<CropItem> crops;

  @Override
  public String toString() {
    StringJoiner joiner = new StringJoiner("\n");
    joiner.add("Crops").add("=====");
    for (CropItem item : this.crops) {
      joiner.add(item.toString());
    }
    return joiner.toString();
  }
}
