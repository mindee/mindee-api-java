package com.mindee.product.eu.licenseplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for License Plate, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicensePlateV1Document {

  /**
   * List of all license plates found in the image.
   */
  @JsonProperty("license_plates")
  private List<StringField> licensePlates = new ArrayList<>();

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
