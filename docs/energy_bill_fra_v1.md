---
title: FR Energy Bill OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-fr-energy-bill-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Energy Bill API](https://platform.mindee.com/mindee/energy_bill_fra).

The [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/energy_bill_fra/default_sample.jpg) can be used for testing purposes.
![Energy Bill sample](https://github.com/mindee/client-lib-test-data/blob/main/products/energy_bill_fra/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.fr.energybill.EnergyBillV1;
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
    AsyncPredictResponse<EnergyBillV1> response = mindeeClient.enqueueAndParse(
        EnergyBillV1.class,
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

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Energy Consumer Field
The entity that consumes the energy.

A `EnergyBillV1EnergyConsumer` implements the following attributes:

* **address** (`String`): The address of the energy consumer.
* **name** (`String`): The name of the energy consumer.
Fields which are specific to this product; they are not used in any other product.

### Energy Supplier Field
The company that supplies the energy.

A `EnergyBillV1EnergySupplier` implements the following attributes:

* **address** (`String`): The address of the energy supplier.
* **name** (`String`): The name of the energy supplier.
Fields which are specific to this product; they are not used in any other product.

### Energy Usage Field
Details of energy consumption.

A `EnergyBillV1EnergyUsage` implements the following attributes:

* **description** (`String`): Description or details of the energy usage.
* **endDate** (`String`): The end date of the energy usage.
* **startDate** (`String`): The start date of the energy usage.
* **taxRate** (`Double`): The rate of tax applied to the total cost.
* **total** (`Double`): The total cost of energy consumed.
* **unitPrice** (`Double`): The price per unit of energy consumed.
Fields which are specific to this product; they are not used in any other product.

### Meter Details Field
Information about the energy meter.

A `EnergyBillV1MeterDetail` implements the following attributes:

* **meterNumber** (`String`): The unique identifier of the energy meter.
* **meterType** (`String`): The type of energy meter.

#### Possible values include:
 - electricity
 - gas
 - water
 - None

* **unit** (`String`): The unit of measurement for energy consumption, which can be kW, m³, or L.
Fields which are specific to this product; they are not used in any other product.

### Subscription Field
The subscription details fee for the energy service.

A `EnergyBillV1Subscription` implements the following attributes:

* **description** (`String`): Description or details of the subscription.
* **endDate** (`String`): The end date of the subscription.
* **startDate** (`String`): The start date of the subscription.
* **taxRate** (`Double`): The rate of tax applied to the total cost.
* **total** (`Double`): The total cost of subscription.
* **unitPrice** (`Double`): The price per unit of subscription.
Fields which are specific to this product; they are not used in any other product.

### Taxes and Contributions Field
Details of Taxes and Contributions.

A `EnergyBillV1TaxesAndContribution` implements the following attributes:

* **description** (`String`): Description or details of the Taxes and Contributions.
* **endDate** (`String`): The end date of the Taxes and Contributions.
* **startDate** (`String`): The start date of the Taxes and Contributions.
* **taxRate** (`Double`): The rate of tax applied to the total cost.
* **total** (`Double`): The total cost of Taxes and Contributions.
* **unitPrice** (`Double`): The price per unit of Taxes and Contributions.

# Attributes
The following fields are extracted for Energy Bill V1:

## Contract ID
**contractId**: The unique identifier associated with a specific contract.

```java
System.out.println(result.getDocument().getInference().getPrediction().getContractId().value);
```

## Delivery Point
**deliveryPoint**: The unique identifier assigned to each electricity or gas consumption point. It specifies the exact location where the energy is delivered.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDeliveryPoint().value);
```

## Due Date
**dueDate**: The date by which the payment for the energy invoice is due.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDueDate().value);
```

## Energy Consumer
**energyConsumer**([EnergyBillV1EnergyConsumer](#energy-consumer-field)): The entity that consumes the energy.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEnergyConsumer().value);
```

## Energy Supplier
**energySupplier**([EnergyBillV1EnergySupplier](#energy-supplier-field)): The company that supplies the energy.

```java
System.out.println(result.getDocument().getInference().getPrediction().getEnergySupplier().value);
```

## Energy Usage
**energyUsage**(List<[EnergyBillV1EnergyUsage](#energy-usage-field)>): Details of energy consumption.

```java
for (energyUsageElem : result.getDocument().getInference().getPrediction().getEnergyUsage())
{
    System.out.println(energyUsageElem.value);
}
```

## Invoice Date
**invoiceDate**: The date when the energy invoice was issued.

```java
System.out.println(result.getDocument().getInference().getPrediction().getInvoiceDate().value);
```

## Invoice Number
**invoiceNumber**: The unique identifier of the energy invoice.

```java
System.out.println(result.getDocument().getInference().getPrediction().getInvoiceNumber().value);
```

## Meter Details
**meterDetails**([EnergyBillV1MeterDetail](#meter-details-field)): Information about the energy meter.

```java
System.out.println(result.getDocument().getInference().getPrediction().getMeterDetails().value);
```

## Subscription
**subscription**(List<[EnergyBillV1Subscription](#subscription-field)>): The subscription details fee for the energy service.

```java
for (subscriptionElem : result.getDocument().getInference().getPrediction().getSubscription())
{
    System.out.println(subscriptionElem.value);
}
```

## Taxes and Contributions
**taxesAndContributions**(List<[EnergyBillV1TaxesAndContribution](#taxes-and-contributions-field)>): Details of Taxes and Contributions.

```java
for (taxesAndContributionsElem : result.getDocument().getInference().getPrediction().getTaxesAndContributions())
{
    System.out.println(taxesAndContributionsElem.value);
}
```

## Total Amount
**totalAmount**: The total amount to be paid for the energy invoice.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalAmount().value);
```

## Total Before Taxes
**totalBeforeTaxes**: The total amount to be paid for the energy invoice before taxes.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalBeforeTaxes().value);
```

## Total Taxes
**totalTaxes**: Total of taxes applied to the invoice.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalTaxes().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
