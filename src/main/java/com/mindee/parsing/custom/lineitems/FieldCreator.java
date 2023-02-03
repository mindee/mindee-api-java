package com.mindee.parsing.custom.lineitems;

import com.mindee.parsing.custom.ListField;

import java.util.stream.Collectors;

public final class FieldCreator {

  private FieldCreator() {

  }

  public static Field create(String fieldName,  ListField listField) {
    return new Field(
      fieldName,
      listField.getValues().stream()
        .map(v -> new FieldValue(v.getContent(), v.getPolygon()))
        .collect(Collectors.toList()));
  }

}
