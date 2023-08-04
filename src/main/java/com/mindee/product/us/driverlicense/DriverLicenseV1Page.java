package com.mindee.product.us.driverlicense;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.PositionField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page data for Driver License, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverLicenseV1Page extends DriverLicenseV1Document {

  /**
   * Has a photo of the US driver license holder
   */
  @JsonProperty("photo")
  private PositionField photo;
  /**
   * Has a signature of the US driver license holder
   */
  @JsonProperty("signature")
  private PositionField signature;

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Photo: %s%n", this.getPhoto())
    );
    outStr.append(
        String.format(":Signature: %s%n", this.getSignature())
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
