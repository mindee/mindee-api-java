package com.mindee.model.documenttype;

import java.util.Map;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true, includeFieldNames = true)
public abstract class BaseDocumentResponse {

  private String type;
  private Map<String, Object> rawResponse;
  private String filename;

  public String documentSummary() {
    return this.toString();
  }

}
