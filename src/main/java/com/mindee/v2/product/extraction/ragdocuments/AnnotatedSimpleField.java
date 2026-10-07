package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A SimpleField with additional configuration for annotation.
 */
@Getter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = AnnotatedSimpleFieldDeserializer.class)
public class AnnotatedSimpleField extends AnnotatedBaseField {

  /**
   * Field value, one of: string, bool, int, double, null.
   */
  @JsonProperty("value")
  private Object value;

  /**
   * Default constructor.
   */
  public AnnotatedSimpleField(Object value, boolean selected, String guidelines) {
    super(selected, guidelines);
    this.value = value;
  }
}
