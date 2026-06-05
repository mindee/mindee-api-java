package com.mindee.v2.product.crop;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.image.ExtractedImages;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.fileoperations.Crop;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringJoiner;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Result of a crop utility inference.
 */
@Getter
@EqualsAndHashCode
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public final class CropResult {
  /**
   * List of objects and their cropping coordinates identified in the source document.
   */
  @JsonProperty("crops")
  private ArrayList<CropItem> crops;

  /**
   * Based on the crop results, extract the documents into individual files as an
   * {@link ExtractedImages} instance.
   */
  public ExtractedImages extractFromInputSource(LocalInputSource inputSource) throws IOException {
    var cropper = new Crop(inputSource);
    return cropper.extractMultipleCrops(this.crops);
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner("\n");
    joiner.add("Crops\n=====");
    for (CropItem item : crops) {
      joiner.add(item.toString());
    }
    return joiner.toString();
  }
}
