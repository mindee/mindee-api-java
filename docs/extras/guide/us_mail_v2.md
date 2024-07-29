---
title: US US Mail OCR Java
---
The Java OCR SDK supports the [US Mail API](https://platform.mindee.com/mindee/us_mail).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/us_mail/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![US Mail sample](https://github.com/mindee/client-lib-test-data/blob/main/products/us_mail/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.us.usmail.UsMailV2;
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
    AsyncPredictResponse<UsMailV2> response = mindeeClient.enqueueAndParse(
        UsMailV2.class,
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
:Sender Name: zed
:Sender Address:
  :City: Dallas
  :Complete Address: 54321 Elm Street, Dallas, Texas ...
  :Postal Code: 54321
  :State: TX
  :Street: 54321 Elm Street
:Recipient Names: Jane Doe
:Recipient Addresses:
  +-----------------+-------------------------------------+-------------------+-------------+------------------------+-------+---------------------------+
  | City            | Complete Address                    | Is Address Change | Postal Code | Private Mailbox Number | State | Street                    |
  +=================+=====================================+===================+=============+========================+=======+===========================+
  | Detroit         | 1234 Market Street PMB 4321, Det... |                   | 12345       | 4321                   | MI    | 1234 Market Street        |
  +-----------------+-------------------------------------+-------------------+-------------+------------------------+-------+---------------------------+
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

### Recipient Addresses Field
The addresses of the recipients.

A `UsMailV2RecipientAddress` implements the following attributes:

* **city** (`String`): The city of the recipient's address.
* **complete** (`String`): The complete address of the recipient.
* **isAddressChange** (`Boolean`): Indicates if the recipient's address is a change of address.
* **postalCode** (`String`): The postal code of the recipient's address.
* **privateMailboxNumber** (`String`): The private mailbox number of the recipient's address.
* **state** (`String`): Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.
* **street** (`String`): The street of the recipient's address.
Fields which are specific to this product; they are not used in any other product.

### Sender Address Field
The address of the sender.

A `UsMailV2SenderAddress` implements the following attributes:

* **city** (`String`): The city of the sender's address.
* **complete** (`String`): The complete address of the sender.
* **postalCode** (`String`): The postal code of the sender's address.
* **state** (`String`): Second part of the ISO 3166-2 code, consisting of two letters indicating the US State.
* **street** (`String`): The street of the sender's address.

# Attributes
The following fields are extracted for US Mail V2:

## Recipient Addresses
**recipientAddresses** (List<[UsMailV2RecipientAddress](#recipient-addresses-field)>): The addresses of the recipients.

```java
for (recipientAddressesElem : result.getDocument().getInference().getPrediction().getRecipientAddresses())
{
    System.out.println(recipientAddressesElem.value);
}
```

## Recipient Names
**recipientNames** : The names of the recipients.

```java
for (recipientNamesElem : result.getDocument().getInference().getPrediction().getRecipientNames())
{
    System.out.println(recipientNamesElem.value);
}
```

## Sender Address
**senderAddress** ([UsMailV2SenderAddress](#sender-address-field)): The address of the sender.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSenderAddress().value);
```

## Sender Name
**senderName** : The name of the sender.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSenderName().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
