package com.mindee.v2.product.extraction.ragdocuments;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * A ListField with additional configuration for annotation.
 */
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class AnnotatedListField extends AnnotatedBaseField {

  /**
   * List of dynamic fields, prefer SimpleItems or ObjectItems.
   */
  @Getter
  @JsonProperty("items")
  private List<AnnotatedDynamicField> items = new ArrayList<>();

  private List<AnnotatedSimpleField> simpleItems;
  private List<AnnotatedObjectField> objectItems;

  /**
   * Default constructor.
   */
  public AnnotatedListField(
      List<AnnotatedDynamicField> items,
      boolean selected,
      String guidelines
  ) {
    super(selected, guidelines);
    this.items = items;
  }

  /**
   * List of simple fields.
   */
  @JsonIgnore
  public List<AnnotatedSimpleField> getSimpleItems() {
    if (simpleItems != null) {
      return simpleItems;
    }

    if (items == null) {
      return new ArrayList<>();
    }

    simpleItems = items
      .stream()
      .filter(item -> item.getSimpleField() != null)
      .map(AnnotatedDynamicField::getSimpleField)
      .collect(Collectors.toList());

    return simpleItems;
  }

  /**
   * List of object fields.
   */
  @JsonIgnore
  public List<AnnotatedObjectField> getObjectItems() {
    if (objectItems != null) {
      return objectItems;
    }

    if (items == null) {
      return new ArrayList<>();
    }

    objectItems = items
      .stream()
      .filter(item -> item.getObjectField() != null)
      .map(AnnotatedDynamicField::getObjectField)
      .collect(Collectors.toList());

    return objectItems;
  }
}
