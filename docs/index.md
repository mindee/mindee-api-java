First, get an [API Key](https://developers.mindee.com/docs/create-api-key)

Then, add the following pom dependency:
```xml
<dependencies>
    ...
    <dependency>
      <artifactId>mindee-api-java</artifactId>
      <groupId>com.mindee.sdk</groupId>
      <version>${mindee.sdk.version}</version>
    </dependency>
</dependency>
<properties>
    ...
    <mindee.sdk.version>3.0.0</mindee.sdk.version>
</properties>
```
For other dependency management tools, please refer to the [Maven Repository](https://mvnrepository.com/artifact/com.mindee.sdk/mindee-api-java)

The following code sample gives a quick overview of how to use this library

```java
import com.mindee;
import com.mindee.parsing;
import com.mindee.parsing.invoice;

// Configure client with an api key
MindeeClient mindeeClient = MindeeClientInit.create("<MINDEE API KEY>");

// load a document from a file (alternatively, as a base64 string
// or a byte array)
DocumentToParse documentToParse = mindeeClient.loadDocument(new File("/path/to/the/file.ext"));

// Parse the document (this calls the MINDEE Receipt API over https)
Document<InvoiceV4Inference> invoiceDocument = mindeeClient.parse(
InvoiceV4Inference.class,
documentToParse);
  
logger.info(invoiceDocument.toString());
```

Complete details on the working of the library are available in the following guides:
* [Overview](java-getting-started)
* [Java Custom OCR](java-api-builder)
* [Java Invoice OCR](java-invoice-ocr)
* [Java Receipt OCR](java-receipt-ocr)
* [Java Passport OCR](java-passport-ocr)
* [Java Licenses plates OCR](java-passport-ocr)
* [Java Shipping containers OCR](java-passport-ocr)
* [Java US Bank Check OCR](java-passport-ocr)

You can view the source code on [GitHub](https://github.com/mindee/mindee-api-java).

&nbsp;
&nbsp;
**Questions?**
<img alt="Slack Logo Icon" style="display:inline!important" src="https://files.readme.io/5b83947-Slack.png" width="20" height="20">&nbsp;&nbsp;[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-1jv6nawjq-FDgFcF2T5CmMmRpl9LLptw)
