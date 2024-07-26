---
title: Passport OCR Java
---
The Java OCR SDK supports the [Passport API](https://platform.mindee.com/mindee/passport).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/passport/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Passport sample](https://github.com/mindee/client-lib-test-data/blob/main/products/passport/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.passport.PassportV1;
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
    PredictResponse<PassportV1> response = mindeeClient.parse(
        PassportV1.class,
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
:Mindee ID: 18e41f6c-16cd-4f8e-8cd2-00ca02a35764
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/passport v1.0
:Rotation applied: Yes

Prediction
==========
:Country Code: GBR
:ID Number: 707797979
:Given Name(s): HENERT
:Surname: PUDARSAN
:Date of Birth: 1995-05-20
:Place of Birth: CAMTETH
:Gender: M
:Date of Issue: 2012-04-22
:Expiry Date: 2017-04-22
:MRZ Line 1: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<
:MRZ Line 2: 7077979792GBR9505209M1704224<<<<<<<<<<<<<<00

Page Predictions
================

Page 0
------
:Country Code: GBR
:ID Number: 707797979
:Given Name(s): HENERT
:Surname: PUDARSAN
:Date of Birth: 1995-05-20
:Place of Birth: CAMTETH
:Gender: M
:Date of Issue: 2012-04-22
:Expiry Date: 2017-04-22
:MRZ Line 1: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<
:MRZ Line 2: 7077979792GBR9505209M1704224<<<<<<<<<<<<<<00
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
* **pageId** (`Integer`): the ID of the page, is `null` when at document-level.

> **Note:** A `Point` simply refers to a List of `Double`.


Aside from the previous attributes, all basic fields have access to a custom `toString` method that can be used to print their value as a string.

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

### DateField
The date field `DateField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.

# Attributes
The following fields are extracted for Passport V1:

## Date of Birth
**birthDate** : The date of birth of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthDate().value);
```

## Place of Birth
**birthPlace** : The place of birth of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthPlace().value);
```

## Country Code
**country** : The country's 3 letter code (ISO 3166-1 alpha-3).

```java
System.out.println(result.getDocument().getInference().getPrediction().getCountry().value);
```

## Expiry Date
**expiryDate** : The expiry date of the passport.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## Gender
**gender** : The gender of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getGender().value);
```

## Given Name(s)
**givenNames** : The given name(s) of the passport holder.

```java
for (givenNamesElem : result.getDocument().getInference().getPrediction().getGivenNames())
{
    System.out.println(givenNamesElem.value);
}
```

## ID Number
**idNumber** : The passport's identification number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIdNumber().value);
```

## Date of Issue
**issuanceDate** : The date the passport was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuanceDate().value);
```

## MRZ Line 1
**mrz1** : Machine Readable Zone, first line

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz1().value);
```

## MRZ Line 2
**mrz2** : Machine Readable Zone, second line

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz2().value);
```

## Surname
**surname** : The surname of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSurname().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
