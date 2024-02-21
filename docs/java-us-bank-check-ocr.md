The JAVA OCR SDK supports the [Bank Checks OCR API](https://developers.mindee.com/docs/bank-check-ocr) for extracting data from bank checks.

## Quick Start
```java
// Init a new client
public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {
    MindeeClient client = new MindeeClient("my-api-key");

    // Load a file from disk and parse it
    LocalInputSource localInputSource = new LocalInputSource(
        new File("./path/to/the/file.ext")
    );
    PredictResponse<BankCheckV1> document = mindeeClient.parse(
        BankCheckV1.class, localInputSource
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

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
