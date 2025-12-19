package com.mindee.parsing.standard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.geometry.Polygon;
import com.mindee.geometry.PolygonDeserializer;
import lombok.Getter;

/**
 * Represent a postal address field broken down into its individual components.
 */
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public final class AddressField extends StringField {
  /** The address exactly as it appears on the document. */
  private final String rawValue;

  /** Street number. */
  private final String streetNumber;

  /** Street name. */
  private final String streetName;

  /** PO-box number. */
  private final String poBox;

  /** Additional address complement. */
  private final String addressComplement;

  /** City or locality. */
  private final String city;

  /** Postal or ZIP code. */
  private final String postalCode;

  /** State, province or region. */
  private final String state;

  /** Country. */
  private final String country;

  public AddressField(
      @JsonProperty("value") String value,
      @JsonProperty("raw_value") String rawValue,
      @JsonProperty("street_number") String streetNumber,
      @JsonProperty("street_name") String streetName,
      @JsonProperty("po_box") String poBox,
      @JsonProperty("address_complement") String addressComplement,
      @JsonProperty("city") String city,
      @JsonProperty("postal_code") String postalCode,
      @JsonProperty("state") String state,
      @JsonProperty("country") String country,
      @JsonProperty("confidence") Double confidence,
      @JsonProperty("polygon") @JsonDeserialize(using = PolygonDeserializer.class) Polygon polygon,
      @JsonProperty("page_id") Integer pageId
  ) {
    super(value, rawValue, confidence, polygon, pageId);
    this.rawValue = rawValue;
    this.streetNumber = streetNumber;
    this.streetName = streetName;
    this.poBox = poBox;
    this.addressComplement = addressComplement;
    this.city = city;
    this.postalCode = postalCode;
    this.state = state;
    this.country = country;
  }

  /**
   * Address field constructor only containing the value.
   */
  public AddressField(String value, Double confidence, Polygon polygon) {
    this(value, null, null, null, null, null, null, null, null, null, confidence, polygon, null);
  }
}
