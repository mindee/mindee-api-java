package com.mindee.product.us.w9;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.PositionField;
import com.mindee.parsing.standard.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * W9 API version 1.0 page data.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class W9V1Page extends W9V1Document {

  /**
   * The street address (number, street, and apt. or suite no.) of the applicant.
   */
  @JsonProperty("address")
  protected StringField address;
  /**
   * The business name or disregarded entity name, if different from Name.
   */
  @JsonProperty("business_name")
  protected StringField businessName;
  /**
   * The city, state, and ZIP code of the applicant.
   */
  @JsonProperty("city_state_zip")
  protected StringField cityStateZip;
  /**
   * The employer identification number.
   */
  @JsonProperty("ein")
  protected StringField ein;
  /**
   * Name as shown on the applicant's income tax return.
   */
  @JsonProperty("name")
  protected StringField name;
  /**
   * Position of the signature date on the document.
   */
  @JsonProperty("signature_date_position")
  protected PositionField signatureDatePosition;
  /**
   * Position of the signature on the document.
   */
  @JsonProperty("signature_position")
  protected PositionField signaturePosition;
  /**
   * The applicant's social security number.
   */
  @JsonProperty("ssn")
  protected StringField ssn;
  /**
   * The federal tax classification, which can vary depending on the revision date.
   */
  @JsonProperty("tax_classification")
  protected StringField taxClassification;
  /**
   * Depending on revision year, among S, C, P or D for Limited Liability Company Classification.
   */
  @JsonProperty("tax_classification_llc")
  protected StringField taxClassificationLlc;
  /**
   * Tax Classification Other Details.
   */
  @JsonProperty("tax_classification_other_details")
  protected StringField taxClassificationOtherDetails;
  /**
   * The Revision month and year of the W9 form.
   */
  @JsonProperty("w9_revision_date")
  protected StringField w9RevisionDate;

  @Override
  public boolean isEmpty() {
    return (
      this.name == null
      && this.ssn == null
      && this.address == null
      && this.cityStateZip == null
      && this.businessName == null
      && this.ein == null
      && this.taxClassification == null
      && this.taxClassificationOtherDetails == null
      && this.w9RevisionDate == null
      && this.signaturePosition == null
      && this.signatureDatePosition == null
      && this.taxClassificationLlc == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Name: %s%n", this.getName())
    );
    outStr.append(
        String.format(":SSN: %s%n", this.getSsn())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    outStr.append(
        String.format(":City State Zip: %s%n", this.getCityStateZip())
    );
    outStr.append(
        String.format(":Business Name: %s%n", this.getBusinessName())
    );
    outStr.append(
        String.format(":EIN: %s%n", this.getEin())
    );
    outStr.append(
        String.format(":Tax Classification: %s%n", this.getTaxClassification())
    );
    outStr.append(
        String.format(":Tax Classification Other Details: %s%n", this.getTaxClassificationOtherDetails())
    );
    outStr.append(
        String.format(":W9 Revision Date: %s%n", this.getW9RevisionDate())
    );
    outStr.append(
        String.format(":Signature Position: %s%n", this.getSignaturePosition())
    );
    outStr.append(
        String.format(":Signature Date Position: %s%n", this.getSignatureDatePosition())
    );
    outStr.append(
        String.format(":Tax Classification LLC: %s%n", this.getTaxClassificationLlc())
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
