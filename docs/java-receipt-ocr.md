The JAVA OCR SDK supports the [receipt API](https://developers.mindee.com/docs/receipt-ocr) for extracting data from receipts.

Using this [sample receipt](https://files.readme.io/ffc127d-sample_receipt.jpg) below,
we are going to illustrate how to extract the data that we want using the OCR SDK.
![sample receipt](https://files.readme.io/ffc127d-sample_receipt.jpg)

## Quick Start
```java
public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {
    // Init a new client
    MindeeClient client = new MindeeClient("my-api-key");

    // Load a file from disk and parse it
    LocalInputSource localInputSource = new LocalInputSource(
        new File("./ffc127d-sample_receipt.jpg")
    );
    Document<ReceiptV4Inference> document = mindeeClient.parse(
        ReceiptV4Inference.class, localInputSource
    );

    // Print a summary of the parsed data
    logger.info(document.toString());
  }
}
```

Output:
```
########
Document
########
:Mindee ID: aa1a8095-20c6-4080-98bd-4684d2807365
:Filename: receipt.jpg

Inference
#########
:Product: mindee/expense_receipts v4.1
:Rotation applied: Yes

Prediction
==========
:Locale: en-US; en; US; USD;
:Date: 2014-07-07
:Category: food
:Subcategory: restaurant
:Document type: EXPENSE RECEIPT
:Time: 20:20
:Supplier name: LOGANS
:Taxes: 3.34 TAX
:Total net: 40.48
:Total taxes: 3.34
:Tip: 10.00
:Total amount: 53.82

Page Predictions
================

Page 0
------
:Locale: en-US; en; US; USD;
:Date: 2014-07-07
:Category: food
:Subcategory: restaurant
:Document type: EXPENSE RECEIPT
:Time: 20:20
:Supplier name: LOGANS
:Taxes: 3.34 TAX
:Total net: 40.48
:Total taxes: 3.34
:Tip: 10.00
:Total amount: 53.82
```

## Field Objects
Each `Field` object contains at a minimum the following attributes:

* `value` (string or number depending on the field type):
  Corresponds to the field value. Can be `null` if no value was extracted.
* `confidence` (Float):
  The confidence score of the field prediction.
* `polygon` (Polygon):
  Contains the relative vertices coordinates (points) of a polygon containing the field in the image.

## Extracted Fields
Attributes that will be extracted from the document and available in the `Receipt` object:

- [Category](#category)
- [Date](#date)
- [Locale](#locale)
- [Supplier Information](#supplier-information)
- [Taxes](#taxes)
- [Time](#time)
- [Tip](#tip)
- [Total Amounts](#total-amounts)

### Category
* **`category`** (StringField): Receipt category as seen on the receipt.
List of supported categories supported: https://developers.mindee.com/docs/receipt-ocr#category.
* **`subCategory`** (StringField): More precise subcategory.
List of supported subcategories supported: https://developers.mindee.com/docs/receipt-ocr#subcategory.
* **`documentType`** (StringField): Is a classification field of the receipt.
  The document types supported: https://developers.mindee.com/docs/receipt-ocr#document-type

### Date
Date fields:
* contain the `raw` attribute, which is the textual representation found on the document.

The following date fields are available:
* **`date`**: Date the receipt was issued

### Locale
**`locale`** (Locale): Locale information.

* `locale.language` (String): Language code in [ISO 639-1](https://en.wikipedia.org/wiki/ISO_639-1) format as seen on the document.
  The following language codes are supported: `ca`, `de`, `en`, `es`, `fr`, `it`, `nl` and `pt`.

* `locale.currency` (String): Currency code in [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) format as seen on the document.
  The following country codes are supported: `CAD`, `CHF`, `GBP`, `EUR`, `USD`.

* `locale.country` (String): Country code in [ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) alpha-2 format as seen on the document.
  The following country codes are supported: `CA`, `CH`, `DE`, `ES`, `FR,` `GB`, `IT`, `NL`, `PT` and `US`.

### Supplier Information
* **`supplier`** (StringField): Supplier name as written in the receipt.

### Taxes
**`taxes`** (Array<TaxField>): Contains tax fields as seen on the receipt.

* `value` (Float): The tax amount.
* `code` (String): The tax code (HST, GST... for Canadian; City Tax, State tax for US, etc..).
* `rate` (Float): The tax rate.

### Time
* **`time`**: Time of purchase as seen on the receipt
    * `value` (string): Time of purchase with 24 hours formatting (hh:mm).

### Tip
**`Tip`** (AmountField): Total amount of tip and gratuity.


### Total Amounts
* **`TotalAmount`** (AmountField): Total amount including taxes and tips

* **`TotalNet`** (AmountField): Total amount paid excluding taxes and tip

* **`TotalTax`** (AmountField): Total tax value from tax lines


## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
