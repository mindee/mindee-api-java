---
title: Driver License OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-driver-license-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Driver License API](https://platform.mindee.com/mindee/driver_license).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/driver_license/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Driver License sample](https://github.com/mindee/client-lib-test-data/blob/main/products/driver_license/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.driverlicense.DriverLicenseV1;
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
    AsyncPredictResponse<DriverLicenseV1> response = mindeeClient.enqueueAndParse(
        DriverLicenseV1.class,
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
:Mindee ID: fbdeae38-ada3-43ac-aa58-e01a3d47e474
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/driver_license v1.0
:Rotation applied: Yes

Prediction
==========
:Country Code: USA
:State: AZ
:ID: D12345678
:Category: D
:Last Name: Sample
:First Name: Jelani
:Date of Birth: 1957-02-01
:Place of Birth:
:Expiry Date: 2018-02-01
:Issued Date: 2013-01-10
:Issuing Authority:
:MRZ:
:DD Number: DD1234567890123456
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

### DateField
The date field `DateField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.

# Attributes
The following fields are extracted for Driver License V1:

## Category
**category**: The category or class of the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCategory().value);
```

## Country Code
**countryCode**: The alpha-3 ISO 3166 code of the country where the driver license was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCountryCode().value);
```

## Date of Birth
**dateOfBirth**: The date of birth of the driver license holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDateOfBirth().value);
```

## DD Number
**ddNumber**: The DD number of the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDdNumber().value);
```

## Expiry Date
**expiryDate**: The expiry date of the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## First Name
**firstName**: The first name of the driver license holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getFirstName().value);
```

## ID
**id**: The unique identifier of the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getId().value);
```

## Issued Date
**issuedDate**: The date when the driver license was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuedDate().value);
```

## Issuing Authority
**issuingAuthority**: The authority that issued the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuingAuthority().value);
```

## Last Name
**lastName**: The last name of the driver license holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getLastName().value);
```

## MRZ
**mrz**: The Machine Readable Zone (MRZ) of the driver license.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMrz().value);
```

## Place of Birth
**placeOfBirth**: The place of birth of the driver license holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPlaceOfBirth().value);
```

## State
**state**: Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.

```java
System.out.println(result.getDocument().getInference().getPrediction().getState().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
