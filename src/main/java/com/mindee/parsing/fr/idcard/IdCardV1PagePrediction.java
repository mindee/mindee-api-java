package com.mindee.parsing.fr.idcard;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.field.StringField;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class IdCardV1PagePrediction extends IdCardV1DocumentPrediction {

  /**
   * Indicates if it is the recto, the verso or the both of it.
   */
  @JsonProperty("document_side")
  private StringField documentSide;

  @Override
  public String toString() {

    String summary =
      String.format(":Document side: %s%n", this.getDocumentSide()) +
        super.toString();

    return SummaryHelper.cleanSummary(summary);
  }
}
