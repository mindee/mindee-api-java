The JAVA OCR SDK supports [custom-built API](https://developers.mindee.com/docs/build-your-first-document-parsing-api) from the API Builder.
If your document isn't covered by one of Mindee's Off-the-Shelf APIs, you can create your own API using the [API Builder](https://developers.mindee.com/docs/overview).

For the following examples, we are using our own [W9s custom API](https://developers.mindee.com/docs/w9-forms-ocr) created with the [API Builder](https://developers.mindee.com/docs/overview).

## Quick Start

```java
CustomEndpoint myEndpoint = new CustomEndpoint(
    "wnine",
    "john",
    "1.0" // optional
);

Document<CustomV1Inference> customDocument = mindeeClient
    .parse(documentToParse, myEndpoint);
```

If the `version` argument is set, you'll be required to update it every time a new model is trained.
This is probably not needed for development but essential for production use.

## Parsing Documents
Use the `ParseAsync` method to call the API prediction on your custom document.
The response class and document type must be specified when calling this method.

You have two different ways to parse a custom document.

1. Use the default one (named ``CustomPrediction``):
```java

String path = "/my/path/myfile.ext";
DocumentToParse documentToParse = new DocumentToParse(new File(path));
CustomEndpoint myEndpoint = new CustomEndpoint(
    "wnine",
    "john",
    "1.0" // optional
);

Document<CustomV1Inference> customDocument = mindeeClient.parse(documentToParse, myEndpoint);
```

2. You can also use your own class which will represent the required fields. For example:
```java

// The CustomEndpointInfo annotation is required when using your own model.
// It will be used to know which Mindee API called.

public class WNineV1DocumentPrediction {
  @JsonProperty("name")
  private StringField name;

  @JsonProperty("employerId")
  private StringField employerId;
  
  (...)
}

@EndpointInfo(endpointName = "wnine", accountName = "john" version = "1")
public class WNineV1Inference
  extends Inference Inference<WNineV1DocumentPrediction, WNineV1DocumentPrediction> {
}

String path = "/my/path/myfile.ext";
DocumentToParse documentToParse = new DocumentToParse(new File(path));

Document<WNineV1Inference> myCustomDocument = mindeeClient
    .parse(WNineV1Inference.class, documentToParse);
```

## CustomV1Inference object
All the fields which are defined in the API builder when creating your custom document, are available.

`CustomV1Inference` is an object which contains a document prediction and pages prediction result.
Both property are a `CustomV1DocumentPrediction` (because there are no differences between results) which is a `HashMap<String, ListField>` with the key as a `string` for the name of the field, and a `ListField` as a value.
Each `ListField` contains a list of all values extracted for this field. 

Value fields are accessed via the `Values` of the `ListField` property.

> 📘 **Info**
>
> Both document level and page level objects work in the same way.

### Fields property
A Map with the following structure:
* `confidence`: a `double`
* `values`: a list of `ListFieldValue` which containing a list of all values found for the field.

In the examples below we'll use the `employer_id` field.

```java
String path = "/my/path/myfile.ext";
DocumentToParse documentToParse = new DocumentToParse(new File(path));

Document<WNineV1Inference> myCustomDocument = mindeeClient
    .parse(WNineV1Inference.class, documentToParse);

ListField employerId = document.getInference().getDocumentPrediction().get("employer_id");
```

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
