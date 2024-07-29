---
title: US Bank Check OCR Java
---
The Java OCR SDK supports the [Bank Check API](https://platform.mindee.com/mindee/bank_check).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/bank_check/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Bank Check sample](https://github.com/mindee/client-lib-test-data/blob/main/products/bank_check/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.us.bankcheck.BankCheckV1;
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
    PredictResponse<BankCheckV1> response = mindeeClient.parse(
        BankCheckV1.class,
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
:Mindee ID: b9809586-57ae-4f84-a35d-a85b2be1f2a2
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/bank_check v1.0
:Rotation applied: Yes

Prediction
==========
:Check Issue Date: 2022-03-29
:Amount: 15332.90
:Payees: JOHN DOE
         JANE DOE
:Routing Number:
:Account Number: 7789778136
:Check Number: 0003401

Page Predictions
================

Page 0
------
:Check Position: Polygon with 21 points.
:Signature Positions: Polygon with 6 points.
:Check Issue Date: 2022-03-29
:Amount: 15332.90
:Payees: JOHN DOE
         JANE DOE
:Routing Number:
:Account Number: 7789778136
:Check Number: 0003401
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

### AmountField
An amount field `AmountField` extends `BaseField`, but also implements:
* **value** (`Double`): corresponds to the field value. Can be `null` if no value was extracted.

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
The following fields are extracted for Bank Check V1:

## Account Number
**accountNumber** : The check payer's account number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAccountNumber().value);
```

## Amount
**amount** : The amount of the check.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAmount().value);
```

## Check Number
**checkNumber** : The issuer's check number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCheckNumber().value);
```

## Check Position
[📄](#page-level-fields "This field is only present on individual pages.")**checkPosition** : The position of the check on the document.

```java
for (PositionField checkPositionElem : result.getDocument().getInference().getPrediction().getCheckPosition())
{
    System.out.println(checkPositionElem).polygon;
}
```

## Check Issue Date
**date** : The date the check was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDate().value);
```

## Payees
**payees** : List of the check's payees (recipients).

```java
for (payeesElem : result.getDocument().getInference().getPrediction().getPayees())
{
    System.out.println(payeesElem.value);
}
```

## Routing Number
**routingNumber** : The check issuer's routing number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getRoutingNumber().value);
```

## Signature Positions
[📄](#page-level-fields "This field is only present on individual pages.")**signaturesPositions** : List of signature positions

```java
for (Page page : result.getDocument().getInference().getPages())
{
    for (PositionField  signaturesPositionsElem : page.getSignaturesPositions())
    {
        System.out.println(signaturesPositionsElem.polygon);
        System.out.println(signaturesPositionsElem.quadrangle);
        System.out.println(signaturesPositionsElem.rectangle);
        System.out.println(signaturesPositionsElem.boundingBox);
    }
}
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
