---
title: Invoice Splitter API Java
category: 622b805aaec68102ea7fcbc2
slug: java-invoice-splitter-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports the [Invoice Splitter API](https://platform.mindee.com/mindee/invoice_splitter).

Using [this sample](https://github.com/mindee/client-lib-test-data/blob/main/products/invoice_splitter/default_sample.pdf), we are going to illustrate how to detect the pages of multiple invoices within the same document.

# Quick-Start

> **⚠️ Important:** This API only works **asynchronously**, which means that documents have to be sent and retrieved in a specific way:

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.parsing.common.Job;
import com.mindee.parsing.common.Document;
import com.mindee.product.invoicesplitter.InvoiceSplitterV1;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    // Parse the file asynchronously
    AsyncPredictResponse<InvoiceSplitterV1> response = mindeeClient.enqueueAndParse(
      InvoiceSplitterV1.class,
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
:Mindee ID: 8c25cc63-212b-4537-9c9b-3fbd3bd0ee20
:Filename: default_sample.jpg

Inference
#########
:Product: mindee/carte_vitale v1.0
:Rotation applied: Yes

Prediction
==========
:Given Name(s): NATHALIE
:Surname: DURAND
:Social Security Number: 269054958815780
:Issuance Date: 2007-01-01

Page Predictions
================

Page 0
------
:Given Name(s): NATHALIE
:Surname: DURAND
:Social Security Number: 269054958815780
:Issuance Date: 2007-01-01
```

# Field Types

## Specific Fields

### Page Group

List of page group indexes.

An `invoicePageGroups` implements the following attributes:

- **pageIndexes** (`List<`PageIndexes`>`): List of indexes of the pages of a single invoice.
- **confidence** (`Double`): The confidence of the prediction.

# Attributes

The following fields are extracted for Invoice Splitter V1:

## Invoice Page Groups

**invoicePageGroups** ([invoicePageGroups](#page-groups)[]): List of page indexes that belong to the same invoice in the PDF.

```java
for (invoicePageGroupsElem : result.getDocument().getInference().getPrediction().getInvoicePageGroups())
{
  System.out.println(invoicePageGroupsElem);
}
```

# Questions?

[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
