package com.mindee.model.documenttype;

import java.util.Map;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
public class CustomDocumentResponse extends BaseDocumentResponse {

  private final Map<String, Object> fields;


  public CustomDocumentResponse(Map<String, Object> fields) {
    this.fields = fields;
  }

  public Object get(String key) {
    return this.fields.get(key);
  }
}
