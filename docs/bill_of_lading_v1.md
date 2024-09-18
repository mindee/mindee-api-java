---
title: Bill of Lading OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-bill-of-lading-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Bill of Lading API](https://platform.mindee.com/mindee/bill_of_lading).

The [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/bill_of_lading/default_sample.jpg) can be used for testing purposes.
![Bill of Lading sample](https://github.com/mindee/client-lib-test-data/blob/main/products/bill_of_lading/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.billoflading.BillOfLadingV1;
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
    AsyncPredictResponse<BillOfLadingV1> response = mindeeClient.enqueueAndParse(
        BillOfLadingV1.class,
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

### Carrier Field
The shipping company responsible for transporting the goods.

A `BillOfLadingV1Carrier` implements the following attributes:

* **name** (`String`): The name of the carrier.
* **professionalNumber** (`String`): The professional number of the carrier.
* **scac** (`String`): The Standard Carrier Alpha Code (SCAC) of the carrier.
Fields which are specific to this product; they are not used in any other product.

### Consignee Field
The party to whom the goods are being shipped.

A `BillOfLadingV1Consignee` implements the following attributes:

* **address** (`String`): The address of the consignee.
* **email** (`String`): The  email of the shipper.
* **name** (`String`): The name of the consignee.
* **phone** (`String`): The phone number of the consignee.
Fields which are specific to this product; they are not used in any other product.

### Items Field
The goods being shipped.

A `BillOfLadingV1CarrierItem` implements the following attributes:

* **description** (`String`): A description of the item.
* **grossWeight** (`Double`): The gross weight of the item.
* **measurement** (`Double`): The measurement of the item.
* **measurementUnit** (`String`): The unit of measurement for the measurement.
* **quantity** (`Double`): The quantity of the item being shipped.
* **weightUnit** (`String`): The unit of measurement for weights.
Fields which are specific to this product; they are not used in any other product.

### Notify Party Field
The party to be notified of the arrival of the goods.

A `BillOfLadingV1NotifyParty` implements the following attributes:

* **address** (`String`): The address of the notify party.
* **email** (`String`): The  email of the shipper.
* **name** (`String`): The name of the notify party.
* **phone** (`String`): The phone number of the notify party.
Fields which are specific to this product; they are not used in any other product.

### Shipper Field
The party responsible for shipping the goods.

A `BillOfLadingV1Shipper` implements the following attributes:

* **address** (`String`): The address of the shipper.
* **email** (`String`): The  email of the shipper.
* **name** (`String`): The name of the shipper.
* **phone** (`String`): The phone number of the shipper.

# Attributes
The following fields are extracted for Bill of Lading V1:

## Bill of Lading Number
**billOfLadingNumber**: A unique identifier assigned to a Bill of Lading document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBillOfLadingNumber().value);
```

## Carrier
**carrier**([BillOfLadingV1Carrier](#carrier-field)): The shipping company responsible for transporting the goods.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCarrier().value);
```

## Items
**carrierItems**(List<[BillOfLadingV1CarrierItem](#items-field)>): The goods being shipped.

```java
for (carrierItemsElem : result.getDocument().getInference().getPrediction().getCarrierItems())
{
    System.out.println(carrierItemsElem.value);
}
```

## Consignee
**consignee**([BillOfLadingV1Consignee](#consignee-field)): The party to whom the goods are being shipped.

```java
System.out.println(result.getDocument().getInference().getPrediction().getConsignee().value);
```

## Date of issue
**dateOfIssue**: The date when the bill of lading is issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDateOfIssue().value);
```

## Departure Date
**departureDate**: The date when the vessel departs from the port of loading.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDepartureDate().value);
```

## Notify Party
**notifyParty**([BillOfLadingV1NotifyParty](#notify-party-field)): The party to be notified of the arrival of the goods.

```java
System.out.println(result.getDocument().getInference().getPrediction().getNotifyParty().value);
```

## Place of Delivery
**placeOfDelivery**: The place where the goods are to be delivered.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPlaceOfDelivery().value);
```

## Port of Discharge
**portOfDischarge**: The port where the goods are unloaded from the vessel.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPortOfDischarge().value);
```

## Port of Loading
**portOfLoading**: The port where the goods are loaded onto the vessel.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPortOfLoading().value);
```

## Shipper
**shipper**([BillOfLadingV1Shipper](#shipper-field)): The party responsible for shipping the goods.

```java
System.out.println(result.getDocument().getInference().getPrediction().getShipper().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
