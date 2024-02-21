package com.mindee.product.internationalid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.DateField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for International ID, API version 2.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternationalIdV2Document extends Prediction {

  /**
   * The physical address of the document holder.
   */
  @JsonProperty("address")
  private StringField address;
  /**
   * The date of birth of the document holder.
   */
  @JsonProperty("birth_date")
  private DateField birthDate;
  /**
   * The place of birth of the document holder.
   */
  @JsonProperty("birth_place")
  private StringField birthPlace;
  /**
   * The country where the document was issued.
   */
  @JsonProperty("country_of_issue")
  private StringField countryOfIssue;
  /**
   * The unique identifier assigned to the document.
   */
  @JsonProperty("document_number")
  private StringField documentNumber;
  /**
   * The type of personal identification document.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * The date when the document becomes invalid.
   */
  @JsonProperty("expiry_date")
  private DateField expiryDate;
  /**
   * The list of the document holder's given names.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The date when the document was issued.
   */
  @JsonProperty("issue_date")
  private DateField issueDate;
  /**
   * The Machine Readable Zone, first line.
   */
  @JsonProperty("mrz_line1")
  private StringField mrzLine1;
  /**
   * The Machine Readable Zone, second line.
   */
  @JsonProperty("mrz_line2")
  private StringField mrzLine2;
  /**
   * The Machine Readable Zone, third line.
   */
  @JsonProperty("mrz_line3")
  private StringField mrzLine3;
  /**
   * The country of citizenship of the document holder.
   */
  @JsonProperty("nationality")
  private StringField nationality;
  /**
   * The unique identifier assigned to the document holder.
   */
  @JsonProperty("personal_number")
  private StringField personalNumber;
  /**
   * The biological sex of the document holder.
   */
  @JsonProperty("sex")
  private StringField sex;
  /**
   * The state or territory where the document was issued.
   */
  @JsonProperty("state_of_issue")
  private StringField stateOfIssue;
  /**
   * The list of the document holder's family names.
   */
  @JsonProperty("surnames")
  private List<StringField> surnames = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      this.documentType == null
      && this.documentNumber == null
      && (this.surnames == null || this.surnames.isEmpty())
      && (this.givenNames == null || this.givenNames.isEmpty())
      && this.sex == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.nationality == null
      && this.personalNumber == null
      && this.countryOfIssue == null
      && this.stateOfIssue == null
      && this.issueDate == null
      && this.expiryDate == null
      && this.address == null
      && this.mrzLine1 == null
      && this.mrzLine2 == null
      && this.mrzLine3 == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    outStr.append(
        String.format(":Document Number: %s%n", this.getDocumentNumber())
    );
    String surnames = SummaryHelper.arrayToString(
        this.getSurnames(),
        "%n           "
    );
    outStr.append(
        String.format(":Surnames: %s%n", surnames)
    );
    String givenNames = SummaryHelper.arrayToString(
        this.getGivenNames(),
        "%n              "
    );
    outStr.append(
        String.format(":Given Names: %s%n", givenNames)
    );
    outStr.append(
        String.format(":Sex: %s%n", this.getSex())
    );
    outStr.append(
        String.format(":Birth Date: %s%n", this.getBirthDate())
    );
    outStr.append(
        String.format(":Birth Place: %s%n", this.getBirthPlace())
    );
    outStr.append(
        String.format(":Nationality: %s%n", this.getNationality())
    );
    outStr.append(
        String.format(":Personal Number: %s%n", this.getPersonalNumber())
    );
    outStr.append(
        String.format(":Country of Issue: %s%n", this.getCountryOfIssue())
    );
    outStr.append(
        String.format(":State of Issue: %s%n", this.getStateOfIssue())
    );
    outStr.append(
        String.format(":Issue Date: %s%n", this.getIssueDate())
    );
    outStr.append(
        String.format(":Expiration Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    outStr.append(
        String.format(":MRZ Line 1: %s%n", this.getMrzLine1())
    );
    outStr.append(
        String.format(":MRZ Line 2: %s%n", this.getMrzLine2())
    );
    outStr.append(
        String.format(":MRZ Line 3: %s%n", this.getMrzLine3())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
