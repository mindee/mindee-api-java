package com.mindee.product.shippingcontainer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Shipping Container, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShippingContainerV1Document {

  /**
   * The ISO-6346 code for container owner and equipment identifier.
   */
  @JsonProperty("owner")
  private StringField owner;
  /**
   * ISO-6346 code for container serial number.
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
    StringBuilder outStr = new StringBuilder();

    outStr.append(
        String.format(":Owner: %s%n", this.getOwner())
    );
    outStr.append(
        String.format(":Serial Number: %s%n", this.getSerialNumber())
    );
    outStr.append(
        String.format(":Size and Type: %s%n", this.getSizeType())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
