---
title: Financial Document OCR Java
category: 622b805aaec68102ea7fcbc2
slug: java-financial-document-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Financial Document API](https://platform.mindee.com/mindee/financial_document).

Using the [sample below](https://github.com/mindee/client-lib-test-data/blob/main/products/financial_document/default_sample.jpg), we are going to illustrate how to extract the data that we want using the OCR SDK.
![Financial Document sample](https://github.com/mindee/client-lib-test-data/blob/main/products/financial_document/default_sample.jpg?raw=true)

# Quick-Start
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.financialdocument.FinancialDocumentV1;
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
    PredictResponse<FinancialDocumentV1> response = mindeeClient.parse(
        FinancialDocumentV1.class,
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

You can also call this product asynchronously:

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.financialdocument.FinancialDocumentV1;
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
    AsyncPredictResponse<FinancialDocumentV1> response = mindeeClient.enqueueAndParse(
        FinancialDocumentV1.class,
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
:Mindee ID: 340ee4ae-b4da-41f0-b5ea-81ae29852b57
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/financial_document v1.10
:Rotation applied: Yes

Prediction
==========
:Locale: en; en; USD;
:Invoice Number: INT-001
:Purchase Order Number: 2412/2019
:Receipt Number:
:Document Number: INT-001
:Reference Numbers: 2412/2019
:Purchase Date: 2019-11-02
:Due Date: 2019-02-26
:Payment Date: 2019-02-26
:Total Net: 195.00
:Total Amount: 204.75
:Taxes:
  +---------------+--------+----------+---------------+
  | Base          | Code   | Rate (%) | Amount        |
  +===============+========+==========+===============+
  |               |        | 5.00     | 9.75          |
  +---------------+--------+----------+---------------+
:Supplier Payment Details:
:Supplier Name: JOHN SMITH
:Supplier Company Registrations:
:Supplier Address: 4490 Oak Drive Albany, NY 12210
:Supplier Phone Number:
:Customer Name: JESSIE M HORNE
:Supplier Website:
:Supplier Email:
:Customer Company Registrations:
:Customer Address: 2019 Redbud Drive New York, NY 10011
:Customer ID: 1234567890
:Shipping Address: 2019 Redbud Drive New York, NY 10011
:Billing Address: 4312 Wood Road New York, NY 10031
:Document Type: INVOICE
:Purchase Subcategory:
:Purchase Category: miscellaneous
:Total Tax: 9.75
:Tip and Gratuity:
:Purchase Time:
:Line Items:
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | Description                          | Product code | Quantity | Tax Amount | Tax Rate (%) | Total Amount | Unit of measure | Unit Price |
  +======================================+==============+==========+============+==============+==============+=================+============+
  | Front and rear brake cables          |              | 1.00     |            |              | 100.00       |                 | 100.00     |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | New set of pedal arms                |              | 2.00     |            |              | 50.00        |                 | 25.00      |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | Labor 3hrs                           |              | 3.00     |            |              | 45.00        |                 | 15.00      |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+

Page Predictions
================

Page 0
------
:Locale: en; en; USD;
:Invoice Number: INT-001
:Purchase Order Number: 2412/2019
:Receipt Number:
:Document Number: INT-001
:Reference Numbers: 2412/2019
:Purchase Date: 2019-11-02
:Due Date: 2019-02-26
:Payment Date: 2019-02-26
:Total Net: 195.00
:Total Amount: 204.75
:Taxes:
  +---------------+--------+----------+---------------+
  | Base          | Code   | Rate (%) | Amount        |
  +===============+========+==========+===============+
  |               |        | 5.00     | 9.75          |
  +---------------+--------+----------+---------------+
:Supplier Payment Details:
:Supplier Name: JOHN SMITH
:Supplier Company Registrations:
:Supplier Address: 4490 Oak Drive Albany, NY 12210
:Supplier Phone Number:
:Customer Name: JESSIE M HORNE
:Supplier Website:
:Supplier Email:
:Customer Company Registrations:
:Customer Address: 2019 Redbud Drive New York, NY 10011
:Customer ID: 1234567890
:Shipping Address: 2019 Redbud Drive New York, NY 10011
:Billing Address: 4312 Wood Road New York, NY 10031
:Document Type: INVOICE
:Purchase Subcategory:
:Purchase Category: miscellaneous
:Total Tax: 9.75
:Tip and Gratuity:
:Purchase Time:
:Line Items:
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | Description                          | Product code | Quantity | Tax Amount | Tax Rate (%) | Total Amount | Unit of measure | Unit Price |
  +======================================+==============+==========+============+==============+==============+=================+============+
  | Front and rear brake cables          |              | 1.00     |            |              | 100.00       |                 | 100.00     |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | New set of pedal arms                |              | 2.00     |            |              | 50.00        |                 | 25.00      |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
  | Labor 3hrs                           |              | 3.00     |            |              | 45.00        |                 | 15.00      |
  +--------------------------------------+--------------+----------+------------+--------------+--------------+-----------------+------------+
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

A `FinancialDocumentV1LineItem` implements the following attributes:

* **description** (`String`): The item description.
* **productCode** (`String`): The product code referring to the item.
* **quantity** (`Double`): The item quantity
* **taxAmount** (`Double`): The item tax amount.
* **taxRate** (`Double`): The item tax rate in percentage.
* **totalAmount** (`Double`): The item total amount.
* **unitMeasure** (`String`): The item unit of measure.
* **unitPrice** (`Double`): The item unit price.

# Attributes
The following fields are extracted for Financial Document V1:

## Billing Address
**billingAddress**: The customer's address used for billing.

```java
System.out.println(result.getDocument().getInference().getPrediction().getBillingAddress().value);
```

## Purchase Category
**category**: The purchase category among predefined classes.

#### Possible values include:
 - toll
 - food
 - parking
 - transport
 - accommodation
 - gasoline
 - telecom
 - miscellaneous

```java
System.out.println(result.getDocument().getInference().getPrediction().getCategory().value);
```

## Customer Address
**customerAddress**: The address of the customer.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCustomerAddress().value);
```

## Customer Company Registrations
**customerCompanyRegistrations**: List of company registrations associated to the customer.

```java
for (customerCompanyRegistrationsElem : result.getDocument().getInference().getPrediction().getCustomerCompanyRegistrations())
{
    System.out.println(customerCompanyRegistrationsElem.value);
}
```

## Customer ID
**customerId**: The customer account number or identifier from the supplier.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCustomerId().value);
```

## Customer Name
**customerName**: The name of the customer.

```java
System.out.println(result.getDocument().getInference().getPrediction().getCustomerName().value);
```

## Purchase Date
**date**: The date the purchase was made.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDate().value);
```

## Document Number
**documentNumber**: The document number or identifier.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentNumber().value);
```

## Document Type
**documentType**: One of: 'INVOICE', 'CREDIT NOTE', 'CREDIT CARD RECEIPT', 'EXPENSE RECEIPT'.

#### Possible values include:
 - INVOICE
 - CREDIT NOTE
 - CREDIT CARD RECEIPT
 - EXPENSE RECEIPT

```java
System.out.println(result.getDocument().getInference().getPrediction().getDocumentType().value);
```

## Due Date
**dueDate**: The date on which the payment is due.

```java
System.out.println(result.getDocument().getInference().getPrediction().getDueDate().value);
```

## Invoice Number
**invoiceNumber**: The invoice number or identifier only if document is an invoice.

```java
System.out.println(result.getDocument().getInference().getPrediction().getInvoiceNumber().value);
```

## Line Items
**lineItems**(List<[FinancialDocumentV1LineItem](#line-items-field)>): List of line item details.

```java
for (lineItemsElem : result.getDocument().getInference().getPrediction().getLineItems())
{
    System.out.println(lineItemsElem.value);
}
```

## Locale
**locale**: The locale detected on the document.

```java
System.out.println(result.getDocument().getInference().getPrediction().getLocale().value);
```

## Payment Date
**paymentDate**: The date on which the payment is due / fullfilled.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPaymentDate().value);
```

## Purchase Order Number
**poNumber**: The purchase order number.

```java
System.out.println(result.getDocument().getInference().getPrediction().getPoNumber().value);
```

## Receipt Number
**receiptNumber**: The receipt number or identifier only if document is a receipt.

```java
System.out.println(result.getDocument().getInference().getPrediction().getReceiptNumber().value);
```

## Reference Numbers
**referenceNumbers**: List of Reference numbers, including PO number.

```java
for (referenceNumbersElem : result.getDocument().getInference().getPrediction().getReferenceNumbers())
{
    System.out.println(referenceNumbersElem.value);
}
```

## Shipping Address
**shippingAddress**: The customer's address used for shipping.

```java
System.out.println(result.getDocument().getInference().getPrediction().getShippingAddress().value);
```

## Purchase Subcategory
**subcategory**: The purchase subcategory among predefined classes for transport and food.

#### Possible values include:
 - plane
 - taxi
 - train
 - restaurant
 - shopping

```java
System.out.println(result.getDocument().getInference().getPrediction().getSubcategory().value);
```

## Supplier Address
**supplierAddress**: The address of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierAddress().value);
```

## Supplier Company Registrations
**supplierCompanyRegistrations**: List of company registrations associated to the supplier.

```java
for (supplierCompanyRegistrationsElem : result.getDocument().getInference().getPrediction().getSupplierCompanyRegistrations())
{
    System.out.println(supplierCompanyRegistrationsElem.value);
}
```

## Supplier Email
**supplierEmail**: The email of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierEmail().value);
```

## Supplier Name
**supplierName**: The name of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierName().value);
```

## Supplier Payment Details
**supplierPaymentDetails**: List of payment details associated to the supplier.

```java
for (supplierPaymentDetailsElem : result.getDocument().getInference().getPrediction().getSupplierPaymentDetails())
{
    System.out.println(supplierPaymentDetailsElemvalue);
    System.out.println(supplierPaymentDetailsElem.rate);
    System.out.println(supplierPaymentDetailsElem.code);
    System.out.println(supplierPaymentDetailsElem.base);
}
```

## Supplier Phone Number
**supplierPhoneNumber**: The phone number of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierPhoneNumber().value);
```

## Supplier Website
**supplierWebsite**: The website URL of the supplier or merchant.

```java
System.out.println(result.getDocument().getInference().getPrediction().getSupplierWebsite().value);
```

## Taxes
**taxes**: List of tax lines information.

```java
for (taxesElem : result.getDocument().getInference().getPrediction().getTaxes())
{
    System.out.println(taxesElem.value);
}
```

## Purchase Time
**time**: The time the purchase was made.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTime().value);
```

## Tip and Gratuity
**tip**: The total amount of tip and gratuity

```java
System.out.println(result.getDocument().getInference().getPrediction().getTip().value);
```

## Total Amount
**totalAmount**: The total amount paid: includes taxes, tips, fees, and other charges.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalAmount().value);
```

## Total Net
**totalNet**: The net amount paid: does not include taxes, fees, and discounts.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalNet().value);
```

## Total Tax
**totalTax**: The total amount of taxes.

```java
System.out.println(result.getDocument().getInference().getPrediction().getTotalTax().value);
```

# Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
