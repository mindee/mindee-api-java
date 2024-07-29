---
title: US W9 OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-us-w9-ocr
---
The Java OCR SDK supports the [W9 API](https://platform.mindee.com/mindee/us_w9).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/us_w9/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![W9 sample](https://github.com/mindee/client-lib-test-data/blob/main/products/us_w9/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.us.w9.W9V1;
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
    PredictResponse<W9V1> response = mindeeClient.parse(
        W9V1.class,
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
:Mindee ID: d7c5b25f-e0d3-4491-af54-6183afa1aaab
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/us_w9 v1.0
:Rotation applied: Yes

Prediction
==========

Page Predictions
================

Page 0
------
:Name: Stephen W Hawking
:SSN: 560758145
:Address: Somewhere In Milky Way
:City State Zip: Probably Still At Cambridge P O Box CB1
:Business Name:
:EIN: 942203664
:Tax Classification: individual
:Tax Classification Other Details:
:W9 Revision Date: august 2013
:Signature Position: Polygon with 4 points.
:Signature Date Position:
:Tax Classification LLC:
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


### PositionField
The position field `PositionField` implements:

* **boundingBox** (`Polygon`): contains exactly 4 relative vertices (points) coordinates of a right rectangle containing the field in the document.
* **polygon** (`Polygon`): contains the relative vertices coordinates (`polygon` extends `List<Point>`) of a polygon containing the field in the image.
* **rectangle** (`Polygon`): a polygon with four points that may be oriented (even beyond canvas).
* **quadrangle** (`Polygon`): a free polygon made up of four points.

## Page-Level Fields
Some fields are constrained to the page level, and so will not be retrievable at document level.

# Attributes
The following fields are extracted for W9 V1:

## Address
[📄](#page-level-fields "This field is only present on individual pages.")**address**: The street address (number, street, and apt. or suite no.) of the applicant.

```java
for (StringField addressElem : result.getDocument().getInference().getPrediction().getAddress())
{
    System.out.println(addressElem)
      .value;
}
```

## Business Name
[📄](#page-level-fields "This field is only present on individual pages.")**businessName**: The business name or disregarded entity name, if different from Name.

```java
for (StringField businessNameElem : result.getDocument().getInference().getPrediction().getBusinessName())
{
    System.out.println(businessNameElem)
      .value;
}
```

## City State Zip
[📄](#page-level-fields "This field is only present on individual pages.")**cityStateZip**: The city, state, and ZIP code of the applicant.

```java
for (StringField cityStateZipElem : result.getDocument().getInference().getPrediction().getCityStateZip())
{
    System.out.println(cityStateZipElem)
      .value;
}
```

## EIN
[📄](#page-level-fields "This field is only present on individual pages.")**ein**: The employer identification number.

```java
for (StringField einElem : result.getDocument().getInference().getPrediction().getEin())
{
    System.out.println(einElem)
      .value;
}
```

## Name
[📄](#page-level-fields "This field is only present on individual pages.")**name**: Name as shown on the applicant's income tax return.

```java
for (StringField nameElem : result.getDocument().getInference().getPrediction().getName())
{
    System.out.println(nameElem)
      .value;
}
```

## Signature Date Position
[📄](#page-level-fields "This field is only present on individual pages.")**signatureDatePosition**: Position of the signature date on the document.

```java
for (PositionField signatureDatePositionElem : result.getDocument().getInference().getPrediction().getSignatureDatePosition())
{
    System.out.println(signatureDatePositionElem).polygon;
}
```

## Signature Position
[📄](#page-level-fields "This field is only present on individual pages.")**signaturePosition**: Position of the signature on the document.

```java
for (PositionField signaturePositionElem : result.getDocument().getInference().getPrediction().getSignaturePosition())
{
    System.out.println(signaturePositionElem).polygon;
}
```

## SSN
[📄](#page-level-fields "This field is only present on individual pages.")**ssn**: The applicant's social security number.

```java
for (StringField ssnElem : result.getDocument().getInference().getPrediction().getSsn())
{
    System.out.println(ssnElem)
      .value;
}
```

## Tax Classification
[📄](#page-level-fields "This field is only present on individual pages.")**taxClassification**: The federal tax classification, which can vary depending on the revision date.

```java
for (StringField taxClassificationElem : result.getDocument().getInference().getPrediction().getTaxClassification())
{
    System.out.println(taxClassificationElem)
      .value;
}
```

## Tax Classification LLC
[📄](#page-level-fields "This field is only present on individual pages.")**taxClassificationLlc**: Depending on revision year, among S, C, P or D for Limited Liability Company Classification.

```java
for (StringField taxClassificationLlcElem : result.getDocument().getInference().getPrediction().getTaxClassificationLlc())
{
    System.out.println(taxClassificationLlcElem)
      .value;
}
```

## Tax Classification Other Details
[📄](#page-level-fields "This field is only present on individual pages.")**taxClassificationOtherDetails**: Tax Classification Other Details.

```java
for (StringField taxClassificationOtherDetailsElem : result.getDocument().getInference().getPrediction().getTaxClassificationOtherDetails())
{
    System.out.println(taxClassificationOtherDetailsElem)
      .value;
}
```

## W9 Revision Date
[📄](#page-level-fields "This field is only present on individual pages.")**w9RevisionDate**: The Revision month and year of the W9 form.

```java
for (StringField w9RevisionDateElem : result.getDocument().getInference().getPrediction().getW9RevisionDate())
{
    System.out.println(w9RevisionDateElem)
      .value;
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
