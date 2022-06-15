package com.mindee.model.mappers;

import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse.FinancialDocument;
import com.mindee.model.documenttype.FinancialDocumentResponse.FinancialDocumentPage;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.InvoiceResponse.InvoiceDocument;
import com.mindee.model.documenttype.InvoiceResponse.InvoicePage;
import com.mindee.model.documenttype.ReceiptResponse;
import com.mindee.model.documenttype.ReceiptResponse.ReceiptDocument;
import com.mindee.model.documenttype.ReceiptResponse.ReceiptPage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FinancialDocumentResponseMapper {

  FinancialDocumentResponseMapper INSTANCE = Mappers.getMapper(
      FinancialDocumentResponseMapper.class);

  @Mapping(source = "invoice", target = "financialDocument")
  @Mapping(source = "invoices", target = "financialDocuments")
  FinancialDocumentResponse invoiceResponseToFinancialDocumentResponse(
      InvoiceResponse invoiceResponse);

  FinancialDocument invoiceToFinancialDocument(InvoiceDocument invoiceDocument);

  FinancialDocumentPage invoicePageToFinancialDocumentPage(InvoicePage invoicePage);

  @Mapping(source = "receipt", target = "financialDocument")
  @Mapping(source = "receipts", target = "financialDocuments")
  FinancialDocumentResponse receiptResponseToFinancialDocumentResponse(
      ReceiptResponse receiptResponse);

  FinancialDocument receiptToFinancialDocument(ReceiptDocument receiptDocument);

  FinancialDocumentPage receiptPageToFinancialDocumentPage(ReceiptPage invoicePage);


}
