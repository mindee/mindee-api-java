package com.mindee.product.resume;

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
 * Document data for Resume, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumeV1Document extends Prediction {

  /**
   * The location information of the person, including city, state, and country.
   */
  @JsonProperty("address")
  private StringField address;
  /**
   * The list of certificates obtained by the candidate.
   */
  @JsonProperty("certificates")
  private List<ResumeV1Certificate> certificates = new ArrayList<>();
  /**
   * The ISO 639 code of the language in which the document is written.
   */
  @JsonProperty("document_language")
  private StringField documentLanguage;
  /**
   * The type of the document sent, possible values being RESUME, MOTIVATION_LETTER and RECOMMENDATION_LETTER.
   */
  @JsonProperty("document_type")
  private StringField documentType;
  /**
   * The list of values that represent the educational background of an individual.
   */
  @JsonProperty("education")
  private List<ResumeV1Education> education = new ArrayList<>();
  /**
   * The email address of the candidate.
   */
  @JsonProperty("email_address")
  private StringField emailAddress;
  /**
   * The list of names that represent a person's first or given names.
   */
  @JsonProperty("given_names")
  private List<StringField> givenNames = new ArrayList<>();
  /**
   * The list of specific technical abilities and knowledge mentioned in a resume.
   */
  @JsonProperty("hard_skills")
  private List<StringField> hardSkills = new ArrayList<>();
  /**
   * The specific industry or job role that the applicant is applying for.
   */
  @JsonProperty("job_applied")
  private StringField jobApplied;
  /**
   * The list of languages that a person is proficient in, as stated in their resume.
   */
  @JsonProperty("languages")
  private List<ResumeV1Language> languages = new ArrayList<>();
  /**
   * The ISO 3166 code for the country of citizenship or origin of the person.
   */
  @JsonProperty("nationality")
  private StringField nationality;
  /**
   * The phone number of the candidate.
   */
  @JsonProperty("phone_number")
  private StringField phoneNumber;
  /**
   * The area of expertise or specialization in which the individual has professional experience and qualifications.
   */
  @JsonProperty("profession")
  private StringField profession;
  /**
   * The list of values that represent the professional experiences of an individual in their global resume.
   */
  @JsonProperty("professional_experiences")
  private List<ResumeV1ProfessionalExperience> professionalExperiences = new ArrayList<>();
  /**
   * The list of URLs for social network profiles of the person.
   */
  @JsonProperty("social_networks_urls")
  private List<ResumeV1SocialNetworksUrl> socialNetworksUrls = new ArrayList<>();
  /**
   * The list of values that represent a person's interpersonal and communication abilities in a global resume.
   */
  @JsonProperty("soft_skills")
  private List<StringField> softSkills = new ArrayList<>();
  /**
   * The list of last names provided in a resume document.
   */
  @JsonProperty("surnames")
  private List<StringField> surnames = new ArrayList<>();

  @Override
  public boolean isEmpty() {
    return (
      this.documentLanguage == null
      && this.documentType == null
      && (this.givenNames == null || this.givenNames.isEmpty())
      && (this.surnames == null || this.surnames.isEmpty())
      && this.nationality == null
      && this.emailAddress == null
      && this.phoneNumber == null
      && this.address == null
      && (this.socialNetworksUrls == null || this.socialNetworksUrls.isEmpty())
      && this.profession == null
      && this.jobApplied == null
      && (this.languages == null || this.languages.isEmpty())
      && (this.hardSkills == null || this.hardSkills.isEmpty())
      && (this.softSkills == null || this.softSkills.isEmpty())
      && (this.education == null || this.education.isEmpty())
      && (this.professionalExperiences == null || this.professionalExperiences.isEmpty())
      && (this.certificates == null || this.certificates.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Document Language: %s%n", this.getDocumentLanguage())
    );
    outStr.append(
        String.format(":Document Type: %s%n", this.getDocumentType())
    );
    String givenNames = SummaryHelper.arrayToString(
        this.getGivenNames(),
        "%n              "
    );
    outStr.append(
        String.format(":Given Names: %s%n", givenNames)
    );
    String surnames = SummaryHelper.arrayToString(
        this.getSurnames(),
        "%n           "
    );
    outStr.append(
        String.format(":Surnames: %s%n", surnames)
    );
    outStr.append(
        String.format(":Nationality: %s%n", this.getNationality())
    );
    outStr.append(
        String.format(":Email Address: %s%n", this.getEmailAddress())
    );
    outStr.append(
        String.format(":Phone Number: %s%n", this.getPhoneNumber())
    );
    outStr.append(
        String.format(":Address: %s%n", this.getAddress())
    );
    String socialNetworksUrlsSummary = "";
    if (!this.getSocialNetworksUrls().isEmpty()) {
      int[] socialNetworksUrlsColSizes = new int[]{22, 52};
      socialNetworksUrlsSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(socialNetworksUrlsColSizes, "-"))
          + "| Name                 "
          + "| URL                                                "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(socialNetworksUrlsColSizes, "="));
      socialNetworksUrlsSummary += SummaryHelper.arrayToString(this.getSocialNetworksUrls(), socialNetworksUrlsColSizes);
      socialNetworksUrlsSummary += String.format("%n%s", SummaryHelper.lineSeparator(socialNetworksUrlsColSizes, "-"));
    }
    outStr.append(
        String.format(":Social Networks: %s%n", socialNetworksUrlsSummary)
    );
    outStr.append(
        String.format(":Profession: %s%n", this.getProfession())
    );
    outStr.append(
        String.format(":Job Applied: %s%n", this.getJobApplied())
    );
    String languagesSummary = "";
    if (!this.getLanguages().isEmpty()) {
      int[] languagesColSizes = new int[]{10, 22};
      languagesSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(languagesColSizes, "-"))
          + "| Language "
          + "| Level                "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(languagesColSizes, "="));
      languagesSummary += SummaryHelper.arrayToString(this.getLanguages(), languagesColSizes);
      languagesSummary += String.format("%n%s", SummaryHelper.lineSeparator(languagesColSizes, "-"));
    }
    outStr.append(
        String.format(":Languages: %s%n", languagesSummary)
    );
    String hardSkills = SummaryHelper.arrayToString(
        this.getHardSkills(),
        "%n              "
    );
    outStr.append(
        String.format(":Hard Skills: %s%n", hardSkills)
    );
    String softSkills = SummaryHelper.arrayToString(
        this.getSoftSkills(),
        "%n              "
    );
    outStr.append(
        String.format(":Soft Skills: %s%n", softSkills)
    );
    String educationSummary = "";
    if (!this.getEducation().isEmpty()) {
      int[] educationColSizes = new int[]{17, 27, 11, 10, 27, 13, 12};
      educationSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(educationColSizes, "-"))
          + "| Domain          "
          + "| Degree                    "
          + "| End Month "
          + "| End Year "
          + "| School                    "
          + "| Start Month "
          + "| Start Year "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(educationColSizes, "="));
      educationSummary += SummaryHelper.arrayToString(this.getEducation(), educationColSizes);
      educationSummary += String.format("%n%s", SummaryHelper.lineSeparator(educationColSizes, "-"));
    }
    outStr.append(
        String.format(":Education: %s%n", educationSummary)
    );
    String professionalExperiencesSummary = "";
    if (!this.getProfessionalExperiences().isEmpty()) {
      int[] professionalExperiencesColSizes = new int[]{17, 12, 27, 11, 10, 22, 13, 12};
      professionalExperiencesSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(professionalExperiencesColSizes, "-"))
          + "| Contract Type   "
          + "| Department "
          + "| Employer                  "
          + "| End Month "
          + "| End Year "
          + "| Role                 "
          + "| Start Month "
          + "| Start Year "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(professionalExperiencesColSizes, "="));
      professionalExperiencesSummary += SummaryHelper.arrayToString(this.getProfessionalExperiences(), professionalExperiencesColSizes);
      professionalExperiencesSummary += String.format("%n%s", SummaryHelper.lineSeparator(professionalExperiencesColSizes, "-"));
    }
    outStr.append(
        String.format(":Professional Experiences: %s%n", professionalExperiencesSummary)
    );
    String certificatesSummary = "";
    if (!this.getCertificates().isEmpty()) {
      int[] certificatesColSizes = new int[]{12, 32, 27, 6};
      certificatesSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(certificatesColSizes, "-"))
          + "| Grade      "
          + "| Name                           "
          + "| Provider                  "
          + "| Year "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(certificatesColSizes, "="));
      certificatesSummary += SummaryHelper.arrayToString(this.getCertificates(), certificatesColSizes);
      certificatesSummary += String.format("%n%s", SummaryHelper.lineSeparator(certificatesColSizes, "-"));
    }
    outStr.append(
        String.format(":Certificates: %s%n", certificatesSummary)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
