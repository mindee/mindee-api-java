---
title: Generated API Java
category: 622b805aaec68102ea7fcbc2
slug: java-generated-ocr
parentDoc: 631a062c3718850f3519b793
---
The Java OCR SDK supports generated APIs.
Generated APIs can theoretically support all APIs in a catch-all generic format.

# Quick-Start

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.generated.GeneratedV1;
import com.mindee.http.Endpoint;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {

  public static void main(String[] args) throws IOException, InterruptedException {
    String apiKey = "my-api-key";
    String filePath = "/path/to/the/file.ext";

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient(apiKey);

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    // Configure the endpoint
    Endpoint endpoint = new Endpoint(
      "my-endpoint",
      "my-account",
      "my-version"
    );

    // Parse the file asynchronously
    AsyncPredictResponse<GeneratedV1> response = mindeeClient.enqueueAndParse(
      GeneratedV1.class,
      endpoint,
      inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());

    // Print a summary of the predictions
//  System.out.println(response.getDocumentObj().toString());

    // Print the document-level predictions
//    System.out.println(response.getDocumentObj().getInference().getPrediction().toString());

    // Print the page-level predictions
//    response.getDocumentObj().getInference().getPages().forEach(
//        page -> System.out.println(page.toString())
//    );
  }

}
```

# Generated Endpoints

As shown above, you will need to provide an account and an endpoint name at the very least.

Although it is optional, the version number should match the latest version of your build in most use-cases.
If it is not set, it will default to "1".

# Field Types

## Generated Fields

By default, GeneratedV1 implements only one attribute: 
- **fields**: this attribute is retrievable through `getFields()`, which will return a `Map<String, GeneratedFeature>`.

### Generated Feature

A `GeneratedFeature` is a special type of custom list that extends `ArrayList<`[GeneratedObject](#generated-object-field)`>` and implements the following:

- **asStringField()** (`StringField`): the value of the field as a `StringField`.
- **asAmountField()** (`AmountField`): the value of the field as an `AmountField`.
- **asDateField()** (`DateField`): the value of the field as a `DateField`.
- **asClassificationField()** (`ClassificationField`): the value of the field as a `ClassificationField`.

### Generated Object Field

By default, non-list objects will be stored in a `GeneratedObject` structure, which are an extension of simple hashmaps. These fields have access to the following:

- **asStringField()** (`StringField`): the value of the field as a `StringField`.
- **asAmountField()** (`AmountField`): the value of the field as an `AmountField`.
- **asDateField()** (`DateField`): the value of the field as a `DateField`.
- **asClassificationField()** (`ClassificationField`): the value of the field as a `ClassificationField`.
- **getAsPolygon()** (`Polygon`): representation of the field as a `Polygon`.
- **getPageId()** (`integer`): retrieves the ID of the page the field was found on. Note: this isn't supported on some APIs.
- **getConfidence()** (`float`): retrieves the confidence score for a field, if it exists.


> Note: the `asXXXXField()` methods mentioned above will raise `MindeeException` if they do not correspond to the field's type.


# Questions?

[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
