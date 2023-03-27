package com.mindee.parsing.eu.licenseplate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.StringField;
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
public class LicensePlateV1DocumentPrediction {

  /**
  * List of all license plates found in the image.
  */
  @JsonProperty("license_plates")
  private List<StringField> licensePlates = new ArrayList<>();

  @Override
  public String toString() {
    String summary =
        String.format(
          ":License Plates: %s%n",
          SummaryHelper.arrayToString(
            this.getLicensePlates(),
            "%n                 "
          )
        );
    return SummaryHelper.cleanSummary(summary);
  }
}
