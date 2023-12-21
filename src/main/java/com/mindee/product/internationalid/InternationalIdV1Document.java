package com.mindee.product.internationalid;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.ClassificationField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for International ID, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class InternationalIdV1Document extends Prediction {

  /**
   * The physical location of the document holder's residence.
   */
  @JsonProperty("address")
  private StringField address;
  /**
   * The date of birth of the document holder.
   */
  @JsonProperty("birth_date")
  private StringField birthDate;
  /**
   * The location where the document holder was born.
   */
  @JsonProperty("birth_place")
  private StringField birthPlace;
  /**
   * The country that issued the identification document.
   */
  @JsonProperty("country_of_issue")
  private StringField countryOfIssue;
  /**
   * The unique identifier assigned to the identification document.
   */
  @JsonProperty("document_number")
  private StringField documentNumber;
  /**
   * The type of identification document being used.
   */
  @JsonProperty("document_type")
  private ClassificationField documentType;
  /**
   * The date when the document will no longer be valid for use.
   */
  @JsonProperty("expiry_date")
  private StringField expiryDate;
  /**
   * The first names or given names of the document holder.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The date when the document was issued.
   */
  @JsonProperty("issue_date")
  private StringField issueDate;
  /**
   * First line of information in a standardized format for easy machine reading and processing.
   */
  @JsonProperty("mrz1")
  private StringField mrz1;
  /**
   * Second line of information in a standardized format for easy machine reading and processing.
   */
  @JsonProperty("mrz2")
  private StringField mrz2;
  /**
   * Third line of information in a standardized format for easy machine reading and processing.
   */
  @JsonProperty("mrz3")
  private StringField mrz3;
  /**
   * Indicates the country of citizenship or nationality of the document holder.
   */
  @JsonProperty("nationality")
  private StringField nationality;
  /**
   * The document holder's biological sex, such as male or female.
   */
  @JsonProperty("sex")
  private StringField sex;
  /**
   * The surnames of the document holder.
   */
  @JsonProperty("surnames")
  private List<StringField> surnames = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      this.documentType == null
      && this.documentNumber == null
      && this.countryOfIssue == null
      && (this.surnames == null || this.surnames.isEmpty())
      && (this.givenNames == null || this.givenNames.isEmpty())
      && this.sex == null
      && this.birthDate == null
      && this.birthPlace == null
      && this.nationality == null
      && this.issueDate == null
      && this.expiryDate == null
      && this.address == null
      && this.mrz1 == null
      && this.mrz2 == null
      && this.mrz3 == null
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
    outStr.append(
        String.format(":Country of Issue: %s%n", this.getCountryOfIssue())
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
        String.format(":Gender: %s%n", this.getSex())
    );
    outStr.append(
        String.format(":Birth date: %s%n", this.getBirthDate())
    );
    outStr.append(
        String.format(":Birth Place: %s%n", this.getBirthPlace())
    );
    outStr.append(
        String.format(":Nationality: %s%n", this.getNationality())
    );
    outStr.append(
        String.format(":Issue Date: %s%n", this.getIssueDate())
    );
    outStr.append(
        String.format(":Expiry Date: %s%n", this.getExpiryDate())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    outStr.append(
        String.format(":Machine Readable Zone Line 1: %s%n", this.getMrz1())
    );
    outStr.append(
        String.format(":Machine Readable Zone Line 2: %s%n", this.getMrz2())
    );
    outStr.append(
        String.format(":Machine Readable Zone Line 3: %s%n", this.getMrz3())
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
