package com.mindee.model.deserialization;

import com.mindee.model.documenttype.*;

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
}
