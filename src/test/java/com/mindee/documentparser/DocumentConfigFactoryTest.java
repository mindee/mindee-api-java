package com.mindee.documentparser;

import com.mindee.http.Endpoint;
import com.mindee.model.documenttype.CustomDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceResponse;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.ReceiptResponse;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DocumentConfigFactoryTest {

  @Test
  void givenCustomDocumentParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfig() {
    DocumentConfig<CustomDocumentResponse> config = DocumentConfigFactory.getDocumentConfigForCustomDocType(
        "custom",
        "owner",
        "1213223",
        "2.0", "singular", "plural"
    );

    Assert.assertNotNull(config);
    Assert.assertEquals("api_builder", config.getApiType());
    Assert.assertEquals("custom", config.getDocumentType());
    Assert.assertEquals("plural", config.getPluralName());
    Assert.assertEquals("singular", config.getSingularName());
    Assert.assertTrue(config.getEndpoints().size() == 1);
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("1213223", endpoint.getApiKey());
    Assert.assertEquals("2.0", endpoint.getVersion());
    Assert.assertEquals("custom", endpoint.getKeyName());
    Assert.assertEquals("owner", endpoint.getOwner());
    Assert.assertEquals("custom", endpoint.getUrlName());
  }

  @Test
  void givenOffTheShelfInvoiceParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<InvoiceResponse> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType("invoice", "mindee", "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("invoice", config.getDocumentType());
    Assert.assertEquals("invoices", config.getPluralName());
    Assert.assertEquals("invoice", config.getSingularName());
    Assert.assertTrue(config.getEndpoints().size() == 1);
    Assert.assertEquals("off_the_shelf", config.getApiType());
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("invoice", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("invoices", endpoint.getUrlName());

  }

  @Test
  void givenOffTheShelfReceiptParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<ReceiptResponse> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType("receipt", "mindee", "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("receipt", config.getDocumentType());
    Assert.assertEquals("receipts", config.getPluralName());
    Assert.assertEquals("receipt", config.getSingularName());
    Assert.assertTrue(config.getEndpoints().size() == 1);
    Assert.assertEquals("off_the_shelf", config.getApiType());
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("receipt", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("expense_receipts", endpoint.getUrlName());

  }

  @Test
  void givenOffTheShelfPassportParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<PassportResponse> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType("passport", "mindee", "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("passport", config.getDocumentType());
    Assert.assertEquals("passports", config.getPluralName());
    Assert.assertEquals("passport", config.getSingularName());
    Assert.assertTrue(config.getEndpoints().size() == 1);
    Assert.assertEquals("off_the_shelf", config.getApiType());
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("1", endpoint.getVersion());
    Assert.assertEquals("passport", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("passport", endpoint.getUrlName());

  }

  @Test
  void givenOffTheShelfFinDocParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<FinancialDocumentResponse> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType("financial_doc", "mindee",
            "12324343", "dwdwed2112");

    Assert.assertNotNull(config);
    Assert.assertEquals("financial_doc", config.getDocumentType());
    Assert.assertEquals("financialDocs", config.getPluralName());
    Assert.assertEquals("financialDoc", config.getSingularName());
    Assert.assertTrue(config.getEndpoints().size() == 2);
    Assert.assertEquals("off_the_shelf", config.getApiType());
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("invoice", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("invoices", endpoint.getUrlName());
    endpoint = config.getEndpoints().get(1);
    Assert.assertEquals("dwdwed2112", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("receipt", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("expense_receipts", endpoint.getUrlName());
  }
}