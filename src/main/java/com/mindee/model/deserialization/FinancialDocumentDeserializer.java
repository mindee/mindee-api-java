package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceV3Response;
import com.mindee.model.documenttype.ReceiptV3Response;
import com.mindee.model.mappers.FinancialDocumentResponseMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FinancialDocumentDeserializer extends StdDeserializer<FinancialDocumentResponse> {


  private static final ObjectMapper mapper = new ObjectMapper();
  static {
    SimpleModule module = new SimpleModule();
    module.addDeserializer(InvoiceV3Response.class, DocumentResponseDeserializerFactory.documentResponseDeserializerFromResponseClass(InvoiceV3Response.class));
    module.addDeserializer(ReceiptV3Response.class, DocumentResponseDeserializerFactory.documentResponseDeserializerFromResponseClass(ReceiptV3Response.class));
    mapper.registerModule(module);
  }

  public FinancialDocumentDeserializer(Class<?> vc) {
    super(vc);

  }

  public FinancialDocumentDeserializer() {
    this(null);
  }

  @Override
  public FinancialDocumentResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JacksonException {
    Map responseMap = (Map) mapper.readValue(jsonParser, Map.class);
    Map documentMap = (Map) responseMap.get("document");
    Map inferenceMap = (Map) documentMap.get("inference");
    Map productMap = (Map) inferenceMap.get("product");
    List<String> keys = (List<String>) productMap.get("features");

    if (keys.contains("invoice_number")) {
      InvoiceV3Response invoiceV3Response = mapper.convertValue(responseMap, InvoiceV3Response.class);
      return FinancialDocumentResponseMapper.INSTANCE.invoiceResponseToFinancialDocumentResponse(
        invoiceV3Response);

    } else {
      ReceiptV3Response receiptResponse = mapper.convertValue(responseMap, ReceiptV3Response.class);
      return FinancialDocumentResponseMapper.INSTANCE.receiptResponseToFinancialDocumentResponse(
          receiptResponse);
    }


  }
}
