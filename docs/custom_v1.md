---
title: Custom OCR Java (Deprecated)
category: 622b805aaec68102ea7fcbc2
slug: java-api-builder
parentDoc: 631a062c3718850f3519b793
---
> 🚧 This product is still supported, but is considered to be deprecated. If you are looking for the DocTI API documentation, you can find it [here](#https://developers.mindee.com/docs/java-generated-ocr).


The JAVA OCR SDK supports [custom-built API](https://developers.mindee.com/docs/build-your-first-document-parsing-api) from the API Builder.
If your document isn't covered by one of Mindee's Off-the-Shelf APIs,
you can create your own API using the [API Builder](https://developers.mindee.com/docs/overview).

For the following examples, we are using our own [W9s custom API](https://developers.mindee.com/docs/w9-forms-ocr)
created with the [API Builder](https://developers.mindee.com/docs/overview).

## Quick Start

```java
String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(path);
CustomEndpoint myEndpoint = new CustomEndpoint(
    "wnine",
    "john",
    // "1.1" // optional
);

MindeeClient mindeeClient = new MindeeClient(apiKey);
  
PredictResponse<CustomV1> response = mindeeClient
    .parse(inputSource, myEndpoint);

// Print a summary of the response
System.out.println(response.toString());
```

If the `version` argument is set, you'll be required to update it every time a new model is trained.
This is probably not needed for development but essential for production use.

## The `CustomV1` Object
The `CustomV1` object contains the results of document-level and page-level predictions.

All the fields which are present in the API builder are available.

The fields are defined when creating your custom API.

### Document-Level Predictions
The document-level predictions are ultimately contained in the `CustomV1Document` object.

The predictions are stored in two properties: `classificationFields` and `fields`. 

Both are a Map where the key is a `String` of the name of the field.

For the values:
* `classificationFields` have a `ClassificationField` object as value, each containing a single `String` value.
* `fields` have a `ListField` object as value, each containing a list of all `String` values extracted for this field.

Here are some example usages:
```java
String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(path);

PredictResponse<CustomV1> response = mindeeClient
    .parse(CustomV1.class, inputSource);

CustomV1 inference = response.getDocument().getInference();
  
// === Getting a single field === \\

ListField employerName = inference.getPrediction().getFields().get("employer_name");

// get the field as a string
System.out.println(employerName.toString());

// get the field as a string with a custom separator
System.out.println(employerName.getContentsString("_"));

// get the list of string values in the field
System.out.println(employerName.getContentsList());

// == Getting all fields === \\

for (Map.Entry<String, ListField> entry : inference.getPrediction().getFields().entrySet()) {
  ListField field = entry.getValue();

  // Not really needed, just showing that the method exists ;-)
  if (field.isEmpty()) {
      continue;
  }

  // We can print directly as in the single field example above ...
  System.out.println(field.toString());

  // ... or go through each value
  for (ListFieldValue value : field.getValues()) {

    // The actual value (word)
    System.out.println(value.getContent());

    // The page on which the value was found
    System.out.println(value.getPageId());
  }
}
```

### Page-Level Predictions
The page-level predictions are ultimately contained in the  `CustomV1Page` object.

In the response, there is a list of these objects, each one representing a single page.

The prediction results are stored as a key-value `HashMap<String, ListField>`.

Here are some example usages:
```java
String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(path);

PredictResponse<CustomV1> response = mindeeClient
    .parse(CustomV1.class, inputSource);

CustomV1 inference = response.getDocument().getInference();
  
for (Page<CustomV1Page> page : inference.getPages()) {
  CustomV1Page pagePrediction = page.getPrediction();

  for (Map.Entry<String, ListField> entry : pagePrediction.entrySet()) {
    ListField field = entry.getValue();
    
    // get the field as a string
    System.out.println(field.toString());

    // get the field as a string with a custom separator
    System.out.println(field.getContentsString("_"));

    // get the list of strings in the field
    System.out.println(field.getContentsList());
  }
}
```

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
