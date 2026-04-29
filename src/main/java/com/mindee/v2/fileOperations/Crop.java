package com.mindee.v2.fileOperations;

import com.mindee.image.ExtractedImage;
import com.mindee.image.ExtractedImages;
import com.mindee.image.ImageExtractor;
import com.mindee.input.LocalInputSource;
import com.mindee.v2.product.crop.CropItem;
import java.io.IOException;
import java.util.List;

public class Crop {
  private final ImageExtractor imageExtractor;

  public Crop(LocalInputSource inputSource) throws IOException {
    this.imageExtractor = new ImageExtractor(inputSource);
  }

  public ExtractedImage extractSingle(CropItem cropItem) throws IOException {
    return this.imageExtractor
      .extractImage(cropItem.getLocation(), cropItem.getLocation().getPage(), 0);
  }

  public ExtractedImages extractMultiple(List<CropItem> cropItems) {
    var extractedImages = new ExtractedImages();
    for (int i = 0; i < cropItems.size(); i++) {
      var cropItem = cropItems.get(i);
      extractedImages
        .add(
          this.imageExtractor
            .extractImage(cropItem.getLocation(), cropItem.getLocation().getPage(), i + 1)
        );
    }
    return extractedImages;
  }
}
