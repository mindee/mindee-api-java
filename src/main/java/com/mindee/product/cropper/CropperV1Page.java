package com.mindee.product.cropper;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.PositionField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Cropper API version 1.1 page data.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CropperV1Page extends CropperV1Document {

  /**
   * List of documents found in the image.
   */
  @JsonProperty("cropping")
  protected List<PositionField> cropping = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      (this.cropping == null || this.cropping.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    String cropping = SummaryHelper.arrayToString(
        this.getCropping(),
        "%n                   "
    );
    outStr.append(
        String.format(":Document Cropper: %s%n", cropping)
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
