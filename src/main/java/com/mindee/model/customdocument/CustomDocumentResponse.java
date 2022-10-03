package com.mindee.model.customdocument;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.documenttype.BaseDocumentResponse;

import lombok.EqualsAndHashCode;
import lombok.ToString;

@JsonDeserialize(using = CustomDocumentDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class CustomDocumentResponse
    extends BaseDocumentResponse {

  private Map<String, ListField> fields = new HashMap<>();

  /**
   * @param fields
   */
  public CustomDocumentResponse(Map<String, ListField> fields) {
    this.fields = fields;
  }

  /**
   * @return the items
   */
  public Map<String, ListField> getFields() {
    return fields;
  }

  /**
   *
   * @param name
   * @return
   */
  public ListField getField(String name) {
    return fields.get(name);
  }
}
