package com.mindee.product.us.healthcarecard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Healthcare Card API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class HealthcareCardV1Document extends Prediction {

  /**
   * The name of the company that provides the healthcare plan.
   */
  @JsonProperty("company_name")
  protected StringField companyName;
  /**
   * Is a fixed amount for a covered service.
   */
  @JsonProperty("copays")
  protected List<HealthcareCardV1Copay> copays = new ArrayList<>();
  /**
   * The list of dependents covered by the healthcare plan.
   */
  @JsonProperty("dependents")
  protected List<StringField> dependents = new ArrayList<>();
  /**
   * The date when the member enrolled in the healthcare plan.
   */
  @JsonProperty("enrollment_date")
  protected DateField enrollmentDate;
  /**
   * The group number associated with the healthcare plan.
   */
  @JsonProperty("group_number")
  protected StringField groupNumber;
  /**
   * The organization that issued the healthcare plan.
   */
  @JsonProperty("issuer_80840")
  protected StringField issuer80840;
  /**
   * The unique identifier for the member in the healthcare system.
   */
  @JsonProperty("member_id")
  protected StringField memberId;
  /**
   * The name of the member covered by the healthcare plan.
   */
  @JsonProperty("member_name")
  protected StringField memberName;
  /**
   * The unique identifier for the payer in the healthcare system.
   */
  @JsonProperty("payer_id")
  protected StringField payerId;
  /**
   * The BIN number for prescription drug coverage.
   */
  @JsonProperty("rx_bin")
  protected StringField rxBin;
  /**
   * The group number for prescription drug coverage.
   */
  @JsonProperty("rx_grp")
  protected StringField rxGrp;
  /**
   * The PCN number for prescription drug coverage.
   */
  @JsonProperty("rx_pcn")
  protected StringField rxPcn;

  @Override
  public boolean isEmpty() {
    return (
      this.companyName == null
      && this.memberName == null
      && this.memberId == null
      && this.issuer80840 == null
      && (this.dependents == null || this.dependents.isEmpty())
      && this.groupNumber == null
      && this.payerId == null
      && this.rxBin == null
      && this.rxGrp == null
      && this.rxPcn == null
      && (this.copays == null || this.copays.isEmpty())
      && this.enrollmentDate == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Company Name: %s%n", this.getCompanyName())
    );
    outStr.append(
        String.format(":Member Name: %s%n", this.getMemberName())
    );
    outStr.append(
        String.format(":Member ID: %s%n", this.getMemberId())
    );
    outStr.append(
        String.format(":Issuer 80840: %s%n", this.getIssuer80840())
    );
    String dependents = SummaryHelper.arrayToString(
        this.getDependents(),
        "%n             "
    );
    outStr.append(
        String.format(":Dependents: %s%n", dependents)
    );
    outStr.append(
        String.format(":Group Number: %s%n", this.getGroupNumber())
    );
    outStr.append(
        String.format(":Payer ID: %s%n", this.getPayerId())
    );
    outStr.append(
        String.format(":RX BIN: %s%n", this.getRxBin())
    );
    outStr.append(
        String.format(":RX GRP: %s%n", this.getRxGrp())
    );
    outStr.append(
        String.format(":RX PCN: %s%n", this.getRxPcn())
    );
    String copaysSummary = "";
    if (!this.getCopays().isEmpty()) {
      int[] copaysColSizes = new int[]{14, 14};
      copaysSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(copaysColSizes, "-"))
          + "| Service Fees "
          + "| Service Name "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(copaysColSizes, "="));
      copaysSummary += SummaryHelper.arrayToString(this.getCopays(), copaysColSizes);
      copaysSummary += String.format("%n%s", SummaryHelper.lineSeparator(copaysColSizes, "-"));
    }
    outStr.append(
        String.format(":copays: %s%n", copaysSummary)
    );
    outStr.append(
        String.format(":Enrollment Date: %s%n", this.getEnrollmentDate())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
