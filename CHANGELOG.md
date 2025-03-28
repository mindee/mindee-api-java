# Mindee Java Client Library Changelog

## v4.26.0 - 2025-03-28
### Changes
* :recycle: minor internal cleanup
* :coffin: deprecate LicensePlate, ProofOfAddress, ReceiptV4, US W9, EU Driver License & US Driver License


## v4.25.0 - 2025-03-21
### Fixes
* :bug: harmonize OS names across clients
* :bug: fix for no tests found
### Changes
* :recycle: increase async retry timers
* :recycle: update display & structure for invoice splitter v1
* :sparkles: bump FR EnergyBillV1 to V1.2 & US HealthcareCardV1 to V1.1
* :sparkles: add OCR output to CLI


## v4.24.2 - 2025-01-06
### Fixes
* :bug: fix missing boolean field accessor for generated objects


## v4.24.1 - 2024-12-27
### Fixes
* :bug: fix for large files timing out in async mode


## v4.24.0 - 2024-12-27
### Changes
* :sparkles: add support for us mail v3


## v4.23.1 - 2024-12-20
### Fixes
* :bug: fix for server not properly formatting floats


## v4.23.0 - 2024-12-12
### Changes
* :sparkles: allow local downloading of remote sources
* :coffin: remove support for (FR) Carte Vitale V1 in favor of French Health Card V1
### Fixes
* :memo: fix technical documentation.


## v4.22.0 - 2024-11-28
### Changes
* :sparkles: add support for workflows
* :sparkles: add support for French Health Card V1
* :sparkles: add support for Driver License V1
* :sparkles: add support for Payslip FR V3
* :coffin: remove support for international ID V1


## v4.21.0 - 2024-10-13
### Changes
* :sparkles: add support for business cards V1
* :sparkles: add support for delivery note V1
* :sparkles: add support for indian passport V1
* :sparkles: add support for resume V1.1
### Fixes
* :recycle: add deserializer + extract custom deserializing date class
* :recycle: decrease default delays for auto-polling


## v4.20.0 - 2024-10-11
### Changes
* :sparkles: add support for Financial Document v1.10
* :sparkles: add support for Invoice v4.8


## v4.19.0 - 2024-09-18
### Changes
* :sparkles: add support for BillOfLadingV1
* :sparkles: add support for (US) UsMailV2
* :sparkles: add support for (FR) EnergyBillV1
* :sparkles: add support for (FR) PayslipV1
* :sparkles: add support for NutritionFactsLabelV1
* :sparkles: add support for (US) HealthcareCardV1
### Fixes
* :bug: fixed a bug that prevented longer decimals from appearing in the string representation of some objects
* :bug: fixed a bug that caused non-table elements to unexpectedly appear truncated when printed to the console
* :memo: fix a few documentation errors & typos
* :wrench: updated CI dependencies


## v4.18.0 - 2024-07-26
### Changes
* :sparkles: add support for US Healthcare card V1
* :sparkles: add support for US Mail V2
* :sparkles: update financial document to v1.9
* :sparkles: update invoices to v4.7
* :sparkles: add support for invoice-splitter auto-extraction
### Fixes
* :bug: fix improper display for BooleanFields
* :recycle: change the way registration fields are displayed
* :arrow_up: bump dependencies


