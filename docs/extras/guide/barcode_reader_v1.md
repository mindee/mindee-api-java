---
title: Barcode Reader OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-barcode-reader-ocr
---
The Java OCR SDK supports the [Barcode Reader API](https://platform.mindee.com/mindee/barcode_reader).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/barcode_reader/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Barcode Reader sample](https://github.com/mindee/client-lib-test-data/blob/main/products/barcode_reader/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.barcodereader.BarcodeReaderV1;
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
    PredictResponse<BarcodeReaderV1> response = mindeeClient.parse(
        BarcodeReaderV1.class,
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
:Mindee ID: f9c48da1-a306-4805-8da8-f7231fda2d88
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/barcode_reader v1.0
:Rotation applied: Yes

Prediction
==========
:Barcodes 1D: Mindee
:Barcodes 2D: https://developers.mindee.com/docs/barcode-reader-ocr
              I love paperwork! - Said no one ever

Page Predictions
================

Page 0
------
:Barcodes 1D: Mindee
:Barcodes 2D: https://developers.mindee.com/docs/barcode-reader-ocr
              I love paperwork! - Said no one ever
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

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

# Attributes
The following fields are extracted for Barcode Reader V1:

## Barcodes 1D
**codes1D**: List of decoded 1D barcodes.

```java
for (codes1DElem : result.getDocument().getInference().getPrediction().getCodes1D())
{
    System.out.println(codes1DElem.value);
}
```

## Barcodes 2D
**codes2D**: List of decoded 2D barcodes.

```java
for (codes2DElem : result.getDocument().getInference().getPrediction().getCodes2D())
{
    System.out.println(codes2DElem.value);
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
