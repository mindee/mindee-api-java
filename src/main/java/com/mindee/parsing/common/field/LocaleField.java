package com.mindee.parsing.common.field;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

/**
 * The local of the page.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocaleField extends BaseField {

  /**
   * Concatenation of lang and country codes.
   */
  @JsonProperty("value")
  private String value;
  /**
   * The language which has been detected.
   */
  private String language;
  @JsonProperty("language")
  public void setLanguage(String language) {
    this.language = language;
    this.value = language;
  }
  /**
   * The country which has been detected.
   */
  @JsonProperty("country")
  private String country;
  /**
   * The currency which has been detected.
   */
  @JsonProperty("currency")
  private String currency;

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder("");
    if (value != null) {
      stringBuilder.append(value);
      stringBuilder.append("; ");
    }
    if (language != null) {
      stringBuilder.append(language);
      stringBuilder.append("; ");
    }
    if (country != null) {
      stringBuilder.append(country);
      stringBuilder.append("; ");
    }
    if (currency != null) {
      stringBuilder.append(currency);
      stringBuilder.append("; ");
    }

    return stringBuilder.toString().trim();
  }

}
