The java client library supports the [invoice API](https://developers.mindee.com/docs/invoice-ocr) for extracting data from invoices.

Using this [sample invoice](https://files.readme.io/a74eaa5-c8e283b-sample_invoice.jpeg) below,
we are going to illustrate how to extract the data that we want using the client library.
![sample invoice](https://files.readme.io/a74eaa5-c8e283b-sample_invoice.jpeg)

## Quick Start
```java

// Init a new client
MindeeClient client = MindeeClientInit.create("my-api-key");

// Load a file from disk and parse it
DocumentToParse documentToParse = mindeeClient.loadDocument(
  new File("./a74eaa5-c8e283b-sample_invoice.jpeg")
);
Document<InvoiceV4Inference> document = mindeeClient.parse(
  InvoiceV4Inference.class, documentToParse
);

// Print a summary of the parsed data
logger.info(document.toString());
```
Output:
```rst
########
Document
########
:Mindee ID: 656c2ec1-0920-4556-9bc2-772162bc698a
:Filename: invoice.pdf

Inference
#########
:Product: mindee/invoices v4.1
:Rotation applied: Yes

Prediction
==========
:Locale: fr; fr; EUR;
:Document type: INVOICE
:Invoice number: 0042004801351
:Reference numbers: AD29094
:Invoice date: 2020-02-17
:Invoice due date: 2020-02-17
:Supplier name: TURNPIKE DESIGNS CO.
:Supplier address: 156 University Ave, Toronto ON, Canada M5H 2H7
:Supplier company registrations: 501124705; FR33501124705
:Supplier payment details: FR7640254025476501124705368;
:Customer name: JIRO DOI
:Customer address: 1954 Bloon Street West Toronto, ON, M6P 3K9 Canada
:Customer company registrations: FR00000000000; 111222333
:Taxes: 97.98 20.00%
:Total net: 489.97
:Total taxes: 97.98
:Total amount: 587.95

:Line Items:
====================== ======== ========= ========== ================== ====================================
Code                   QTY      Price     Amount     Tax (Rate)         Description
====================== ======== ========= ========== ================== ====================================
                                          4.31        (2.10%)           PQ20 ETIQ ULTRA RESIS METAXXDC
                       1.00     65.00     75.00      10.00              Platinum web hosting package Down...
XXX81125600010         1.00     250.01    275.51     25.50 (10.20%)     a long string describing the item
ABC456                 200.30   8.101     1622.63    121.70 (7.50%)     Liquid perfection
                                                                        CARTOUCHE L NR BROTHER TN247BK
====================== ======== ========= ========== ================== ====================================

Page Predictions
================

Page 0
------
:Locale: fr; fr; EUR;
:Document type: INVOICE
:Invoice number: 0042004801351
:Reference numbers:
:Invoice date: 2020-02-17
:Invoice due date: 2020-02-17
:Supplier name:
:Supplier address:
:Supplier company registrations: 501124705; FR33501124705
:Supplier payment details: FR7640254025476501124705368;
:Customer name:
:Customer address:
:Customer company registrations:
:Taxes: 97.98 20.00%
:Total net: 489.97
:Total taxes: 97.98
:Total amount: 587.95

:Line Items:
====================== ======== ========= ========== ================== ====================================
Code                   QTY      Price     Amount     Tax (Rate)         Description
====================== ======== ========= ========== ================== ====================================
                                          4.31        (2.10%)           PQ20 ETIQ ULTRA RESIS METAXXDC
                       1.00     65.00     75.00      10.00              Platinum web hosting package Down...
====================== ======== ========= ========== ================== ====================================

Page 1
------
:Locale: fr; fr; EUR;
:Document type: INVOICE
:Invoice number:
:Reference numbers: AD29094
:Invoice date:
:Invoice due date: 2020-02-17
:Supplier name: TURNPIKE DESIGNS CO.
:Supplier address: 156 University Ave, Toronto ON, Canada M5H 2H7
:Supplier company registrations:
:Supplier payment details:
:Customer name: JIRO DOI
:Customer address: 1954 Bloon Street West Toronto, ON, M6P 3K9 Canada
:Customer company registrations:
:Taxes: 193.20 8.00%
:Total net:
:Total taxes: 193.20
:Total amount: 2608.20

:Line Items:
====================== ======== ========= ========== ================== ====================================
Code                   QTY      Price     Amount     Tax (Rate)         Description
====================== ======== ========= ========== ================== ====================================
XXX81125600010         1.00     250.00    250.00      (10.00%)          a long string describing the item
ABC456                 200.30   8.101     1622.63    121.70 (7.50%)     Liquid perfection
                                                                        CARTOUCHE L NR BROTHER TN247BK
====================== ======== ========= ========== ================== ====================================
```


## Extracted Fields
Attributes that will be extracted from the document and available in the `inference` object:
- [Invoice Number](#invoice-number)
- [Reference Numbers](#invoice-number)
- [Locale](#locale)
- [Invoice Date](#invoice-date)
- [Due Date](#due-date)
- [Customer Name](#customer-name)
- [Customer Address](#customer-address)
- [Customer Company Registration](#customer-company-registrations)
- [Supplier name](#supplier-name)
- [Supplier Address](#supplier-address)
- [Supplier Company Registrations](#supplier-company-registrations)
- [Payment Details](#payment-details)
- [Line items](#line-items)
- [Taxes](#taxes)
- [Total Tax](#total-tax)
- [Total Including Taxes](#total-including-taxes)
- [Total Excluding Taxes](#total-excluding-taxes)

### Invoice Information
#### Invoice Number
The invoice number for the invoice being processed
* `invoiceNumber`of type [Field](https://developers.mindee.com/docs/java-field#field)

````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getInvoiceNumber()getValue());
````

#### Reference Numbers
The reference numbers for the invoice being processed
* `referenceNumbers`of list of type [Field](https://developers.mindee.com/docs/java-field#field)

````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getReferenceNumbers()getValue();
````

#### Locale
* `locale` of type [Locale]((https://developers.mindee.com/docs/java-field#locale)
#### Invoice Date
* invoiceDate of type [Date](https://developers.mindee.com/docs/java-field#date)

#### Due Date
* dueDate of type [Date](https://developers.mindee.com/docs/java-field#date)

### Customer Information
#### Customer Name
Customer's name
* `customerName` of type [Field](https://developers.mindee.com/docs/java-field#field)
````java
logger.info(invoiceDocument.getInference().getCustomerName().getReference()getValue();
````

#### Customer Address
Customer's Postal Address
* `customerAddress` of type [Field](https://developers.mindee.com/docs/java-field#field)
````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getCustomerAddress().getValue();
````
#### Customer Company Registration
A list with the customers company registration information
* `customerCompanyRegistrations` of type List< [Field](https://developers.mindee.com/docs/java-field#field) >
The field contains an extraField with a key "type"

````java
invoiceDocument.getInference().getDocumentPrediction().getCustomerCompanyRegistrations().stream()
      .map((registration) -> String.join(":",registration.getExtraFields().get("type"),
        registration.getValue()))
      .forEach(System.out::println);
````

### Supplier Information
#### Supplier Name
* `supplierName` of type [Field](https://developers.mindee.com/docs/java-field#field)
````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getSupplierName().getValue());
````
#### Supplier Address
* supplierAddress of type [Field](https://developers.mindee.com/docs/java-field#field)
````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getSupplierAddress().getValue());
````

#### Supplier Company Registrations
A list with the supplier's company registration information
* `supplierCompanyRegistrations` of type List< [CompanyRegistration](https://developers.mindee.com/docs/java-field#field) >
  The field contains an extraField with a key "type" - Type of company registration number among: [VAT NUMBER](https://en.wikipedia.org/wiki/VAT_identification_number), [SIRET](https://en.wikipedia.org/wiki/SIRET_code), [SIREN](https://en.wikipedia.org/wiki/SIREN_code), [NIF](https://en.wikipedia.org/wiki/National_identification_number), [CF](https://en.wikipedia.org/wiki/Italian_fiscal_code), [UID](https://en.wikipedia.org/wiki/VAT_identification_number), [STNR](https://de.wikipedia.org/wiki/Steuernummer), [HRA/HRB](https://en.wikipedia.org/wiki/German_Commercial_Register), [TIN](https://en.wikipedia.org/wiki/Taxpayer_Identification_Number) (includes EIN, FEIN, SSN, ATIN, PTIN, ITIN), [RFC](https://wise.com/us/blog/clabe-rfc-curp-abm-meaning-mexico), [BTW](https://en.wikipedia.org/wiki/European_Union_value_added_tax), [ABN](https://abr.business.gov.au/Help/AbnFormat), [UEN](https://www.uen.gov.sg/ueninternet/faces/pages/admin/aboutUEN.jspx), [CVR](https://en.wikipedia.org/wiki/Central_Business_Register_(Denmark)), [ORGNR](https://en.wikipedia.org/wiki/VAT_identification_number), [INN](https://www.nalog.gov.ru/eng/exchinf/inn/), [DPH](https://en.wikipedia.org/wiki/Value-added_tax), [GSTIN](https://en.wikipedia.org/wiki/VAT_identification_number), [COMPANY REGISTRATION NUMBER](https://en.wikipedia.org/wiki/VAT_identification_number) (UK), [KVK](https://business.gov.nl/starting-your-business/registering-your-business/lei-rsin-vat-and-kvk-number-which-is-which/), [DIC](https://www.vatify.eu/czech-vat-number.html)


````java
invoiceDocument.getInference().getDocumentPrediction().getCustomerCompanyRegistration().stream()
      .map((registration) -> String.join(":",registration.getExtraFields().get("type"),
        registration.getValue()))
      .forEach(System.out::println);
/// SAMPLE OUTPUT 
//        VAT NUMBER:FR1112323222
//        SIRET:1111111111
//        SIREN:1122221111
````
#### Payment Details
A list of the invoice supplier's payment details.
* `supplierPaymentDetails` of type List< [PaymentDetails](https://developers.mindee.com/docs/java-field#payment-details) >
````java
invoiceDocument.getInference().getDocumentPrediction().getSupplierPaymentDetails()
        .stream().map(PaymentDetails::getSupplierPaymentDetailsSummary)
        .forEach(System.out::println);
````

### Line items

**`LineItems`** (List<InvoiceLineItem>):  Line items details.
Each object in the list contains the following getter methods:
* `getProductCode()` (String)
* `geDescription()` (String)
* `getQuantity()` (Double)
* `getUnitPrice()` (Double)
* `getTotalAmount()` (Double)
* `getTaxRate()` (Double)
* `getTaxAmount()` (Double)
* `getConfidence()` (Double)
* `getPageId()` (Double)
* `getPolygon()` (Polygon)

### Taxes and Amounts

#### Taxes
A list of the taxes seen on the invoice
* `taxes` of type List < [Tax](https://developers.mindee.com/docs/java-field#tax) >

````java
invoiceDocument.getInference().getDocumentPrediction().getTaxes()
      .stream()
      .map(Tax::getTaxSummary)
      .forEach(System.out::println);
````

#### Total Tax
The total tax for the invoice
* `totalTax` of type [Amount](https://developers.mindee.com/docs/java-field#amount)

````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getTotalTax().getValue());
````

#### Total Amount
The total amount including taxes for the invoice
* `totalAmount` of type [Amount](https://developers.mindee.com/docs/java-field#amount)

````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getTotalAmount().getValue());
````

#### Total Net
The total amount excluding taxes for the invoice
* `totalNet` of type [Amount](https://developers.mindee.com/docs/java-field#amount)

````java
logger.info(invoiceDocument.getInference().getDocumentPrediction().getTotalNet().getValue());
````

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
