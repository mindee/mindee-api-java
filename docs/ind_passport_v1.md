---
title: IND Passport OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-ind-passport-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Passport API](https://platform.mindee.com/mindee/ind_passport).

The [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/ind_passport/default_sample.jpg) can be used for testing purposes.
![Passport sample](https://github.com/mindee/client-lib-test-data/blob/main/products/ind_passport/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.ind.passport.PassportV1;
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
    AsyncPredictResponse<PassportV1> response = mindeeClient.enqueueAndParse(
        PassportV1.class,
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
The following fields are extracted for Passport V1:

## Address Line 1
**address1**: The first line of the address of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress1().value);
```

## Address Line 2
**address2**: The second line of the address of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress2().value);
```

## Address Line 3
**address3**: The third line of the address of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress3().value);
```

## Birth Date
**birthDate**: The birth date of the passport holder, ISO format: YYYY-MM-DD.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthDate().value);
```

## Birth Place
**birthPlace**: The birth place of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBirthPlace().value);
```

## Country
**country**: ISO 3166-1 alpha-3 country code (3 letters format).

```java
System.out.println(result.getDocument().getInference().getPrediction().getCountry().value);
```

## Expiry Date
**expiryDate**: The date when the passport will expire, ISO format: YYYY-MM-DD.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## File Number
**fileNumber**: The file number of the passport document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getFileNumber().value);
```

## Gender
**gender**: The gender of the passport holder.

#### Possible values include:
 - M
 - F

```java
System.out.println(result.getDocument().getInference().getPrediction().getGender().value);
```

## Given Names
**givenNames**: The given names of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getGivenNames().value);
```

## ID Number
**idNumber**: The identification number of the passport document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIdNumber().value);
```

## Issuance Date
**issuanceDate**: The date when the passport was issued, ISO format: YYYY-MM-DD.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuanceDate().value);
```

## Issuance Place
**issuancePlace**: The place where the passport was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuancePlace().value);
```

## Legal Guardian
**legalGuardian**: The name of the legal guardian of the passport holder (if applicable).

```java
System.out.println(result.getDocument().getInference().getPrediction().getLegalGuardian().value);
```

## MRZ Line 1
**mrz1**: The first line of the machine-readable zone (MRZ) of the passport document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz1().value);
```

## MRZ Line 2
**mrz2**: The second line of the machine-readable zone (MRZ) of the passport document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz2().value);
```

## Name of Mother
**nameOfMother**: The name of the mother of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getNameOfMother().value);
```

## Name of Spouse
**nameOfSpouse**: The name of the spouse of the passport holder (if applicable).

```java
System.out.println(result.getDocument().getInference().getPrediction().getNameOfSpouse().value);
```

## Old Passport Date of Issue
**oldPassportDateOfIssue**: The date of issue of the old passport (if applicable), ISO format: YYYY-MM-DD.

```java
System.out.println(result.getDocument().getInference().getPrediction().getOldPassportDateOfIssue().value);
```

## Old Passport Number
**oldPassportNumber**: The number of the old passport (if applicable).

```java
System.out.println(result.getDocument().getInference().getPrediction().getOldPassportNumber().value);
```

## Old Passport Place of Issue
**oldPassportPlaceOfIssue**: The place of issue of the old passport (if applicable).

```java
System.out.println(result.getDocument().getInference().getPrediction().getOldPassportPlaceOfIssue().value);
```

## Page Number
**pageNumber**: The page number of the passport document.

#### Possible values include:
 - 1
 - 2

```java
System.out.println(result.getDocument().getInference().getPrediction().getPageNumber().value);
```

## Surname
**surname**: The surname of the passport holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSurname().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
