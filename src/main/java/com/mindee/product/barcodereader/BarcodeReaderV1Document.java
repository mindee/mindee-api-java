package com.mindee.product.barcodereader;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * Document data for Barcode Reader, API version 1.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
public class BarcodeReaderV1Document {

  /**
   * List of decoded 1D barcodes.
   */
  @JsonProperty("codes_1d")
  private List<StringField> codes1D = new ArrayList<>();
  /**
   * List of decoded 2D barcodes.
   */
  @JsonProperty("codes_2d")
  private List<StringField> codes2D = new ArrayList<>();

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    String codes1D = SummaryHelper.arrayToString(
        this.getCodes1D(),
        "%n              "
    );
    outStr.append(
        String.format(":Barcodes 1D: %s%n", codes1D)
    );
    String codes2D = SummaryHelper.arrayToString(
        this.getCodes2D(),
        "%n              "
    );
    outStr.append(
        String.format(":Barcodes 2D: %s%n", codes2D)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
