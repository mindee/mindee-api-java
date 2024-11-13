---
title: Business Card OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-business-card-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Business Card API](https://platform.mindee.com/mindee/business_card).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/business_card/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Business Card sample](https://github.com/mindee/client-lib-test-data/blob/main/products/business_card/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.businesscard.BusinessCardV1;
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
    AsyncPredictResponse<BusinessCardV1> response = mindeeClient.enqueueAndParse(
        BusinessCardV1.class,
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
:Mindee ID: 6f9a261f-7609-4687-9af0-46a45156566e
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/business_card v1.0
:Rotation applied: Yes

Prediction
==========
:Firstname: Andrew
:Lastname: Morin
:Job Title: Founder & CEO
:Company: RemoteGlobal
:Email: amorin@remoteglobalconsulting.com
:Phone Number: +14015555555
:Mobile Number: +13015555555
:Fax Number: +14015555556
:Address: 178 Main Avenue, Providence, RI 02111
:Website: www.remoteglobalconsulting.com
:Social Media: https://www.linkedin.com/in/johndoe
               https://twitter.com/johndoe
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
The following fields are extracted for Business Card V1:

## Address
**address**: The address of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAddress().value);
```

## Company
**company**: The company the person works for.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCompany().value);
```

## Email
**email**: The email address of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEmail().value);
```

## Fax Number
**faxNumber**: The Fax number of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getFaxNumber().value);
```

## Firstname
**firstname**: The given name of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getFirstname().value);
```

## Job Title
**jobTitle**: The job title of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getJobTitle().value);
```

## Lastname
**lastname**: The lastname of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getLastname().value);
```

## Mobile Number
**mobileNumber**: The mobile number of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMobileNumber().value);
```

## Phone Number
**phoneNumber**: The phone number of the person.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPhoneNumber().value);
```

## Social Media
**socialMedia**: The social media profiles of the person or company.

```java
for (socialMediaElem : result.getDocument().getInference().getPrediction().getSocialMedia())
{
    System.out.println(socialMediaElem.value);
}
```

## Website
**website**: The website of the person or company.

```java
System.out.println(result.getDocument().getInference().getPrediction().getWebsite().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
