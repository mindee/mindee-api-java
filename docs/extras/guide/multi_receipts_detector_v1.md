---
title: Multi Receipts Detector OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-multi-receipts-detector-ocr
---
The Java OCR SDK supports the [Multi Receipts Detector API](https://platform.mindee.com/mindee/multi_receipts_detector).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/multi_receipts_detector/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Multi Receipts Detector sample](https://github.com/mindee/client-lib-test-data/blob/main/products/multi_receipts_detector/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.multireceiptsdetector.MultiReceiptsDetectorV1;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(filePath);

    // Parse the file
    PredictResponse<MultiReceiptsDetectorV1> response = mindeeClient.parse(
        MultiReceiptsDetectorV1.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocument().toString());

    // Print the document-level predictions
//    System.out.println(response.getDocument().getInference().getPrediction().toString());

    // Print the page-level predictions
//    response.getDocument().getInference().getPages().forEach(
//        page -> System.out.println(page.toString())
//    );
  }

}

```

**Output (RST):**
```rst
########
Document
########
:Mindee ID: d7c5b25f-e0d3-4491-af54-6183afa1aaab
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/multi_receipts_detector v1.0
:Rotation applied: Yes

Prediction
==========
:List of Receipts: Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.

Page Predictions
================

Page 0
------
:List of Receipts: Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
                   Polygon with 4 points.
```

# Field Types
## Standard Fields
These fields are generic and used in several products.

### BaseField
Each prediction object contains a set of fields that inherit from the generic `BaseField` class.
A typical `BaseField` object will have the following attributes:

* **confidence** (`Double`): the confidence score of the field prediction.
* **boundingBox** (`Polygon`): contains exactly 4 relative vertices (points) coordinates of a right rectangle containing the field in the document.
* **polygon** (`Polygon`): contains the relative vertices coordinates (`polygon` extends `List<Point>`) of a polygon containing the field in the image.
* **pageId** (`Integer`): the ID of the page, always `null` when at document-level.

> **Note:** A `Point` simply refers to a List of `Double`.


Aside from the previous attributes, all basic fields have access to a custom `toString` method that can be used to print their value as a string.


### PositionField
The position field `PositionField` implements:

* **boundingBox** (`Polygon`): contains exactly 4 relative vertices (points) coordinates of a right rectangle containing the field in the document.
* **polygon** (`Polygon`): contains the relative vertices coordinates (`polygon` extends `List<Point>`) of a polygon containing the field in the image.
* **rectangle** (`Polygon`): a polygon with four points that may be oriented (even beyond canvas).
* **quadrangle** (`Polygon`): a free polygon made up of four points.

# Attributes
The following fields are extracted for Multi Receipts Detector V1:

## List of Receipts
**receipts**: Positions of the receipts on the document.

```java
for (receiptsElem : result.getDocument().getInference().getPrediction().getReceipts())
{
    System.out.println(receiptsElem.polygon);
    System.out.println(receiptsElem.quadrangle);
    System.out.println(receiptsElem.rectangle);
    System.out.println(receiptsElem.boundingBox);
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
