package com.mindee.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.ClassificationField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Carte Nationale d'Identité API version 2.0 page data.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV2Page extends IdCardV2Document {

  /**
   * The sides of the document which are visible.
   */
  @JsonProperty("document_side")
  protected ClassificationField documentSide;
  /**
   * The document type or format.
   */
  @JsonProperty("document_type")
  protected ClassificationField documentType;

  @Override
  public boolean isEmpty() {
    return (this.documentType == null && this.documentSide == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Document Type: %s%n", this.getDocumentType()));
    outStr.append(String.format(":Document Sides: %s%n", this.getDocumentSide()));
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
