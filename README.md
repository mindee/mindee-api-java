[![Tests](https://github.com/mindee/mindee-api-java/actions/workflows/build.yml/badge.svg)](https://github.com/mindee/mindee-api-java/actions/workflows/build.yml)

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

## Loading a File and Parsing It
The `Client` class is the entry point for most of the helper library features.

### Global Documents
```java
import com.mindee.parsing;
import com.mindee.parsing.invoice;

MindeeClient mindeeClient = new MindeeClient(new MindeeSetings("<MINDEE API KEY>"));

DocumentToParse documentToParse = mindeeClient.loadDocument(new File("/path/to/the/file.ext"));

Document<InvoiceV4Inference> invoiceDocument = mindeeClient.parse(
  InvoiceV4Inference.class,
  documentToParse);
```

### Region-Specific Documents
```java
import com.mindee.parsing;
import com.mindee.parsing.us.bankcheck;

MindeeClient mindeeClient = new MindeeClient(new MindeeSetings("<MINDEE API KEY>"));

DocumentToParse documentToParse = mindeeClient.loadDocument(new File("/path/to/the/file.ext"));

Document<BankCheckV1Inference> bankCheckDocument = mindeeClient.parse(
  BankCheckV1Inference.class,
  documentToParse);
```

### Custom Documents (API Builder)
```java
import com.mindee.parsing;
import com.mindee.parsing.custom;

MindeeClient mindeeClient = new MindeeClient(new MindeeSetings("<MINDEE API KEY>"));
CustomEndpoint customEndpoint = new CustomEndpoint("myProductName", "myAccountName");

DocumentToParse documentToParse = mindeeClient.loadDocument(new File("src/main/resources/invoices/invoice1.pdf"));

Document<CustomV1Inference> customDocument = mindeeClient.parse(
  documentToParse,
  new CustomEndpoint());
```

Client supports multiple input types:

* java.io.File file
* InputStream fileAsStream, String filename
* byte[] fileAsByteArray, String filename
* String fileAsBase64String, String filename

## Further Reading
There's more to it than that for those that need more features, or want to
customize the experience.

All the juicy details are described in the
**[Official Documentation](https://developers.mindee.com/docs/java-ocr-sdk)**.

### Questions, Comments?
We'd love to hear from you!
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw).


## License
Copyright © Mindee

Available as open source under the terms of the [MIT License](https://opensource.org/licenses/MIT).
