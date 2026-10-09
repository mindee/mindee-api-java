package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * An ObjectField with additional configuration for annotation.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AnnotatedObjectField extends AnnotatedBaseField {

  /**
   * Sub-fields of the field.
   */
  @JsonProperty("fields")
  private AnnotatedFields fields;

  /**
   * Default constructor.
   */
  public AnnotatedObjectField(AnnotatedFields fields, boolean selected, String guidelines) {
    super(selected, guidelines);
    this.fields = fields;
  }
}
