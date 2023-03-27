package com.mindee.parsing.shippingcontainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingContainerV1DocumentPrediction {

  /**
   * ISO 6346 code for container owner prefix + equipment identifier.
   */
  @JsonProperty("owner")
  private StringField owner;
  /**
   * ISO 6346 code for container serial number (6+1 digits).
   */
  @JsonProperty("serial_number")
  private StringField serialNumber;
  /**
   * ISO 6346 code for container length, height and type.
   */
  @JsonProperty("size_type")
  private StringField sizeType;

  @Override
  public String toString() {

    String summary =
      String.format(":Owner: %s%n", this.getOwner()) +
        String.format(":Serial number: %s%n", this.getSerialNumber()) +
        String.format(":Size and type: %s%n", this.getSizeType());

    return SummaryHelper.cleanSummary(summary);
  }
}
