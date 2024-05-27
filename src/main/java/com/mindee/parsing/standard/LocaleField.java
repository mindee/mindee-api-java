package com.mindee.parsing.standard;


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
  private String value;

  /**
   * The language which has been detected.
   */
  private final String language;

  /**
   * The country which has been detected.
   */
  private final String country;

  /**
   * The currency which has been detected.
   */
  private final String currency;

  public LocaleField(
      @JsonProperty("value")
      String value,
      @JsonProperty("language")
      String language,
      @JsonProperty("country")
      String country,
      @JsonProperty("currency")
      String currency
  ) {
    this.value = value;
    this.language = language;
    this.country = country;
    this.currency = currency;

    if ((value == null || value.isEmpty()) && language != null) {
      this.value = language;
    }
  }

  public boolean isEmpty() {
    return (
        (this.value == null || this.value.isEmpty())
        && (this.country == null || this.country.isEmpty())
        && (this.language == null || this.language.isEmpty())
        && (this.currency == null || this.currency.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    if (value != null && !value.isEmpty()) {
      stringBuilder.append(value);
      stringBuilder.append("; ");
    }
    if (language != null && !language.isEmpty()) {
      stringBuilder.append(language);
      stringBuilder.append("; ");
    }
    if (country != null && !country.isEmpty()) {
      stringBuilder.append(country);
      stringBuilder.append("; ");
    }
    if (currency != null && !currency.isEmpty()) {
      stringBuilder.append(currency);
      stringBuilder.append("; ");
    }
    return stringBuilder.toString().trim();

  }

}
