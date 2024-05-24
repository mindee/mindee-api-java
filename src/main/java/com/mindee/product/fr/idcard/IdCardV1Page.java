package com.mindee.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.ClassificationField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Carte Nationale d'Identité API version 1.1 page data.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1Page extends IdCardV1Document {

  /**
   * The side of the document which is visible.
   */
  @JsonProperty("document_side")
  protected ClassificationField documentSide;

  @Override
  public boolean isEmpty() {
    return (
      this.documentSide == null
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Document Side: %s%n", this.getDocumentSide())
    );
    outStr.append(super.toString());
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
