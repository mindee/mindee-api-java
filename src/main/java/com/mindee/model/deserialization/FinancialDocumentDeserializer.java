package com.mindee.model.deserialization;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.ReceiptResponse;
import com.mindee.model.mappers.FinancialDocumentResponseMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class FinancialDocumentDeserializer extends StdDeserializer<FinancialDocumentResponse> {


  private static final ObjectMapper mapper = new ObjectMapper();

  public FinancialDocumentDeserializer(Class<?> vc) {
    super(vc);

  }

  public FinancialDocumentDeserializer() {
    this(null);
  }

  @Override
  public FinancialDocumentResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext) throws IOException, JacksonException {

    //JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    Map responseMap = (Map) mapper.readValue(jsonParser, Map.class);
    Map documentMap = (Map) responseMap.get("document");
    Map inferenceMap = (Map) documentMap.get("inference");
    Map productMap = (Map) inferenceMap.get("product");
    List<String> keys = (List<String>) productMap.get("features");

    if (keys.contains("invoice_number")) {
      InvoiceResponse invoiceResponse = mapper.convertValue(responseMap, InvoiceResponse.class);
      return FinancialDocumentResponseMapper.INSTANCE.invoiceResponseToFinancialDocumentResponse(
          invoiceResponse);

    } else {
      ReceiptResponse receiptResponse = mapper.convertValue(responseMap, ReceiptResponse.class);
      return FinancialDocumentResponseMapper.INSTANCE.receiptResponseToFinancialDocumentResponse(
          receiptResponse);
    }


  }
}
