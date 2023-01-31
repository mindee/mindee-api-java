The Java OCR SDK supports the [Proof of Address OCR API](https://developers.mindee.com/docs/proof-of-address-ocr).

Using this [sample](https://files.readme.io/a8e8cfa-a74eaa5-c8e283b-sample_invoice.jpeg) below, we are going to illustrate how to extract the data that we want using the OCR SDK.
![sample](https://files.readme.io/a8e8cfa-a74eaa5-c8e283b-sample_invoice.jpeg)

## Quick Start
```java
// Init a new client
MindeeClient client = MindeeClientInit.create("<your mindee api key>");

// Load a file from disk and parse it
  DocumentToParse documentToParse = mindeeClient.loadDocument(new File("./a8e8cfa-a74eaa5-c8e283b-sample_invoice.jpeg"));
  Document<ProofOfAddressV1Inference> document =
  mindeeClient.parse(ProofOfAddressV1Inference.class, documentToParse);

// Print a summary of the parsed data
  logger.info(document.getInference().getDocumentPrediction().toString());
```

Output:
```
:Locale: en; en; USD;
:Issuer name: PPL ELECTRIC UTILITIES
:Issuer Address: 2 NORTH 9TH STREET CPC-GENN1 ALLENTOWN,PA 18101-1175
:Issuer Company Registrations:
:Recipient name: CUSTOMER
:Recipient Address: 123 MAIN ST ANYTOWN,PA 18062
:Recipient Company Registrations:
:Issuance date: 2011-07-27
:Dates: 2011-07-27
        2011-07-06
        2011-08-03
        2011-07-27
        2011-06-01
        2011-07-01
        2010-07-01
        2010-08-01
        2011-07-01
        2009-08-01
        2010-07-01
        2011-07-27
```

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack]((https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
