The JAVA OCR SDK supports the [Shipping containers OCR API](https://developers.mindee.com/docs/shipping-containers-ocr) for extracting data from pictures of containers identification.

Using this [sample](https://files.readme.io/853f15a-shipping_containers.jpg) below, we are going to illustrate how to extract the data that we want using the OCR SDK.
![sample](https://files.readme.io/853f15a-shipping_containers.jpg)

## Quick Start
```java
// Init a new client
MindeeClient client = MindeeClientInit.create("<your mindee api key>");

// Load a file from disk and parse it
DocumentToParse documentToParse = mindeeClient.loadDocument(
  new File("./853f15a-shipping_containers.jpg")
);
Document<ShippingContainerV1Inference> document = mindeeClient.parse(
  ShippingContainerV1Inference.class, documentToParse
);

// Print a summary of the parsed data
logger.info(document.toString());
```

Output:
```
########
Document
########
:Mindee ID: 32418e69-77b4-41e0-bdcd-d5fb40a2cfe2
:Filename: shipping_containers.jpg

Inference
#########
:Product: mindee/shipping_containers v1.0
:Rotation applied: No

Prediction
==========
:Owner: MMAU
:Serial number: 1193249
:Size and type: 45R1

Page Predictions
================

Page 0
------
:Owner: MMAU
:Serial number: 1193249
:Size and type: 45R1
```

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack]((https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
