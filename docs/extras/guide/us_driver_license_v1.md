---
title: US Driver License OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-us-driver-license-ocr
---
The Java OCR SDK supports the [Driver License API](https://platform.mindee.com/mindee/us_driver_license).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/us_driver_license/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Driver License sample](https://github.com/mindee/client-lib-test-data/blob/main/products/us_driver_license/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.us.driverlicense.DriverLicenseV1;
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
    PredictResponse<DriverLicenseV1> response = mindeeClient.parse(
        DriverLicenseV1.class,
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
:Mindee ID: bf70068d-d3d6-49dc-b93a-b4b7d156fc3d
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/us_driver_license v1.0
:Rotation applied: Yes

Prediction
==========
:State: AZ
:Driver License ID: D12345678
:Expiry Date: 2018-02-01
:Date Of Issue: 2013-01-10
:Last Name: SAMPLE
:First Name: JELANI
:Address: 123 MAIN STREET PHOENIX AZ 85007
:Date Of Birth: 1957-02-01
:Restrictions: NONE
:Endorsements: NONE
:Driver License Class: D
:Sex: M
:Height: 5-08
:Weight: 185
:Hair Color: BRO
:Eye Color: BRO
:Document Discriminator: 1234567890123456

Page Predictions
================

Page 0
------
:Photo: Polygon with 4 points.
:Signature: Polygon with 4 points.
:State: AZ
:Driver License ID: D12345678
:Expiry Date: 2018-02-01
:Date Of Issue: 2013-01-10
:Last Name: SAMPLE
:First Name: JELANI
:Address: 123 MAIN STREET PHOENIX AZ 85007
:Date Of Birth: 1957-02-01
:Restrictions: NONE
:Endorsements: NONE
:Driver License Class: D
:Sex: M
:Height: 5-08
:Weight: 185
:Hair Color: BRO
:Eye Color: BRO
:Document Discriminator: 1234567890123456
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


### PositionField
The position field `PositionField` implements:

* **boundingBox** (`Polygon`): contains exactly 4 relative vertices (points) coordinates of a right rectangle containing the field in the document.
* **polygon** (`Polygon`): contains the relative vertices coordinates (`polygon` extends `List<Point>`) of a polygon containing the field in the image.
* **rectangle** (`Polygon`): a polygon with four points that may be oriented (even beyond canvas).
* **quadrangle** (`Polygon`): a free polygon made up of four points.

## Page-Level Fields
Some fields are constrained to the page level, and so will not be retrievable at document level.

# Attributes
The following fields are extracted for Driver License V1:

## Address
**address**: US driver license holders address

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress().value);
```

## Date Of Birth
**dateOfBirth**: US driver license holders date of birth

```java
System.out.println(result.getDocument().getInference().getPrediction().getDateOfBirth().value);
```

## Document Discriminator
**ddNumber**: Document Discriminator Number of the US Driver License

```java
System.out.println(result.getDocument().getInference().getPrediction().getDdNumber().value);
```

## Driver License Class
**dlClass**: US driver license holders class

```java
System.out.println(result.getDocument().getInference().getPrediction().getDlClass().value);
```

## Driver License ID
**driverLicenseId**: ID number of the US Driver License.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDriverLicenseId().value);
```

## Endorsements
**endorsements**: US driver license holders endorsements

```java
System.out.println(result.getDocument().getInference().getPrediction().getEndorsements().value);
```

## Expiry Date
**expiryDate**: Date on which the documents expires.

```java
System.out.println(result.getDocument().getInference().getPrediction().getExpiryDate().value);
```

## Eye Color
**eyeColor**: US driver license holders eye colour

```java
System.out.println(result.getDocument().getInference().getPrediction().getEyeColor().value);
```

## First Name
**firstName**: US driver license holders first name(s)

```java
System.out.println(result.getDocument().getInference().getPrediction().getFirstName().value);
```

## Hair Color
**hairColor**: US driver license holders hair colour

```java
System.out.println(result.getDocument().getInference().getPrediction().getHairColor().value);
```

## Height
**height**: US driver license holders hight

```java
System.out.println(result.getDocument().getInference().getPrediction().getHeight().value);
```

## Date Of Issue
**issuedDate**: Date on which the documents was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuedDate().value);
```

## Last Name
**lastName**: US driver license holders last name

```java
System.out.println(result.getDocument().getInference().getPrediction().getLastName().value);
```

## Photo
[📄](#page-level-fields "This field is only present on individual pages.")**photo**: Has a photo of the US driver license holder

```java
for (PositionField photoElem : result.getDocument().getInference().getPrediction().getPhoto())
{
    System.out.println(photoElem).polygon;
}
```

## Restrictions
**restrictions**: US driver license holders restrictions

```java
System.out.println(result.getDocument().getInference().getPrediction().getRestrictions().value);
```

## Sex
**sex**: US driver license holders gender

```java
System.out.println(result.getDocument().getInference().getPrediction().getSex().value);
```

## Signature
[📄](#page-level-fields "This field is only present on individual pages.")**signature**: Has a signature of the US driver license holder

```java
for (PositionField signatureElem : result.getDocument().getInference().getPrediction().getSignature())
{
    System.out.println(signatureElem).polygon;
}
```

## State
**state**: US State

```java
System.out.println(result.getDocument().getInference().getPrediction().getState().value);
```

## Weight
**weight**: US driver license holders weight

```java
System.out.println(result.getDocument().getInference().getPrediction().getWeight().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
