package com.mindee.product.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.field.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Page data for Carte Nationale d'Identité, API version 1.
 */
@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1PagePrediction extends IdCardV1DocumentPrediction {

  /**
   * The side of the document which is visible.
   */
  @JsonProperty("document_side")
  private StringField documentSide;

  @Override
  public String toString() {
    String summary =
        String.format(":Document Side: %s%n", this.getDocumentSide())
        + super.toString();
    return SummaryHelper.cleanSummary(summary);
  }
}
