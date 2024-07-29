---
title: US Healthcare Card OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-us-healthcare-card-ocr
---
The Java OCR SDK supports the [Healthcare Card API](https://platform.mindee.com/mindee/us_healthcare_cards).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/us_healthcare_cards/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Healthcare Card sample](https://github.com/mindee/client-lib-test-data/blob/main/products/us_healthcare_cards/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.us.healthcarecard.HealthcareCardV1;
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
    AsyncPredictResponse<HealthcareCardV1> response = mindeeClient.enqueueAndParse(
        HealthcareCardV1.class,
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
:Mindee ID: 0ced9f49-00c0-4a1d-8221-4a1538813a95
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/us_healthcare_cards v1.0
:Rotation applied: No

Prediction
==========
:Company Name: UnitedHealthcare
:Member Name: SUBSCRIBER SMITH
:Member ID: 123456789
:Issuer 80840:
:Dependents: SPOUSE SMITH
             CHILD1 SMITH
             CHILD2 SMITH
             CHILD3 SMITH
:Group Number: 98765
:Payer ID: 87726
:RX BIN: 610279
:RX GRP: UHEALTH
:RX PCN: 9999
:copays:
  +--------------+--------------+
  | Service Fees | Service Name |
  +==============+==============+
  | 20.00        | office visit |
  +--------------+--------------+
  | 300.00       | emergency    |
  +--------------+--------------+
  | 75.00        | urgent care  |
  +--------------+--------------+
  | 30.00        | specialist   |
  +--------------+--------------+
:Enrollment Date: 2023-09-13
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

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### copays Field
Is a fixed amount for a covered service.

A `HealthcareCardV1Copay` implements the following attributes:

* **serviceFees** (`Double`): The price of service.
* **serviceName** (`String`): The name of service of the copay.

# Attributes
The following fields are extracted for Healthcare Card V1:

## Company Name
**companyName** : The name of the company that provides the healthcare plan.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCompanyName().value);
```

## copays
**copays** (List<[HealthcareCardV1Copay](#copays-field)>): Is a fixed amount for a covered service.

```java
for (copaysElem : result.getDocument().getInference().getPrediction().getCopays())
{
    System.out.println(copaysElem.value);
}
```

## Dependents
**dependents** : The list of dependents covered by the healthcare plan.

```java
for (dependentsElem : result.getDocument().getInference().getPrediction().getDependents())
{
    System.out.println(dependentsElem.value);
}
```

## Enrollment Date
**enrollmentDate** : The date when the member enrolled in the healthcare plan.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEnrollmentDate().value);
```

## Group Number
**groupNumber** : The group number associated with the healthcare plan.

```java
System.out.println(result.getDocument().getInference().getPrediction().getGroupNumber().value);
```

## Issuer 80840
**issuer80840** : The organization that issued the healthcare plan.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuer80840().value);
```

## Member ID
**memberId** : The unique identifier for the member in the healthcare system.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMemberId().value);
```

## Member Name
**memberName** : The name of the member covered by the healthcare plan.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMemberName().value);
```

## Payer ID
**payerId** : The unique identifier for the payer in the healthcare system.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPayerId().value);
```

## RX BIN
**rxBin** : The BIN number for prescription drug coverage.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRxBin().value);
```

## RX GRP
**rxGrp** : The group number for prescription drug coverage.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRxGrp().value);
```

## RX PCN
**rxPcn** : The PCN number for prescription drug coverage.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRxPcn().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
