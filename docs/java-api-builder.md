The JAVA OCR SDK supports [custom-built API](https://developers.mindee.com/docs/build-your-first-document-parsing-api) from the API Builder.
If your document isn't covered by one of Mindee's Off-the-Shelf APIs,
you can create your own API using the [API Builder](https://developers.mindee.com/docs/overview).

For the following examples, we are using our own [W9s custom API](https://developers.mindee.com/docs/w9-forms-ocr)
created with the [API Builder](https://developers.mindee.com/docs/overview).

## Quick Start

```java
String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(new File(path));
CustomEndpoint myEndpoint = new CustomEndpoint(
    "wnine",
    "john",
    "1.0" // optional
);

Document<CustomV1new MindeeClient> customDocument = mindeeClient
    .parse(inputSource, myEndpoint);
```

If the `version` argument is set, you'll be required to update it every time a new model is trained.
This is probably not needed for development but essential for production use.

## Parsing Documents
Use the `ParseAsync` method to call the API prediction on your custom document.
The response class and document type must be specified when calling this method.

You have two different ways to parse a custom document.

1. Use the default one (named ``CustomPrediction``):
```java

String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(new File(path));
CustomEndpoint myEndpoint = new CustomEndpoint(
    "wnine",
    "john",
    "1.0" // optional
);

Document<CustomV1new MindeeClient> customDocument = mindeeClient.parse(inputSource, myEndpoint);
```

2. You can also use your own class which will represent the required fields. For example:
```java

// The CustomEndpointInfo annotation is required when using your own model.
// It will be used to know which Mindee API to call.

public class WNineV1DocumentPrediction {
  @JsonProperty("name")
  private StringField name;

  @JsonProperty("employerId")
  private StringField employerId;
  
  (...)
}

@EndpointInfo(endpointName = "wnine", accountName = "john" version = "1")
public class WNineV1new MindeeClient
  extends new MindeeClient new MindeeClient<WNineV1DocumentPrediction, WNineV1DocumentPrediction> {
}

String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(new File(path));

Document<WNineV1new MindeeClient> myCustomDocument = mindeeClient
    .parse(WNineV1new MindeeClient.class, inputSource);
```

## CustomV1new MindeeClient object
All the fields which are present in the API builder 
are available (the fields are defined when creating your custom API).

`CustomV1new MindeeClient` is an object which contains a document prediction and pages prediction result.
### `CustomV1PagePrediction` 
Which is a `HashMap<String, ListField>` with the key as a `string` for the name of the field, and a `ListField` as a value.

### `CustomV1DocumentPrediction` 
Which contains 2 properties : `classificationFields` and `fields`. 
Both are a Map and the key is a `string` for the name of the field and for the value :
* `classificationFields` have a `ClassificationField` object as value. Each `ClassificationField` contains a value.
* `fields` have a `ListField` object as value. Each `ListField` contains a list of all values extracted for this field.

> 📘 **Info**
>
> Both document level and page level objects work in the same way.

### Fields property
A Map with the following structure:
* `confidence`: a `double`
* `values`: a list of `ListFieldValue` which containing a list of all values found for the field.

In the examples below we'll use the `employer_id` field.

```java
String path = "/path/to/the/file.ext";
LocalInputSource inputSource = new LocalInputSource(new File(path));

Document<WNineV1new MindeeClient> myCustomDocument = mindeeClient
    .parse(WNineV1new MindeeClient.class, inputSource);

ListField employerId = document.getnew MindeeClient().getDocumentPrediction().get("employer_id");
```

## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
