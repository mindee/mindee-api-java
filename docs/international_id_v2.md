---
title: International ID OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-international-id-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [International ID API](https://platform.mindee.com/mindee/international_id).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/international_id/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![International ID sample](https://github.com/mindee/client-lib-test-data/blob/main/products/international_id/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.internationalid.InternationalIdV2;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    // Parse the file asynchronously
    AsyncPredictResponse<InternationalIdV2> response = mindeeClient.enqueueAndParse(
        InternationalIdV2.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocumentObj().toString());

    // Print the document-level predictions
//    System.out.println(response.getDocumentObj().getInference().getPrediction().toString());

    // Print the page-level predictions
//    response.getDocumentObj().getInference().getPages().forEach(
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
:Mindee ID: cfa20a58-20cf-43b6-8cec-9505fa69d1c2
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/international_id v2.0
:Rotation applied: No

Prediction
==========
:Document Type: IDENTIFICATION_CARD
:Document Number: 12345678A
:Surnames: MUESTRA
           MUESTRA
:Given Names: CARMEN
:Sex: F
:Birth Date: 1980-01-01
:Birth Place: CAMPO DE CRIPTANA CIUDAD REAL ESPANA
:Nationality: ESP
:Personal Number: BAB1834284<44282767Q0
:Country of Issue: ESP
:State of Issue: MADRID
:Issue Date:
:Expiration Date: 2030-01-01
:Address: C/REAL N13, 1 DCHA COLLADO VILLALBA MADRID MADRID MADRID
:MRZ Line 1: IDESPBAB1834284<44282767Q0<<<<
:MRZ Line 2: 8001010F1301017ESP<<<<<<<<<<<3
:MRZ Line 3: MUESTRA<MUESTRA<<CARMEN<<<<<<<
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

# Attributes
The following fields are extracted for International ID V2:

## Address
**address**: The physical address of the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress().value);
```

## Birth Date
**birthDate**: The date of birth of the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthDate().value);
```

## Birth Place
**birthPlace**: The place of birth of the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthPlace().value);
```

## Country of Issue
**countryOfIssue**: The country where the document was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCountryOfIssue().value);
```

## Document Number
**documentNumber**: The unique identifier assigned to the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentNumber().value);
```

## Document Type
**documentType**: The type of personal identification document.

> Possible values include:
> - IDENTIFICATION_CARD
> - PASSPORT
> - DRIVER_LICENSE
> - VISA
> - RESIDENCY_CARD
> - VOTER_REGISTRATION

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentType().value);
```

## Expiration Date
**expiryDate**: The date when the document becomes invalid.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## Given Names
**givenNames**: The list of the document holder's given names.

```java
for (givenNamesElem : result.getDocument().getInference().getPrediction().getGivenNames())
{
    System.out.println(givenNamesElem.value);
}
```

## Issue Date
**issueDate**: The date when the document was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssueDate().value);
```

## MRZ Line 1
**mrzLine1**: The Machine Readable Zone, first line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrzLine1().value);
```

## MRZ Line 2
**mrzLine2**: The Machine Readable Zone, second line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrzLine2().value);
```

## MRZ Line 3
**mrzLine3**: The Machine Readable Zone, third line.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrzLine3().value);
```

## Nationality
**nationality**: The country of citizenship of the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getNationality().value);
```

## Personal Number
**personalNumber**: The unique identifier assigned to the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPersonalNumber().value);
```

## Sex
**sex**: The biological sex of the document holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSex().value);
```

## State of Issue
**stateOfIssue**: The state or territory where the document was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getStateOfIssue().value);
```

## Surnames
**surnames**: The list of the document holder's family names.

```java
for (surnamesElem : result.getDocument().getInference().getPrediction().getSurnames())
{
    System.out.println(surnamesElem.value);
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
