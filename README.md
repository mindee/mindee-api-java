[![License: MIT](https://img.shields.io/github/license/mindee/mindee-api-java)](https://opensource.org/licenses/MIT) [![GitHub Workflow Status](https://img.shields.io/github/actions/workflow/status/mindee/mindee-api-java/build.yml)](https://github.com/mindee/mindee-api-java) [![Version](https://img.shields.io/maven-central/v/com.mindee.sdk/mindee-api-java)](https://mvnrepository.com/artifact/com.mindee.sdk/mindee-api-java)

# Mindee API Helper Library for Java
Quickly and easily connect to Mindee's API services using Java.

## Quick Start
Here's the TL;DR of getting started.

First, get an [API Key](https://developers.mindee.com/docs/create-api-key)

Include the following maven dependency in your project to use the helper library:
```xml
<dependency>
  <artifactId>mindee-api-java</artifactId>
  <groupId>com.mindee.sdk</groupId>
  <version>${mindee.sdk.version}</version>
</dependency>
```

Where `${mindee.sdk.version}` is the version shown here:

![Version](https://img.shields.io/maven-central/v/com.mindee.sdk/mindee-api-java)


## Loading a File and Parsing It
The `MindeeClient` class is the entry point for most of the helper library features.

## Synchronously Parsing a File
This is the easiest and fastest way to integrate into the Mindee API.

However, not all products are available in synchronous mode.

### Global Documents
These classes are available in the `com.mindee.product` package:

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoice.InvoiceV4;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient("my-api-key");

    // Load a file from disk
    LocalInputSource localInputSource = new LocalInputSource(
        "/path/to/the/file.ext"
    );
    // Parse the file
    Document<InvoiceV4> response = mindeeClient.parse(
        InvoiceV4.class,
        localInputSource
    );
    // Print a summary of the parsed data
    System.out.println(response.getDocument().toString());
  }
}
```

**Note for Region-Specific Documents:**

Each region will have its own package within the general `com.mindee.product` package.

For example USA-specific classes will be in the `com.mindee.product.us` package:
```java
import com.mindee.product.us.bankcheck.BankCheckV1;
```

### Custom Documents (API Builder)
```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.custom.CustomV1;
import com.mindee.http.Endpoint;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient("my-api-key");
    
    // Init the endpoint for the custom document
    Endpoint endpoint = new Endpoint("my-endpoint", "my-account");

    // Load a file from disk
    LocalInputSource localInputSource = new LocalInputSource(
        "src/main/resources/invoices/invoice1.pdf"
    );
    // Parse the file
    Document<CustomV1> customDocument = mindeeClient.parse(
        localInputSource,
        endpoint
    );
  }
}
```

## Synchronously Parsing a File
This allows for easier handling of bursts of documents sent.

Some products are only available asynchronously, check the example code
directly on the Mindee platform.

### Enqueue and Parse a File
The client library will take care of handling the polling requests for you.

This is the easiest way to get started.

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.AsyncPredictResponse;
import com.mindee.product.internationalid.InternationalIdV2;
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

    // Parse the file asynchronously
    AsyncPredictResponse<InternationalIdV2> response = mindeeClient.enqueueAndParse(
        InternationalIdV2.class,
        inputSource
    );

    // Print a summary of the response
    System.out.println(response.toString());
  }
}
```

### Enqueue and Parse a Webhook Response
This is an optional way of handling asynchronous APIs.

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.input.WebhookSource;
import com.mindee.product.internationalid.InternationalIdV2;

import java.io.IOException;

public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient("my-api-key");

    // Load a file from disk
    LocalInputSource localInputSource = new LocalInputSource(
      "/path/to/the/file.ext"
    );
    // Enqueue the file
    String jobId = client.enqueue(InternationalIdV2.class, localInputSource)
      .getJob().getId();
    
    // Load the JSON string sent by the Mindee webhook callback.
    //
    // Reading the callback data will vary greatly depending on which
    // HTTP server you are using, and is beyond the scope of this example.
    WebhookSource webhookSource = new WebhookSource("{'json': 'data'}");
    
    // You can also use a File object as the input.
    //WebhookSource webhookSource = new WebhookSource(new File("/path/to/file.json"));

    AsyncPredictResponse<InternationalIdV2> response = mindeeClient.loadPrediction(
      InternationalIdV2.class,
      new WebhookSource(webhookStream)
    );

    // Print a summary of the parsed data
    System.out.println(response.getDocument().toString());
  }
}
```


## Further Reading
Complete details on the working of the library are available in the following guides:

* [Java Library: Overview](https://developers.mindee.com/docs/java-ocr-getting-started)
* [Java Library: Output Fields](https://developers.mindee.com/docs/java-field)
* [Custom APIs (API Builder)](https://developers.mindee.com/docs/java-api-builder)
* [Invoice API](https://developers.mindee.com/docs/java-invoice-ocr)
* [Receipt API](https://developers.mindee.com/docs/java-receipt-ocr)
* [Financial Document API](https://developers.mindee.com/docs/java-financial-document-ocr)
* [Passport API](https://developers.mindee.com/docs/java-passport-ocr)
* [License Plate API](https://developers.mindee.com/docs/java-license-plates-ocr)
* [US Bank Check API](https://developers.mindee.com/docs/java-us-bank-check-ocr)

You can view the source code on [GitHub](https://github.com/mindee/mindee-api-nodejs).

You can also take a look at the
**[Reference Documentation](https://mindee.github.io/mindee-api-java/)**.

## License
Copyright © Mindee

Available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).


## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
