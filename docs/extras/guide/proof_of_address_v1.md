---
title: Proof of Address OCR Java
category: 631a062c3718850f3519b793
slug: java-proof-of-address-ocr
---
The Java OCR SDK supports the [Proof of Address API](https://platform.mindee.com/mindee/proof_of_address).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/proof_of_address/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Proof of Address sample](https://github.com/mindee/client-lib-test-data/blob/main/products/proof_of_address/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.proofofaddress.ProofOfAddressV1;
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
    PredictResponse<ProofOfAddressV1> response = mindeeClient.parse(
        ProofOfAddressV1.class,
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
:Mindee ID: 5d2361e9-405e-4fc1-8531-f92a3aef0c38
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/proof_of_address v1.1
:Rotation applied: Yes

Prediction
==========
:Locale: en; en; USD;
:Issuer Name: PPL ELECTRIC UTILITIES
:Issuer Company Registrations:
:Issuer Address: 2 NORTH 9TH STREET CPC-GENN1 ALLENTOWN.PA 18101-1175
:Recipient Name:
:Recipient Company Registrations:
:Recipient Address: 123 MAIN ST ANYTOWN,PA 18062
:Dates: 2011-07-27
        2011-07-06
        2011-08-03
        2011-07-27
        2011-06-01
        2011-07-01
        2010-07-01
        2010-08-01
        2011-07-01
        2009-08-01
        2010-07-01
        2011-07-27
:Date of Issue: 2011-07-27

Page Predictions
================

Page 0
------
:Locale: en; en; USD;
:Issuer Name: PPL ELECTRIC UTILITIES
:Issuer Company Registrations:
:Issuer Address: 2 NORTH 9TH STREET CPC-GENN1 ALLENTOWN.PA 18101-1175
:Recipient Name:
:Recipient Company Registrations:
:Recipient Address: 123 MAIN ST ANYTOWN,PA 18062
:Dates: 2011-07-27
        2011-07-06
        2011-08-03
        2011-07-27
        2011-06-01
        2011-07-01
        2010-07-01
        2010-08-01
        2011-07-01
        2009-08-01
        2010-07-01
        2011-07-27
:Date of Issue: 2011-07-27
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


### CompanyRegistrationField
Aside from the basic `BaseField` attributes, the company registration field `CompanyRegistrationField` also implements the following:

* **type** (`String`): the type of company.
* **value** (`String`): corresponds to the field value.
* **toTableLine()**: a method that formats the data to fit in a .rst display.

### StringField
The text field `StringField` extends `BaseField`, but also implements:
* **value** (`String`): corresponds to the field value.
* **rawValue** (`String`): corresponds to the raw value as it appears on the document.

### DateField
The date field `DateField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.

### LocaleField
The locale field `LocaleField` extends `BaseField`, but also implements:

* **value** (`LocalDate`): an accessible representation of the value as a Java object. Can be `null`.
* **language** (`String`): ISO 639-1 language code (e.g.: `en` for English). Can be `null`.
* **country** (`String`): ISO 3166-1 alpha-2 or ISO 3166-1 alpha-3 code for countries (e.g.: `GRB` or `GB` for "Great Britain"). Can be `null`.
* **currency** (`String`): ISO 4217 code for currencies (e.g.: `USD` for "US Dollars"). Can be `null`.

# Attributes
The following fields are extracted for Proof of Address V1:

## Date of Issue
**date**: The date the document was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDate().value);
```

## Dates
**dates**: List of dates found on the document.

```java
for (datesElem : result.getDocument().getInference().getPrediction().getDates())
{
    System.out.println(datesElem.value);
}
```

## Issuer Address
**issuerAddress**: The address of the document's issuer.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuerAddress().value);
```

## Issuer Company Registrations
**issuerCompanyRegistration**: List of company registrations found for the issuer.

```java
for (issuerCompanyRegistrationElem : result.getDocument().getInference().getPrediction().getIssuerCompanyRegistration())
{
    System.out.println(issuerCompanyRegistrationElem.value);
}
```

## Issuer Name
**issuerName**: The name of the person or company issuing the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIssuerName().value);
```

## Locale
**locale**: The locale detected on the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getLocale().value);
```

## Recipient Address
**recipientAddress**: The address of the recipient.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRecipientAddress().value);
```

## Recipient Company Registrations
**recipientCompanyRegistration**: List of company registrations found for the recipient.

```java
for (recipientCompanyRegistrationElem : result.getDocument().getInference().getPrediction().getRecipientCompanyRegistration())
{
    System.out.println(recipientCompanyRegistrationElem.value);
}
```

## Recipient Name
**recipientName**: The name of the person or company receiving the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRecipientName().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
