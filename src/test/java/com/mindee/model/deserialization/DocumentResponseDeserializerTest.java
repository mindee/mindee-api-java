package com.mindee.model.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mindee.model.documenttype.InvoiceV3Response;
import com.mindee.model.documenttype.InvoiceV3Response.InvoiceDocument;
import com.mindee.model.documenttype.InvoiceV3Response.InvoicePage;
import com.mindee.model.documenttype.PassportV1Response;
import com.mindee.model.documenttype.ReceiptV3Response;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptDocument;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptPage;
import com.mindee.model.documenttype.ReceiptV4Response;
import com.mindee.model.documenttype.invoice.InvoiceV4Response;
import com.mindee.model.postprocessing.PassportResponsePostProcessor;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;


class DocumentResponseDeserializerTest {

  private static final List<Class> VERSION_BASED_RESPONSE_TYPES = Arrays.asList(
    ReceiptV3Response.class,
    ReceiptV4Response.class,
    InvoiceV3Response.class,
    PassportV1Response.class
  );
  private static final ObjectMapper objectMapper = new ObjectMapper();

  static {
    SimpleModule module = new SimpleModule();
    for (Class clazz : VERSION_BASED_RESPONSE_TYPES) {
      module.addDeserializer(clazz, DocumentResponseDeserializerFactory.documentResponseDeserializerFromResponseClass(clazz));
    }
    objectMapper.registerModule(module);
  }

  @Test
  public void givenAPassport_whenDeserialized_ReturnsPassport() throws IOException {


    PassportV1Response response = objectMapper.readValue(new File("src/test/resources/data/passport/response_v1/complete.json"),
      PassportV1Response.class);

    UnaryOperator<PassportV1Response> operator = PassportResponsePostProcessor::reconstructMrz;
    Function<PassportV1Response, PassportV1Response> finalOperator = operator.compose(
      PassportResponsePostProcessor::reconstructFullName);
    Assert.assertNotNull(response);
    response = finalOperator.apply(response);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getDocument());
    Assert.assertNotNull(response.getPages());
    Assert.assertTrue(response.getPages().size() > 0);

  }

  @Test
  public void givenAnInvoice_whenDeserialized_ReturnsInvoice() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    DocumentResponseDeserializer<InvoiceV3Response, InvoiceDocument,
      InvoicePage> documentResponseDeserializer = new DocumentResponseDeserializer<>(
      InvoiceV3Response::new, InvoiceV3Response.InvoiceDocument.class, InvoiceV3Response.InvoicePage.class
    );
    SimpleModule module = new SimpleModule();
    module.addDeserializer(
      InvoiceV3Response.class, documentResponseDeserializer);
    objectMapper.registerModule(module);
    InvoiceV3Response response = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v3/complete.json"),
      InvoiceV3Response.class);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getDocument());
    Assert.assertNotNull(response.getPages());
    Assert.assertTrue(response.getPages().size() > 0);
    response.documentSummary();

  }

  @Test
  public void givenAReceipt_whenDeserialized_ReturnsReceipt() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    DocumentResponseDeserializer<ReceiptV3Response, ReceiptDocument,
      ReceiptPage> documentResponseDeserializer = new DocumentResponseDeserializer<>(
      ReceiptV3Response::new, ReceiptV3Response.ReceiptDocument.class, ReceiptV3Response.ReceiptPage.class
    );
    SimpleModule module = new SimpleModule();
    module.addDeserializer(
      ReceiptV3Response.class, documentResponseDeserializer);
    objectMapper.registerModule(module);
    ReceiptV3Response response = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v3/complete.json"),
      ReceiptV3Response.class);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getDocument());
    Assert.assertNotNull(response.getPages());
    Assert.assertTrue(response.getPages().size() > 0);
    response.documentSummary();

  }

  @Test
  public void givenAReceiptV4_whenDeserialized_ReturnsReceipt() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    DocumentResponseDeserializer<ReceiptV4Response, ReceiptV4Response.ReceiptDocument,
      ReceiptV4Response.ReceiptPage> documentResponseDeserializer = new DocumentResponseDeserializer<>(
      ReceiptV4Response::new, ReceiptV4Response.ReceiptDocument.class, ReceiptV4Response.ReceiptPage.class
    );
    SimpleModule module = new SimpleModule();
    module.addDeserializer(
      ReceiptV4Response.class, documentResponseDeserializer);
    objectMapper.registerModule(module);
    ReceiptV4Response response = objectMapper.readValue(new File("src/test/resources/data/receipt/response_v4/complete.json"),
      ReceiptV4Response.class);

    Assert.assertNotNull(response);
    Assert.assertNotNull(response.getDocument());
    Assert.assertNotNull(response.getPages());
    Assert.assertTrue(response.getPages().size() > 0);
    response.documentSummary();

  }

  @Test
  public void givenAnInvoiceV4_whenDeserialized_ReturnsInvoice() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    DocumentResponseDeserializer<InvoiceV4Response, InvoiceV4Response.InvoiceV4Document,
      InvoiceV4Response.InvoiceV4Page> documentResponseDeserializer = new DocumentResponseDeserializer<>(
      InvoiceV4Response::new, InvoiceV4Response.InvoiceV4Document.class, InvoiceV4Response.InvoiceV4Page.class
    );
    SimpleModule module = new SimpleModule();
    module.addDeserializer(
      InvoiceV4Response.class, documentResponseDeserializer);
    objectMapper.registerModule(module);
    InvoiceV4Response response = objectMapper.readValue(new File("src/test/resources/data/invoice/response_v4/complete.json"),
      InvoiceV4Response.class);

    Assertions.assertNotNull(response);
    Assertions.assertNotNull(response.getDocument());
    Assertions.assertNotNull(response.getPages());
    Assertions.assertTrue(response.getPages().size() > 0);
    Assertions.assertEquals(5, response.getDocument().getInvoiceLineItems().length);
  }

}
