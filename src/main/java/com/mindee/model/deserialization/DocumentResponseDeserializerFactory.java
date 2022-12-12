package com.mindee.model.deserialization;

import com.mindee.model.documenttype.*;
import com.mindee.model.documenttype.invoice.InvoiceV4Response;

public final class DocumentResponseDeserializerFactory {


  private DocumentResponseDeserializerFactory() {
  }


  public static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>, S extends DocumentLevelResponse, U extends PageLevelResponse> DocumentResponseDeserializer<T, S, U> documentResponseDeserializerFromResponseClass(
    Class<T> responseClass) {
    DocumentResponseDeserializer<T, S, U> deserializer = null;
    if (responseClass.equals(InvoiceV3Response.class))
      deserializer = invoiceV3Deserializer();
    if (responseClass.equals(ReceiptV3Response.class))
      deserializer = receiptResponseV3Deserializer();
    if (responseClass.equals(ReceiptV4Response.class))
      deserializer = receiptResponseV4Deserializer();
    if (responseClass.equals(PassportV1Response.class))
      deserializer = passportV1Deserializer();
    if (responseClass.equals(InvoiceV4Response.class))
      deserializer = invoiceResponseV4Deserializer();

    if (deserializer == null)
      throw new RuntimeException(String.format("Deserializer not configured for %s", responseClass.getName()));
    return deserializer;
  }

  private static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>,
    S extends DocumentLevelResponse,
    U extends PageLevelResponse>
  DocumentResponseDeserializer<T, S, U> passportV1Deserializer() {
    return new DocumentResponseDeserializer(PassportV1Response::new, PassportV1Response.PassportDocument.class,
      PassportV1Response.PassportPage.class);
  }

  private static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>,
    S extends DocumentLevelResponse,
    U extends PageLevelResponse>
  DocumentResponseDeserializer<T, S, U> invoiceV3Deserializer() {
    return new DocumentResponseDeserializer(InvoiceV3Response::new, InvoiceV3Response.InvoiceDocument.class,
      InvoiceV3Response.InvoicePage.class);
  }

  private static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>,
    S extends DocumentLevelResponse,
    U extends PageLevelResponse>
  DocumentResponseDeserializer<T, S, U> receiptResponseV3Deserializer() {
    return new DocumentResponseDeserializer(
      ReceiptV3Response::new, ReceiptV3Response.ReceiptDocument.class, ReceiptV3Response.ReceiptPage.class);
  }

  private static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>,
    S extends DocumentLevelResponse,
    U extends PageLevelResponse>
  DocumentResponseDeserializer<T, S, U> receiptResponseV4Deserializer() {
    return new DocumentResponseDeserializer(
      ReceiptV4Response::new, ReceiptV4Response.ReceiptDocument.class, ReceiptV4Response.ReceiptPage.class);
  }

  private static <T extends BaseDocumentResponse & PredictionApiResponse<S, U>,
    S extends DocumentLevelResponse,
    U extends PageLevelResponse>
  DocumentResponseDeserializer<T, S, U> invoiceResponseV4Deserializer() {
    return new DocumentResponseDeserializer(
      InvoiceV4Response::new, InvoiceV4Response.InvoiceV4Document.class, InvoiceV4Response.InvoiceV4Page.class);
  }

}
