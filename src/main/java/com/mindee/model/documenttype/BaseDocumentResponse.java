package com.mindee.model.documenttype;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString(callSuper = true, includeFieldNames = true)
public abstract class BaseDocumentResponse {

  private String type;

}
