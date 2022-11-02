package com.mindee.model.mappers;

import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse.FinancialDocument;
import com.mindee.model.documenttype.FinancialDocumentResponse.FinancialDocumentPage;
import com.mindee.model.documenttype.InvoiceV3Response;
import com.mindee.model.documenttype.ReceiptV3Response;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FinancialDocumentResponseMapper {

  FinancialDocumentResponseMapper INSTANCE = Mappers.getMapper(
      FinancialDocumentResponseMapper.class);

  @Mapping(source = "document", target = "financialDocument")
  @Mapping(source = "pages", target = "financialDocuments")
  FinancialDocumentResponse invoiceResponseToFinancialDocumentResponse(
      InvoiceV3Response invoiceV3Response);

  FinancialDocument invoiceToFinancialDocument(InvoiceV3Response.InvoiceDocument invoiceDocument);

  FinancialDocumentPage invoicePageToFinancialDocumentPage(InvoiceV3Response.InvoicePage invoicePage);

  @Mapping(source = "document", target = "financialDocument")
  @Mapping(source = "pages", target = "financialDocuments")
  FinancialDocumentResponse receiptResponseToFinancialDocumentResponse(
      ReceiptV3Response receiptResponse);

  FinancialDocument receiptToFinancialDocument(ReceiptV3Response.ReceiptDocument receiptDocument);

  FinancialDocumentPage receiptPageToFinancialDocumentPage(ReceiptV3Response.ReceiptPage invoicePage);


}
