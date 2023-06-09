The JAVA OCR SDK supports the [passport API](https://developers.mindee.com/docs/passport-ocr) for extracting data from passports.

Using this [sample passport](https://files.readme.io/4a16b1d-passport_pic.jpg) below,
we are going to illustrate how to extract the data that we want using the  OCR SDK.
![sample passport](https://files.readme.io/4a16b1d-passport_pic.jpg)

## Quick Start
```java
// Init a new client
MindeeClient client = MindeeClientInit.create("my-api-key");

// Load a file from disk and parse it
LocalInputSource localInputSource = mindeeClient.loadDocument(
  new File("./4a16b1d-passport_pic.jpg")
);
Document<PassportV1Inference> document = mindeeClient.parse(
  PassportV1Inference.class, localInputSource
);

// Print a summary of the parsed data
logger.info(document.toString());
```

Output:
```rst
########
Document
########
:Mindee ID: 79db59c7-b312-4692-a77f-e8698666ffba
:Filename: passport.jpeg

Inference
#########
:Product: mindee/passport v1.0
:Rotation applied: No

Prediction
==========
:Full name: HENERT PUDARSAN
:Given names: HENERT
:Surname: PUDARSAN
:Country: GBR
:ID Number: 707797979
:Issuance date: 2012-04-22
:Birth date: 1995-05-20
:Expiry date: 2057-04-22
:MRZ 1: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<
:MRZ 2: 7077979792GBR9505209M1704224<<<<<<<<<<<<<<00
:MRZ: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<7077979792GBR9505209M1704224<<<<<<<<<<<<<<00

Page Predictions
================

Page 0
------
:Full name: HENERT PUDARSAN
:Given names: HENERT
:Surname: PUDARSAN
:Country: GBR
:ID Number: 707797979
:Issuance date: 2012-04-22
:Birth date: 1995-05-20
:Expiry date: 2057-04-22
:MRZ 1: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<
:MRZ 2: 7077979792GBR9505209M1704224<<<<<<<<<<<<<<00
:MRZ: P<GBRPUDARSAN<<HENERT<<<<<<<<<<<<<<<<<<<<<<<7077979792GBR9505209M1704224<<<<<<<<<<<<<<00
```

## Field Objects
Each `Field` object contains at a minimum the following attributes:

* `value` (string or number depending on the field type):
  Corresponds to the field value. Can be `null` if no value was extracted.
* `confidence` (Double):
  The confidence score of the field prediction.
* `polygon` (Polygon):
  Contains exactly 4 relative vertices coordinates (points) of a right rectangle containing the field in the document.

## Extracted Fields
Depending on the field type specified, additional attributes can be extracted from the `Passport` object.

Using the above [passport example](https://files.readme.io/4a16b1d-passport_pic.jpg), the following are the basic fields that can be extracted.

- [Birth Place](#birth-place)
- [Country](#country)
- [Dates (Expiry, Issuance, Birth)](#dates)
- [Gender](#gender)
- [Given Names](#given-names)
- [ID Number](#id)
- [Machine Readable Zone](#machine-readable-zone)
- [Surname](#surname)

### Birth Place

* **`birthPlace`** (Field): Passport owner birthplace.

### Country
* **`country`** (Field): Passport country in [ISO 3166-1 alpha-3 code format](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) (3 letters code).

### Dates
Date fields:
* have a `value` attribute which is the [ISO 8601](https://en.wikipedia.org/wiki/ISO_8601) representation of the date, regardless of the `raw` contents.

The following date fields are available:
- **`expiryDate`**: Passport expiry date.

- **`issuanceDate`**: Passport date of issuance.

- **`birthDate`**: Passport's owner date of birth.

### Gender

- **`gender`** (StringField): Passport owner's gender (M / F).

### Given Names

* **`givenNames`** (List<StringField>): List of passport owner's given names.

### ID

* **`idNumber`** (StringField): Passport identification number.

### Machine Readable Zone

* **`mrz1`** (StringField): Passport first line of machine-readable zone.

* **`mrz2`** (StringField): Passport second line of machine-readable zone.

### Surname
* **`surname`** (StringField): Passport's owner surname.

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
