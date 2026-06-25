package com.mindee.v2.fileoperations;

import com.mindee.image.ExtractedImage;
import com.mindee.image.ExtractedImages;
import com.mindee.image.ImageExtractor;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.product.crop.CropItem;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Crop {
  private final ImageExtractor imageExtractor;

  public Crop(LocalInputSource inputSource) throws IOException {
    this.imageExtractor = new ImageExtractor(inputSource);
  }

  public ExtractedImage extractSingleCrop(CropItem cropItem) {
    return this.imageExtractor
      .extractImage(cropItem.getLocation(), cropItem.getLocation().getPage(), 0);
  }

  public ExtractedImages extractMultipleCrops(List<CropItem> cropItems) {
    if (cropItems == null || cropItems.isEmpty()) {
      return new ExtractedImages();
    }

    // Group crops by page, preserving insertion order
    Map<Integer, List<CropItem>> cropsByPage = cropItems
      .stream()
      .collect(
        Collectors
          .groupingBy(
            item -> item.getLocation().getPage(),
            java.util.LinkedHashMap::new,
            Collectors.toList()
          )
      );

    var extractedImages = new ExtractedImages();
    cropsByPage
      .forEach(
        (page, pageCrops) -> IntStream
          .range(0, pageCrops.size())
          .forEach(
            elementId -> extractedImages
              .add(
                this.imageExtractor
                  .extractImage(pageCrops.get(elementId).getLocation(), page, elementId)
              )
          )
      );
    return extractedImages;
  }
}
