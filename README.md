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
        new File("/path/to/the/file.ext")
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

### Region-Specific Documents
Each region will have its own package within the general `com.mindee.product` package.

For example USA-specific classes will be in the `com.mindee.product.us` package:

```java
import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.us.bankcheck.BankCheckV1;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {
  public static void main(String[] args) throws IOException {

    // Init a new client
    MindeeClient mindeeClient = new MindeeClient("my-api-key");

    // Load a file from disk
    LocalInputSource localInputSource = new LocalInputSource(
        new File("/path/to/the/file.ext")
    );
    // Parse the file
    Document<BankCheckV1> response = mindeeClient.parse(
            BankCheckV1.class,
            localInputSource
    );
    // Print a summary of the parsed data
    System.out.println(response.getDocument().toString());
  }
}
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
        new File("src/main/resources/invoices/invoice1.pdf")
    );
    // Parse the file
    Document<CustomV1> customDocument = mindeeClient.parse(
        localInputSource,
        endpoint
    );
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
* [Shipping Container API](https://developers.mindee.com/docs/java-shipping-containers-ocr)
* [US Bank Check API](https://developers.mindee.com/docs/java-us-bank-check-ocr)

You can view the source code on [GitHub](https://github.com/mindee/mindee-api-nodejs).


## License
Copyright © Mindee

Available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).


## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
