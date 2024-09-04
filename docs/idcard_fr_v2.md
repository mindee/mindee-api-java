---
title: FR Carte Nationale d'Identité OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-carte-nationale-didentite-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Carte Nationale d'Identité API](https://platform.mindee.com/mindee/idcard_fr).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/idcard_fr/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Carte Nationale d'Identité sample](https://github.com/mindee/client-lib-test-data/blob/main/products/idcard_fr/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.fr.idcard.IdCardV2;
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
    PredictResponse<IdCardV2> response = mindeeClient.parse(
        IdCardV2.class,
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
:Mindee ID: d33828f1-ef7e-4984-b9df-a2bfaa38a78d
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/idcard_fr v2.0
:Rotation applied: Yes

Prediction
==========
:Nationality:
:Card Access Number: 175775H55790
:Document Number:
:Given Name(s): Victor
                Marie
:Surname: DAMBARD
:Alternate Name:
:Date of Birth: 1994-04-24
:Place of Birth: LYON 4E ARRONDISSEM
:Gender: M
:Expiry Date: 2030-04-02
:Mrz Line 1: IDFRADAMBARD<<<<<<<<<<<<<<<<<<075025
:Mrz Line 2: 170775H557903VICTOR<<MARIE<9404246M5
:Mrz Line 3:
:Date of Issue: 2015-04-03
:Issuing Authority: SOUS-PREFECTURE DE BELLE (02)

Page Predictions
================

Page 0
------
:Document Type: OLD
:Document Sides: RECTO & VERSO
:Nationality:
:Card Access Number: 175775H55790
:Document Number:
:Given Name(s): Victor
                Marie
:Surname: DAMBARD
:Alternate Name:
:Date of Birth: 1994-04-24
:Place of Birth: LYON 4E ARRONDISSEM
:Gender: M
:Expiry Date: 2030-04-02
:Mrz Line 1: IDFRADAMBARD<<<<<<<<<<<<<<<<<<075025
:Mrz Line 2: 170775H557903VICTOR<<MARIE<9404246M5
:Mrz Line 3:
:Date of Issue: 2015-04-03
:Issuing Authority: SOUS-PREFECTURE DE BELLE (02)
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


### ClassificationField
The classification field `ClassificationField` extends `BaseField`, but also implements:
* **value** (`strong`): corresponds to the field value.
* **confidence** (`double`): the confidence score of the field prediction.

> Note: a classification field's `value is always a `String`.

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

### DateField
The date field `DateField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.

## Page-Level Fields
Some fields are constrained to the page level, and so will not be retrievable at document level.

# Attributes
The following fields are extracted for Carte Nationale d'Identité V2:

## Alternate Name
**alternateName**: The alternate name of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAlternateName().value);
```

## Issuing Authority
**authority**: The name of the issuing authority.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAuthority().value);
```

## Date of Birth
**birthDate**: The date of birth of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthDate().value);
```

## Place of Birth
**birthPlace**: The place of birth of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthPlace().value);
```

## Card Access Number
**cardAccessNumber**: The card access number (CAN).

```java
System.out.println(result.getDocument().getInference().getPrediction().getCardAccessNumber().value);
```

## Document Number
**documentNumber**: The document number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentNumber().value);
```

## Document Sides
[📄](#page-level-fields "This field is only present on individual pages.")**documentSide**: The sides of the document which are visible.

#### Possible values include:
 - RECTO
 - VERSO
 - RECTO & VERSO

```java
for (ClassificationField documentSideElem : result.getDocument().getInference().getPrediction().getDocumentSide())
{
    System.out.println(documentSideElem)
      .value;
}
```

## Document Type
[📄](#page-level-fields "This field is only present on individual pages.")**documentType**: The document type or format.

#### Possible values include:
 - NEW
 - OLD

```java
for (ClassificationField documentTypeElem : result.getDocument().getInference().getPrediction().getDocumentType())
{
    System.out.println(documentTypeElem)
      .value;
}
```

## Expiry Date
**expiryDate**: The expiry date of the identification card.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## Gender
**gender**: The gender of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getGender().value);
```

## Given Name(s)
**givenNames**: The given name(s) of the card holder.

```java
for (givenNamesElem : result.getDocument().getInference().getPrediction().getGivenNames())
{
    System.out.println(givenNamesElem.value);
}
```

## Date of Issue
**issueDate**: The date of issue of the identification card.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssueDate().value);
```

## Mrz Line 1
**mrz1**: The Machine Readable Zone, first line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz1().value);
```

## Mrz Line 2
**mrz2**: The Machine Readable Zone, second line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz2().value);
```

## Mrz Line 3
**mrz3**: The Machine Readable Zone, third line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz3().value);
```

## Nationality
**nationality**: The nationality of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getNationality().value);
```

## Surname
**surname**: The surname of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSurname().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
