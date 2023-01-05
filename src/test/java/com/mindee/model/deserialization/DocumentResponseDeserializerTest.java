package com.mindee.model.deserialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mindee.model.documenttype.ReceiptV3Response;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptDocument;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptPage;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;


class DocumentResponseDeserializerTest {

  private static final List<Class> VERSION_BASED_RESPONSE_TYPES = Arrays.asList(
    ReceiptV3Response.class
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
}