## v4.17.0 - 2024-06-04
### Changes
* :sparkles: add support for full text extraction (#177)
### Fixes
* :bug: fix warning on not properly closing a PDF document


## v4.16.0 - 2024-05-27
### Changes
* :sparkles: add isEmpty() method to field classes
* :recycle: allow easier sub-classing of inferences
* :recycle: add public constructors to feature classes


## v4.15.0 - 2024-05-23
### Changes
* :sparkles: add boolean field support
* :sparkles: allow converting between bboxes and polygons
* :sparkles: allow sending multiple anchors when building line items


## v4.14.0 - 2024-05-16
### Changes
* :sparkles: update financial document to v1.7 and receipt to v5.2
* :memo: add HMAC verification documentation


## v4.13.0 - 2024-04-30
### Changes
* :sparkles: add HMAC validation for webhooks
* :art: a few minor improvements to using Generated products
* :sparkles: allow rendering a single page from a PDF
* :sparkles: update invoice to 4.6, financial document to 1.6


## v4.12.1 - 2024-03-19
### Changes
* :arrow_up: update libraries for Java 21

### Fixes
* :bug: fix HTML in docs


## v4.12.0 - 2024-03-18
### Changes
* :sparkles: update Invoice to v4.5
* :sparkles: add ability to easily load a webhook response
* :sparkles: Add support for generated v1


## v4.11.1 - 2024-03-06
### Changes
* :sparkles: handle job errors separately from API errors


## v4.11.0 - 2024-02-21
### Changes
* :sparkles: add support for EU Driver License V1
* :sparkles: add support for International Id V2
* :sparkles: add support for Resume v1


## v4.10.0 - 2024-01-30
### Changes
* :arrow_up: :boom: update invoice to 4.4
* :sparkles: add support for rawValue in string fields


## v4.9.0 - 2024-01-09
### Changes
* :sparkles: add PDF to images feature
* :sparkles: add ability to extract sub-images from PDFs


## v4.8.1 - 2023-12-29
### Changes
* :memo: updates and fixes to documentation


## v4.8.0 - 2023-12-29
### Changes
* :sparkles: add better support for custom value list fields


## v4.7.0 - 2023-12-28
### Changes
* :sparkles: add ability to extract images from position data
* :sparkles: allow passing a string path to new `LocalInputSource`


## v4.6.0 - 2023-12-22
### Changes
* :recycle: change async timers & retry values
* :recycle: implement stricter value checks for async timers & retry
* :arrow_up: update dependencies
* :sparkles: add Carte Grise v1 product
* :sparkles: add International ID v1 product


## v4.5.0 - 2023-11-17
### Changes
* :memo: use delombok to document lombok methods
* :sparkles: add nPages to Document class
* :sparkles: add pageId to custom values


## v4.4.1 - 2023-09-25
### Changes
* :sparkles: add a method to simplify getting the document object


## v4.4.0 - 2023-09-23
### Changes
* :sparkles: add barcode reader v1
* :sparkles: add support for FR ID card v2
* :sparkles: add support for cropper v1
* :sparkles: add error code in HTTP exceptions
* :sparkles: add raw response string in response object
* :sparkles: add built-in async call polling
* :sparkles: add multi receipts detector v1
* :sparkles: better HTTP error printing
### Fixes
* :bug: fix minor spacing issue when printing lists
* :bug: take line height tolerance into account when evaluating fields


## v4.3.0 - 2023-09-01
### Changes
* :sparkles: add cropper option
* :sparkles: add support for US w9 v1
* :sparkles: add better handling of HTTP errors


## v4.2.0 - 2023-08-08
### Changes
* :white_check_mark: add unit tests for receipt v4, invoice v4
* :sparkles: add support for US driver license v1


## v4.1.0 - 2023-07-24
### Changes
* :coffin: remove shipping container
### Fixes
* :bug: fix for missing page data in us bank check
* :bug: fix passport to match API return


## v4.0.0 - 2023-06-20
### ¡Breaking Changes!
* :art: harmonize variable and class names with API response
* :art: improve code structure
* :recycle: harmonize naming with other client libraries
* :art: put all the api classes in the http package
* :recycle: simplify getting a polygon's centroid
* :art: simplify MindeeClient creation
* :art: simplify product class names
### Changes
* :sparkles: add support for asynchronous endpoints
* :sparkles: add support for invoice splitter v1
* :sparkles: add support for financial document v1.1
* :sparkles: add support for FR bank account details v2
* :sparkles: add receipt v5 support
* :sparkles: allow printing of the entire response
* :sparkles: better printing of tax information
* :recycle: use better table output for line items
* :art: harmonize CLI with other libraries
* :speech_balloon: update product property descriptions
### Fixes
* :bug: fix for proper ordering of words
* :bug: make sure account and endpoint is set on test script


## v3.4.0 - 2023-05-02
### Changes
* :memo: make code samples easier to read
* :sparkles: add support for FR bank account details
* :sparkles: Allow passing an URL to parse


## v3.3.0 - 2023-04-19
### Changes
* :recycle: :bug: rework CLI, fix minor bugs along the way
* :art: use full java classes in sample code
* :art: ensure checkstyle enforces rules
* :sparkles: changed MindeeApi implementation to allow custom httpclientbuilders


## v3.2.1 - 2023-03-28
### Changes
* :arrow_up: update httpClient and picocli


## v3.2.0 - 2023-03-27
### Changes
* :sparkles: add **experimental** support for line items reconstruction in custom APIs
* :sparkles: allow the HTTP client to use system properties
* :white_check_mark: add tests for environment variables
* :art: start conforming to Google Java standards
* :art: rename CLI class to more explicit CommandLineInterface
* :recycle: harmonize output with other client libraries
### Fixes
* :bug: fix passport API version
* :bug: fix for US Bank check API
* :bug: fix cli version


## v3.1.0 - 2023-02-02
### Changes
* :sparkles: support Financial Document V1
* :sparkles: support proof of address V1
* :sparkles: support Ocr property for Invoice and Receipt
* :recycle: using IllegalStateException instead of NotSerializableException
* :sparkles: add default constructors for ListFieldValue
* :pencil2: some renaming on Financial V1 and Proof of Address V1
### Fixes
* :bug: Details property in Error become an object because it can handle both string and object


## v3.0.0 - 2023-01-16
### ¡Breaking Changes!
* :recycle: Mindee class become MindeeClient 
* :recycle: enable the possibility to use your own pdf implementation
* :recycle: improve the creation of MindeeClient and reduce deps
### Changes
* :sparkles: support document as stream
* :sparkles: support tax base on Receipt V4
* :sparkles: support EU License plates V1
* :sparkles: support US Bank check V1
* :sparkles: support FR Carte vitale V1
* :sparkles: support FR Id Card
* :sparkles: support Shipping container V1
* :sparkles: support Receipt V4.1
* :sparkles: support Invoice V4.1
* :sparkles: support Custom document as object from API Builder
* :memo: improve documentation
* :memo: update Custom V1 to include classification


## v2.1.0 - 2022-12-12
### Changes
* :sparkles: Add support for Invoice v4
### Fixes
* :arrow_up: Bump jackson-databind from 2.13.4 to 2.13.4.2


## v2.0.0 - 2022-12-06
### ¡Breaking Changes!
* :boom: Document (endpoints) are now versioned, providing better backward-compatible support.
### Changes
* :sparkles: New PDF cut system, which allows specifying exactly which pages to keep or remove.
* :sparkles: PDF documents are no longer cut by default, use the pageOptions parameter in the parse method.
* :sparkles: Add support fot Receipt v4
* :recycle: Improve the internal de-serialization process


## v1.1.1 - 2022-10-10
### Fixes
* :arrow_up: fix for Uncontrolled Resource Consumption in Jackson-databind


## v1.1.0 - 2022-10-03
### Changes
* :sparkles: custom document is handled by sub objects (#21)


## v1.0.0 - 2022-09-21
* :tada: First official release!
