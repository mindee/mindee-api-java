package com.mindee.model.fields;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.geometry.Polygon;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;


@Value
@JsonIgnoreProperties(ignoreUnknown = true)
public class Locale extends BaseField {

  private final java.util.Locale value;
  private final String language;
  private final String country;
  private final String currency;



  @Builder @Jacksonized
  public Locale(Boolean reconstructed, Double confidence,
    Polygon polygon, @JsonProperty("page_id")  Integer page, String language,
      String country,
      String currency) {
    super(reconstructed, confidence, polygon, page);
    this.value = new java.util.Locale.Builder().setLanguageTag(language)
      .build();
    this.language = language;
    this.country = country;
    this.currency = currency;
  }

  public String localeSummary() {
    StringBuilder stringBuilder = new StringBuilder("");
    if (value != null) {
      stringBuilder.append(value.getDisplayName());
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
