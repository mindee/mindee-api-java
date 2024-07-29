---
title: FR Bank Account Details OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-bank-account-details-ocr
---
The Java OCR SDK supports the [Bank Account Details API](https://platform.mindee.com/mindee/bank_account_details).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/bank_account_details/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Bank Account Details sample](https://github.com/mindee/client-lib-test-data/blob/main/products/bank_account_details/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.fr.bankaccountdetails.BankAccountDetailsV2;
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
    PredictResponse<BankAccountDetailsV2> response = mindeeClient.parse(
        BankAccountDetailsV2.class,
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
:Mindee ID: bc8f7265-8dab-49fe-810c-d50049605578
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/bank_account_details v2.0
:Rotation applied: Yes

Prediction
==========
:Account Holder's Names: MME HEGALALDIA L ENVOL
:Basic Bank Account Number:
  :Bank Code: 13335
  :Branch Code: 00040
  :Key: 06
  :Account Number: 08932891361
:IBAN: FR7613335000400893289136106
:SWIFT Code: CEPAFRPP333

Page Predictions
================

Page 0
------
:Account Holder's Names: MME HEGALALDIA L ENVOL
:Basic Bank Account Number:
  :Bank Code: 13335
  :Branch Code: 00040
  :Key: 06
  :Account Number: 08932891361
:IBAN: FR7613335000400893289136106
:SWIFT Code: CEPAFRPP333
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

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Basic Bank Account Number Field
Full extraction of BBAN, including: branch code, bank code, account and key.

A `BankAccountDetailsV2Bban` implements the following attributes:

* **bbanBankCode** (`String`): The BBAN bank code outputted as a string.
* **bbanBranchCode** (`String`): The BBAN branch code outputted as a string.
* **bbanKey** (`String`): The BBAN key outputted as a string.
* **bbanNumber** (`String`): The BBAN Account number outputted as a string.

# Attributes
The following fields are extracted for Bank Account Details V2:

## Account Holder's Names
**accountHoldersNames** : Full extraction of the account holders names.

```java
System.out.println(result.getDocument().getInference().getPrediction().getAccountHoldersNames().value);
```

## Basic Bank Account Number
**bban** ([BankAccountDetailsV2Bban](#basic-bank-account-number-field)): Full extraction of BBAN, including: branch code, bank code, account and key.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBban().value);
```

## IBAN
**iban** : Full extraction of the IBAN number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getIban().value);
```

## SWIFT Code
**swiftCode** : Full extraction of the SWIFT code.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSwiftCode().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
