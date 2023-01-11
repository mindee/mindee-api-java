The JAVA SDK OCR SDK supports the  API  [Licenses plate OCR](https://developers.mindee.com/docs/license-plates-ocr-nodejs)  for extracting data from pictures (or documents) of cars.

Using this [sample photo](https://files.readme.io/ffc127d-sample_receipt.jpg) below, we are going to illustrate how to extract the data that we want using the OCR SDK.
![sample receipt](https://files.readme.io/fd6086e-license_plate.jpg)

## Quick Start
```java
// Init a new client
MindeeClient client = MindeeClientInit.create("<your mindee api key>");

// Load a file from disk and parse it
DocumentToParse documentToParse = mindeeClient.loadDocument(new File("./a74eaa5-c8e283b-sample_invoice.jpeg"));
Document<LicensePlatesV1Inference> licensePlatesDocument =
 mindeeClient.parse(LicensePlatesV1Inference.class, documentToParse);

// Print a summary of the parsed data
logger.info(licensePlatesDocument.toString());
```

Output:
```
########
Document
########
:Mindee ID: 858d2e05-2166-46ad-81da-c917e3a1b453
:Filename: multiplate.jpg

Inference
#########
:Product: mindee/license_plates v1.0
:Rotation applied: No

Prediction
==========
:License plates: 8LQA341, 8LBW890

Page Predictions
================

Page 0
------
:License plates: 8LQA341, 8LBW890
``` 

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack]((https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
