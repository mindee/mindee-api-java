---
title: FR Health Card OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-health-card-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Health Card API](https://platform.mindee.com/mindee/french_healthcard).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/french_healthcard/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Health Card sample](https://github.com/mindee/client-lib-test-data/blob/main/products/french_healthcard/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.fr.healthcard.HealthCardV1;
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
    AsyncPredictResponse<HealthCardV1> response = mindeeClient.enqueueAndParse(
        HealthCardV1.class,
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
:Mindee ID: 9ee2733d-933a-4dcd-a73a-a31395e3b288
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/french_healthcard v1.0
:Rotation applied: Yes

Prediction
==========
:Given Name(s): NATHALIE
:Surname: DURAND
:Social Security Number: 2 69 05 49 588 157 80
:Issuance Date: 2007-01-01
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
The following fields are extracted for Health Card V1:

## Given Name(s)
**givenNames**: The given names of the card holder.

```java
for (givenNamesElem : result.getDocument().getInference().getPrediction().getGivenNames())
{
    System.out.println(givenNamesElem.value);
}
```

## Issuance Date
**issuanceDate**: The date when the carte vitale document was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuanceDate().value);
```

## Social Security Number
**socialSecurity**: The social security number of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSocialSecurity().value);
```

## Surname
**surname**: The surname of the card holder.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSurname().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
