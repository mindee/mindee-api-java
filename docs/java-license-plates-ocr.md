The JAVA SDK OCR SDK supports the [Licenses plate OCR API](https://developers.mindee.com/docs/license-plates-ocr-nodejs)  for extracting license plates from pictures (or documents) of vehicles.

Using this [sample photo](https://files.readme.io/ffc127d-sample_receipt.jpg) below,
we are going to illustrate how to extract the data that we want using the OCR SDK.
![sample receipt](https://files.readme.io/fd6086e-license_plate.jpg)

## Quick Start
```java
public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {
    // Init a new client
    MindeeClient client = new MindeeClient("my-api-key");

    // Load a file from disk and parse it
    LocalInputSource localInputSource = new LocalInputSource(
        new File("./fd6086e-license_plate.jpg")
    );
    Document<LicensePlatesV1Inference> document = mindeeClient.parse(
        LicensePlatesV1Inference.class, localInputSource
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

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
