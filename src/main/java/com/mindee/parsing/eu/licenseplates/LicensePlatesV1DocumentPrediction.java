package com.mindee.parsing.eu.licenseplates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.StringField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class LicensePlatesV1DocumentPrediction {

  /**
   * A list of license plates.
   */
  @JsonProperty("license_plates")
  private List<StringField> licensePlates = new ArrayList<>();

  @Override
  public String toString() {

    String summary =
      String.format(":License plates: %s%n",
        this.getLicensePlates().stream()
          .map(StringField::toString)
          .collect(Collectors.joining(", ")));

    return SummaryHelper.cleanSummary(summary);
  }
}
