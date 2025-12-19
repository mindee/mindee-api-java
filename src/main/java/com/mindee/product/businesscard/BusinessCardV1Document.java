package com.mindee.product.businesscard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Business Card API version 1.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessCardV1Document extends Prediction {

  /**
   * The address of the person.
   */
  @JsonProperty("address")
  protected StringField address;
  /**
   * The company the person works for.
   */
  @JsonProperty("company")
  protected StringField company;
  /**
   * The email address of the person.
   */
  @JsonProperty("email")
  protected StringField email;
  /**
   * The Fax number of the person.
   */
  @JsonProperty("fax_number")
  protected StringField faxNumber;
  /**
   * The given name of the person.
   */
  @JsonProperty("firstname")
  protected StringField firstname;
  /**
   * The job title of the person.
   */
  @JsonProperty("job_title")
  protected StringField jobTitle;
  /**
   * The lastname of the person.
   */
  @JsonProperty("lastname")
  protected StringField lastname;
  /**
   * The mobile number of the person.
   */
  @JsonProperty("mobile_number")
  protected StringField mobileNumber;
  /**
   * The phone number of the person.
   */
  @JsonProperty("phone_number")
  protected StringField phoneNumber;
  /**
   * The social media profiles of the person or company.
   */
  @JsonProperty("social_media")
  protected List<StringField> socialMedia = new ArrayList<>();
  /**
   * The website of the person or company.
   */
  @JsonProperty("website")
  protected StringField website;

  @Override
  public boolean isEmpty() {
    return (this.firstname == null
      && this.lastname == null
      && this.jobTitle == null
      && this.company == null
      && this.email == null
      && this.phoneNumber == null
      && this.mobileNumber == null
      && this.faxNumber == null
      && this.address == null
      && this.website == null
      && (this.socialMedia == null || this.socialMedia.isEmpty()));
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Firstname: %s%n", this.getFirstname()));
    outStr.append(String.format(":Lastname: %s%n", this.getLastname()));
    outStr.append(String.format(":Job Title: %s%n", this.getJobTitle()));
    outStr.append(String.format(":Company: %s%n", this.getCompany()));
    outStr.append(String.format(":Email: %s%n", this.getEmail()));
    outStr.append(String.format(":Phone Number: %s%n", this.getPhoneNumber()));
    outStr.append(String.format(":Mobile Number: %s%n", this.getMobileNumber()));
    outStr.append(String.format(":Fax Number: %s%n", this.getFaxNumber()));
    outStr.append(String.format(":Address: %s%n", this.getAddress()));
    outStr.append(String.format(":Website: %s%n", this.getWebsite()));
    String socialMedia = SummaryHelper.arrayToString(this.getSocialMedia(), "%n               ");
    outStr.append(String.format(":Social Media: %s%n", socialMedia));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
