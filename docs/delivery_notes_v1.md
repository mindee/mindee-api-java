---
title: Delivery note OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-delivery-note-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Delivery note API](https://platform.mindee.com/mindee/delivery_notes).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/delivery_notes/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Delivery note sample](https://github.com/mindee/client-lib-test-data/blob/main/products/delivery_notes/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.deliverynote.DeliveryNoteV1;
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
    AsyncPredictResponse<DeliveryNoteV1> response = mindeeClient.enqueueAndParse(
        DeliveryNoteV1.class,
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
:Mindee ID: d5ead821-edec-4d31-a69a-cf3998d9a506
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/delivery_notes v1.0
:Rotation applied: Yes

Prediction
==========
:Delivery Date: 2019-10-02
:Delivery Number: INT-001
:Supplier Name: John Smith
:Supplier Address: 4490 Oak Drive, Albany, NY 12210
:Customer Name: Jessie M Horne
:Customer Address: 4312 Wood Road, New York, NY 10031
:Total Amount: 204.75
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
The following fields are extracted for Delivery note V1:

## Customer Address
**customerAddress**: The Customer Address field is used to store the address of the customer receiving the goods.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCustomerAddress().value);
```

## Customer Name
**customerName**: The Customer Name field is used to store the name of the customer who is receiving the goods.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCustomerName().value);
```

## Delivery Date
**deliveryDate**: Delivery Date is the date when the goods are expected to be delivered to the customer.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDeliveryDate().value);
```

## Delivery Number
**deliveryNumber**: Delivery Number is a unique identifier for a Global Delivery Note document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDeliveryNumber().value);
```

## Supplier Address
**supplierAddress**: The Supplier Address field is used to store the address of the supplier from whom the goods were purchased.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierAddress().value);
```

## Supplier Name
**supplierName**: Supplier Name field is used to capture the name of the supplier from whom the goods are being received.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierName().value);
```

## Total Amount
**totalAmount**: Total Amount field is the sum of all line items on the Global Delivery Note.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalAmount().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
