The JAVA OCR SDK supports the [Bank Checks OCR API](https://developers.mindee.com/docs/bank-check-ocr) for extracting data from bank checks.

## Quick Start
```java
// Init a new client
MindeeClient client = MindeeClientInit.create("<your mindee api key>");

// Load a file from disk and parse it
DocumentToParse documentToParse = mindeeClient.loadDocument(new File("./path/to/the/file.ext"));
Document<BankCheckV1Inference> bankCheckDocument =
 mindeeClient.parse(BankCheckV1Inference.class, documentToParse);

// Print a summary of the parsed data
logger.info(bankCheckDocument.toString());
```

Output:
```
########
Document
########
:Mindee ID: 0626bc33-2b9f-4645-b455-6af111c551cf
:Filename: check.jpg

Inference
#########
:Product: mindee/bank_check v1.0
:Rotation applied: Yes

Prediction
==========
:Routing number: 012345678
:Account number: 12345678910
:Check number: 8620001342
:Date: 2022-04-26
:Amount: 6496.58
:Payees: John Doe, Jane Doe

Page Predictions
================

Page 0
------
:Routing number: 012345678
:Account number: 12345678910
:Check number: 8620001342
:Date: 2022-04-26
:Amount: 6496.58
:Payees: John Doe, Jane Doe
```

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack]((https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
