---
title: 'Java Library: Output Fields'
category: 622b805aaec68102ea7fcbc2
slug: java-field
parentDoc: 631a062c3718850f3519b793
---

# Field Objects
Each `Field` contains attributes representing the data that is returned from the API.
The exact data structure depends on the type of the field. Most `Field` classes have an attribute named `value`.
The data type of the `value` attribute varies between field types.

All Field classes contain some common data types

* `confidence` (Double):
  The confidence score of the field prediction.
* `polygon` (`Polygon` containing a `List<Point>`):
  Contains the relative vertices coordinates (points) of a polygon containing the field in the image.
* `reconstructed` (Boolean):
  True if the field was reconstructed or computed using other fields.
* `rawValue` (String):
  A string representing the field that was returned.
* `boundingBox` (`Polygon` containing a `List<Point>`): Represents the [bounding box](https://en.wikipedia.org/wiki/Minimum_bounding_box) containing the field in the image

The library uses the following classes to represents common field types
* [Amount](#amount)
* [Date](#date)
* [Field](#field)
* [Locale](#locale)
* [Orientation](#orientation)
* [PaymentDetails](#payment-details)
* [Tax](#tax)
* [Time](#time)

### Amount
Used to represent sum totals, monetary value etc. The `value` attribute is of type `java.lang.Double`

### Date
The `value` attribute is of type `java.Time.LocalDate`

### Field
Used to represent text fields and fields whose value is captured as `String`.
This type also has a map attribute `extraFields` that represents any additional fields returned from the api.

* The `value` attribute is of type `java.lang.String`\
* The `extraFields` is a map of type `Map<String,String>`

### Locale
The `value` attribute is of type `java.util.Locale`\
This class also contains additional attributes containing String representations of the available locale information returned by the API.

* `language` of type `java.lang.String` - Language code in [ISO 639-1](https://en.wikipedia.org/wiki/ISO_639-1) format as seen on the document.
  The following language codes are supported: `ca`, `de`, `en`, `es`, `fr`, `it`, `nl` and `pt`.
* `country` of type `java.lang.String` - Currency code in [ISO 4217](https://en.wikipedia.org/wiki/ISO_4217) format as seen on the document.
  The following country codes are supported: `CAD`, `CHF`, `GBP`, `EUR`, `USD`.
* `currency` of type `java.lang.String` - Country code in [ISO 3166-1](https://en.wikipedia.org/wiki/ISO_3166-1) alpha-2 format as seen on the document.
  The following country codes are supported: `CA`, `CH`, `DE`, `ES`, `FR,` `GB`, `IT`, `NL`, `PT` and `US`.

### Orientation
The orientation field is only available at the page level as it describes whether the page image should be rotated to be upright.

If the page requires rotation for correct display, the orientation field gives a prediction among these 3 possible outputs:
* 0 degrees: the page is already upright
* 90 degrees: the page must be rotated clockwise to be upright
* 270 degrees: the page must be rotated counterclockwise to be upright

The `value` attribute of type `java.lang.Integer` is set to one of 0, 90, or 270

### Payment Details
This field type captures information related to the payment details for vendors
* `iban` of type `java.lang.String`
* `swift` of type `java.lang.String`
* `routingNumber` of type `java.lang.String`
* `accountNumber` of type `java.lang.String`

### Tax
This field type captures tax information

* `value` of type `java.lang.Double` represents the tax amount.
* `code` of type `java.lang.String` represents The tax code (HST, GST, City Tax, State tax for US, etc..).
* `rate` of type `java.lang.Double` represents the tax rate.

### Time
The `value` attribute is of type `java.time.LocalTime`

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
