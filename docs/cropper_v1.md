---
title: Cropper OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-cropper-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Cropper API](https://platform.mindee.com/mindee/cropper).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/cropper/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Cropper sample](https://github.com/mindee/client-lib-test-data/blob/main/products/cropper/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.cropper.CropperV1;
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
    PredictResponse<CropperV1> response = mindeeClient.parse(
        CropperV1.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocument().toString());

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
:Mindee ID: 149ce775-8302-4798-8649-7eda9fb84a1a
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/cropper v1.0
:Rotation applied: No

Prediction
==========

Page Predictions
================

Page 0
------
:Document Cropper: Polygon with 26 points.
                   Polygon with 25 points.
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

## Page-Level Fields
Some fields are constrained to the page level, and so will not be retrievable at document level.

# Attributes
The following fields are extracted for Cropper V1:

## Document Cropper
[📄](#page-level-fields "This field is only present on individual pages.")**cropping**: List of documents found in the image.

```java
for (Page page : result.getDocument().getInference().getPages())
{
    for (PositionField  croppingElem : page.getCropping())
    {
        System.out.println(croppingElem.polygon);
        System.out.println(croppingElem.quadrangle);
        System.out.println(croppingElem.rectangle);
        System.out.println(croppingElem.boundingBox);
    }
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
