---
title: Receipt OCR Java
---
The Java OCR SDK supports the [Receipt API](https://platform.mindee.com/mindee/expense_receipts).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/expense_receipts/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Receipt sample](https://github.com/mindee/client-lib-test-data/blob/main/products/expense_receipts/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.receipt.ReceiptV5;
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
    PredictResponse<ReceiptV5> response = mindeeClient.parse(
        ReceiptV5.class,
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
:Mindee ID: d96fb043-8fb8-4adc-820c-387aae83376d
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/expense_receipts v5.3
:Rotation applied: Yes

Prediction
==========
:Expense Locale: en-GB; en; GB; GBP;
:Purchase Category: food
:Purchase Subcategory: restaurant
:Document Type: EXPENSE RECEIPT
:Purchase Date: 2016-02-26
:Purchase Time: 15:20
:Total Amount: 10.20
:Total Net: 8.50
:Total Tax: 1.70
:Tip and Gratuity:
:Taxes:
  +---------------+--------+----------+---------------+
  | Base          | Code   | Rate (%) | Amount        |
  +===============+========+==========+===============+
  | 8.50          | VAT    | 20.00    | 1.70          |
  +---------------+--------+----------+---------------+
:Supplier Name: clachan
:Supplier Company Registrations: Type: VAT NUMBER, Value: 232153895
                                 Type: VAT NUMBER, Value: 232153895
:Supplier Address: 34 Kingley Street W1B 50H
:Supplier Phone Number: 02074940834
:Receipt Number: 54/7500
:Line Items:
  +--------------------------------------+----------+--------------+------------+
  | Description                          | Quantity | Total Amount | Unit Price |
  +======================================+==========+==============+============+
  | Meantime Pale                        | 2.00     | 10.20        |            |
  +--------------------------------------+----------+--------------+------------+

Page Predictions
================

Page 0
------
:Expense Locale: en-GB; en; GB; GBP;
:Purchase Category: food
:Purchase Subcategory: restaurant
:Document Type: EXPENSE RECEIPT
:Purchase Date: 2016-02-26
:Purchase Time: 15:20
:Total Amount: 10.20
:Total Net: 8.50
:Total Tax: 1.70
:Tip and Gratuity:
:Taxes:
  +---------------+--------+----------+---------------+
  | Base          | Code   | Rate (%) | Amount        |
  +===============+========+==========+===============+
  | 8.50          | VAT    | 20.00    | 1.70          |
  +---------------+--------+----------+---------------+
:Supplier Name: clachan
:Supplier Company Registrations: Type: VAT NUMBER, Value: 232153895
                                 Type: VAT NUMBER, Value: 232153895
:Supplier Address: 34 Kingley Street W1B 50H
:Supplier Phone Number: 02074940834
:Receipt Number: 54/7500
:Line Items:
  +--------------------------------------+----------+--------------+------------+
  | Description                          | Quantity | Total Amount | Unit Price |
  +======================================+==========+==============+============+
  | Meantime Pale                        | 2.00     | 10.20        |            |
  +--------------------------------------+----------+--------------+------------+
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


### ClassificationField
The classification field `ClassificationField` extends `BaseField`, but also implements:
* **value** (`strong`): corresponds to the field value.
* **confidence** (`double`): the confidence score of the field prediction.

> Note: a classification field's `value is always a `String`.


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

### Taxes
#### TaxField
Aside from the basic `BaseField` attributes, the tax field `TaxField` also implements the following:

* **rate** (`Double`): the tax rate applied to an item expressed as a percentage. Can be `null`.
* **code** (`String`): tax code (or equivalent, depending on the origin of the document).
* **base** (`Double`): base amount used for the tax. Can be `null`.
* **value** (`Double`): the value of the tax. Can be `null`.

> Note: currently `TaxField` is not used on its own, and is accessed through a parent `Taxes` object, a list-like structure.

#### Taxes (List)
The `Taxes` field represents a List of `TaxField` objects. As it is the representation of several objects, it has access to a custom `toString` method that can render a `TaxField` object as a table line.

## Specific Fields
Fields which are specific to this product; they are not used in any other product.

### Line Items Field
List of line item details.

A `ReceiptV5LineItem` implements the following attributes:

* **description** (`String`): The item description.
* **quantity** (`Double`): The item quantity.
* **totalAmount** (`Double`): The item total amount.
* **unitPrice** (`Double`): The item unit price.

# Attributes
The following fields are extracted for Receipt V5:

## Purchase Category
**category** : The purchase category among predefined classes.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCategory().value);
```

## Purchase Date
**date** : The date the purchase was made.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDate().value);
```

## Document Type
**documentType** : One of: 'CREDIT CARD RECEIPT', 'EXPENSE RECEIPT'.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentType().value);
```

## Line Items
**lineItems** (List<[ReceiptV5LineItem](#line-items-field)>): List of line item details.

```java
for (lineItemsElem : result.getDocument().getInference().getPrediction().getLineItems())
{
    System.out.println(lineItemsElem.value);
}
```

## Expense Locale
**locale** : The locale detected on the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getLocale().value);
```

## Receipt Number
**receiptNumber** : The receipt number or identifier.

```java
System.out.println(result.getDocument().getInference().getPrediction().getReceiptNumber().value);
```

## Purchase Subcategory
**subcategory** : The purchase subcategory among predefined classes for transport and food.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSubcategory().value);
```

## Supplier Address
**supplierAddress** : The address of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierAddress().value);
```

## Supplier Company Registrations
**supplierCompanyRegistrations** : List of company registrations associated to the supplier.

```java
for (supplierCompanyRegistrationsElem : result.getDocument().getInference().getPrediction().getSupplierCompanyRegistrations())
{
    System.out.println(supplierCompanyRegistrationsElem.value);
}
```

## Supplier Name
**supplierName** : The name of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierName().value);
```

## Supplier Phone Number
**supplierPhoneNumber** : The phone number of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierPhoneNumber().value);
```

## Taxes
**taxes** : List of tax lines information.

```java
for (taxesElem : result.getDocument().getInference().getPrediction().getTaxes())
{
    System.out.println(taxesElem.value);
}
```

## Purchase Time
**time** : The time the purchase was made.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTime().value);
```

## Tip and Gratuity
**tip** : The total amount of tip and gratuity.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTip().value);
```

## Total Amount
**totalAmount** : The total amount paid: includes taxes, discounts, fees, tips, and gratuity.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalAmount().value);
```

## Total Net
**totalNet** : The net amount paid: does not include taxes, fees, and discounts.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalNet().value);
```

## Total Tax
**totalTax** : The total amount of taxes.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalTax().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
