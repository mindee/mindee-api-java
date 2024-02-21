This guide will help you get the most of the Mindee Java client library to easily extract data from your documents.

> 📘 **Info**
>
> The library is written and compiled with Java 8. This guide assumes you have a compatible JVM language installed

## Installation

### Prerequisites
Installation using [Apache Maven](https://maven.apache.org/install.html) is recommended.

The library is tested on Java versions 8 and 11.

Other installation methods and/or Java versions may work, but are not officially supported.

### Maven
The easiest way to use the Mindee client library for your project is by adding 
the maven dependency in your project's POM:

```shell
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
    <mindee.sdk.version>3.x.x</mindee.sdk.version>
</properties>
```
For the latest version of the Library please refer to our [maven central repository](https://mvnrepository.com/artifact/com.mindee.sdk/mindee-api-java)

### Development Installation
If you'll be modifying the source code, you'll need to follow these steps to get started.

1. First clone the repo.

```shell
git clone git@github.com:mindee/mindee-api-java.git
```

2. Navigate to the cloned directory and install all required libraries.

```shell
mvn clean install 
```

## Updating the Library
When starting out it is recommended that you use the latest release version of the library from
our [maven central repository](https://mvnrepository.com/artifact/com.mindee.sdk/mindee-api-java).

Future updates can be made using the [maven versions plugin](https://www.mojohaus.org/versions-maven-plugin/).

```shell
mvn versions:use-next-releases -Dincludes=com.mindee.sdk:mindee-api-java
```

## Usage
Using Mindee's APIs can be broken down into the following steps:
1. [Initialize a `Client`](#initializing-the-client)
2. [Load a file](#loading-a-document-file)
3. [Send the file](#sending-a-document) to Mindee's API
4. [Retrieve the response](#retrieving-the-response)
5. [Process the response](#processing-the-response) in some way

Let's take a deep dive into how this works.

### Initializing the Client
The `com.mindee.MindeeClient` class centralizes document configurations into a single class.

The `MindeeClient` requires your [API key](https://developers.mindee.com/docs/make-your-first-request#create-an-api-key).

You can either pass these directly to the constructor or through environment variables.

#### Pass the API key directly
```java
import com.mindee;

// Init a new client and passing the key directly
MindeeClient client = new MindeeClient("<your mindee api key>");
```

#### Set the API key in the environment
API keys can be set as environment variables.

The following environment variable will set the global API key:
```shell
MINDEE_API_KEY="my-api-key"
```

Then in your code:
```java
// Init a new client without an API key
MindeeClient client = new MindeeClient();
```

### HttpClient Customizations
Mindee's API lives on the internet and many internal applications on corporate networks may therefore need to configure an HTTP proxy to access it.
This is possible by using a `MindeeClient` configured to use a user provided instance  of the `com.mindee.http.MindeeApi` interface.

There are a few layers to this:
* The default implementation of `com.mindee.http.MindeeApi` interface is `com.mindee.http.MindeeHttpApi`
* `MindeeHttpApi` can be initialized with an Apache HttpComponents `HttpClientBuilder`.
* `HttpClientBuilder` can be configured for use cases like proxying requests, custom authentication schemes, setting SSL Context etc.

To Configure a `MindeeClient` to use a proxy, the following code can be referenced.

```java

import com.mindee.MindeeClient;
import com.mindee.input.LocalInputSource;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoice.InvoiceV4;
import java.io.File;
import java.io.IOException;

public class SimpleMindeeClient {
    public static void main(String[] args) throws IOException {

      // You can also configure things like caching, custom HTTPS certs,
      // timeouts and connection pool sizes here.
      // See: https://hc.apache.org/httpcomponents-client-5.1.x/current/httpclient5/apidocs/org/apache/hc/client5/http/impl/classic/HttpClientBuilder.html
      String proxyHost = "myproxy.local";
      int proxyPort = 8181;
      HttpHost proxy = new HttpHost(proxyHost, proxyPort);

      DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
      HttpClientBuilder httpclientBuilder = HttpClients.custom().setRoutePlanner(routePlanner);

      // Build MindeeHttpAPI using the HtppClientBuilder
      MindeeHttpApi mindeeHttpApi = MindeeHttpApi.builder()
          .mindeeSettings(new MindeeSettings("my-api-key"))
          .httpClientBuilder(httpclientBuilder)
          .build();

      // Inject the MindeeHttpAPI instance directly into the MindeeClient
      MindeeClient mindeeClient = new MindeeClient(mindeeHttpApi);
      
      // Parse a file as usual
      PredictResponse<InvoiceV4> invoiceDocument = mindeeClient.parse(
          InvoiceV4.class,
          LocalInputSource(new File("/path/to/the/file.ext"))
      );
    }
}
```

### Loading a Source File
Before being able to send a file to the API, it must first be loaded.

You don't need to worry about different MIME types, the library will take care of handling
all supported types automatically.

Once a file is loaded, interacting with it is done in exactly the same way, regardless
of how it was loaded.

Loading a file allows performing PDF operations on it.

There are a few different ways of loading a document file, depending on your use case:
* [File](#file-object)
* [Base64](#base64)
* [Byte Array](#bytes)

You can also send distant files.
However, in this case nothing is done or can be done locally.
* [URL](#URL)


#### File Object
Load a `java.io.File` object.
When using this option you do not need to pass in a file name - the API uses the `file.getName()` method to get the file name.

```java
LocalInputSource localInputSource = new LocalInputSource(
    new File("path/to/document/document.pdf")
);
```

#### Base64
Load file contents from a base64-encoded string.

**Note**: The filename of the encoded file is required when calling the method.

```java
String b64String = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLD....";
LocalInputSource localInputSource = new LocalInputSource(b64String, "document.pdf");
```

#### Bytes
Load file contents from a byte array.

**Note**: The original filename of the encoded file is required when calling the method.

```java
// Get Byte Array from a File, Multipart File, Input Stream, or as a method parameter
byte[] fileAsBytes = ....;
LocalInputSource localInputSource = new LocalInputSource(fileAsBytes, "document.pdf");
PredictResponse<InvoiceV4> response = mindeeClient.parse(InvoiceV4.class, LocalInputSource);
```

### URL
Alternatively, an HTTPS URL can be loaded:
```java
URL documentUrl = new URL("https://path/to/document");
PredictResponse<InvoiceV4> response = mindeeClient.parse(InvoiceV4.class, documentUrl);
```

### Parsing a Document
The `MindeeClient` has multiple overloaded `parse` methods available for parsing the documents 
and you will get `LocalInputSource`.

This can be done by implicitly by calling the `parse(Class<T> type)` method with the product type's class.
This is detailed in each product-specific guide.


#### Off-the-Shelf Documents
Simply setting the correct class is enough:
```java
// After the document has been loaded
PredictResponse<ReceiptV4> receiptV4Inference = mindeeClient.parse(ReceiptV4.class, localInputSource);
```

For more fine-grained control over parsing the documents you can have a look on the `parse` override method.

#### Custom Documents
In this case, you will have two ways to handle them.

The first one enables the possibility to use a class object which represents a kind of dictionary where,
keys will be the name of each field define in your Custom API model (on the Mindee platform).

It also requires that you instantiate a new `Endpoint` object to define the information of your custom API built.
```java
CustomEndpoint endpoint = new CustomEndpoint(
    "wnine",
    "john",
    "1.0" // optional
);

PredictResponse<CustomV1> customDocument = mindeeClient.parse(localInputSource, endpoint);
```

The second one is using your own class.
See more information [here](#java-api-builder)

### Processing the Response
Regardless of the model, it will be encapsulated in a `Document` object and therefore will have the following attributes:
* `inference` — [Inference](#inference)

#### Inference
Regroup the prediction on all the pages of the document and the prediction for all the document.
* `documentPrediction` — [Document level prediction](#document-level-prediction)
* `pages` — [Page level prediction](#page-level-prediction)

#### Document Level Prediction
The Response object for each document type has an attribute that represents data extracted from the entire document.
It's possible to have the same field in various pages, but at the document level only the highest confidence field data 
will be shown (this is all done automatically at the API level).

```java
PredictResponse<InvoiceV4> response = documentClient.parse(
    InvoiceV4.class,
    localInputSource
);

// print the complete response
System.out.println(response.toString());

// print all prediction data
System.out.println(response.getDocument().toString());

// print the document-level prediction
System.out.println(
    response.getDocument().getInference().getPrediction().toString()
);

// print the page-level predictions
response.getDocument().getInference().getPages().forEach(
  page -> System.out.println(page.toString())
);
```

Each inference specific class will have its own specific attributes, these correspond to the various fields extracted from the document.


#### Page Level Prediction
The `pagesPrediction` attribute is a list of `prediction` objects, often of the same class as the [`documentPrediction` attribute](#document-level-prediction).
But sometimes, it could have a different class which would extend the `documentPrediction` class.

Each page element contains the data extracted for a particular page of the document.


## Questions?
[Join our Slack](https://join.slack.com/t/mindee-community/shared_invite/zt-2d0ds7dtz-DPAF81ZqTy20chsYpQBW5g)
