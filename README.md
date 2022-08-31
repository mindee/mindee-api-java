# Note: currently under heavy development

**This code is not yet ready:**
* possible breaking changes
* no documentation
* missing features
* possible rebase on main :scream:

We are only opening this repository for our users to more easily track our progress.

# Mindee API Helper Library for Java
Quickly and easily connect to Mindee's API services using Java.

# Installation
Clone the source code from GitHub

```sh
mvn clean install
```
To package the Helper Library as a jar and deploy to your local m2 repository

Include the following maven dependency in your project to use the helper library
```xml
<dependency>
      <artifactId>mindee-api-java</artifactId>
      <groupId>com.mindee.sdk</groupId>
      <version>${mindee.sdk.version}</version>
    </dependency>
```
# Usage

The Client class is the entry point for most of the helper library features. 

Configuring and using a client to parse invoices, receipts, financial documents, and passports
```java
    Client client = new Client("<MINDEE API KEY>");
       

    
    InvoiceResponse invoiceResponse = client.loadDocument(new File("src/main/resources/invoices/invoice1.pdf"))
        .parse(InvoiceResponse.class, ParseParameters.builder()
            .documentType("invoice")
            .build());
    
    ReceiptResponse receiptResponse = client.loadDocument(new File("src/main/resources/receipts/receipt1.pdf"))
        .parse(ReceiptResponse.class, ParseParameters.builder()
            .documentType("receipt")
            .build());

    FinancialDocumentResponse finDocResponse = client.loadDocument(new File("src/main/resources/findocs/findoc1.pdf"))
        .parse(FinancialDocumentResponse.class, ParseParameters.builder()
          .documentType("financial_doc")
          .build());
```

Custom documents are supported as well and can be parsed as follows

```java
   CustomDocumentResponse bill = client.loadDocument(new File("src/test/resources/custom/custom1.pdf"))
        .parse(CustomDocumentResponse.class, ParseParameters.builder()
            .documentType("bill_of_lading_line_items")
            .accountName("<your account name>")
            .build());
```

The Client loadDocument method supports multiple input types
* java.io.File file
* byte[] fileAsByteArray, String filename
* String fileAsBase64String, String filename


## Quick Start
Here's the TL;DR of getting started.

First, get an [API Key](https://developers.mindee.com/docs/create-api-key)


## CLI

```
mvn clean install

./cli.sh
```
