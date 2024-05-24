package com.mindee.product.eu.licenseplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * License Plate API version 1.1 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicensePlateV1Document extends Prediction {

  /**
   * List of all license plates found in the image.
   */
  @JsonProperty("license_plates")
  protected List<StringField> licensePlates = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      (this.licensePlates == null || this.licensePlates.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    String licensePlates = SummaryHelper.arrayToString(
        this.getLicensePlates(),
        "%n                 "
    );
    outStr.append(
        String.format(":License Plates: %s%n", licensePlates)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
