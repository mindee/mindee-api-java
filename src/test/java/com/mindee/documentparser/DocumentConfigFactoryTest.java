package com.mindee.documentparser;

import com.mindee.http.Endpoint;
import com.mindee.model.customdocument.CustomDocumentResponse;
import com.mindee.model.documenttype.FinancialDocumentResponse;
import com.mindee.model.documenttype.InvoiceV3Response;
import com.mindee.model.documenttype.ReceiptV3Response;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

class DocumentConfigFactoryTest {

  @Test
  void givenCustomDocumentParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfig() {
    DocumentConfig<CustomDocumentResponse> config = DocumentConfigFactory.getDocumentConfigFromApiKey(
        "1213223",
        "custom",
        "owner");

    Assert.assertNotNull(config);
    Assert.assertEquals("api_builder", config.getApiType());
    Assert.assertEquals("custom", config.getDocumentType());
    Assert.assertTrue(config.getEndpoints().size() == 1);
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("1213223", endpoint.getApiKey());
    Assert.assertEquals("1", endpoint.getVersion());
    Assert.assertEquals("custom", endpoint.getKeyName());
    Assert.assertEquals("owner", endpoint.getOwner());
    Assert.assertEquals("custom", endpoint.getUrlName());
  }

  @Test
  void givenOffTheShelfInvoiceParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<InvoiceV3Response> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType(InvoiceV3Response.class, "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("invoice", config.getDocumentType());
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

    DocumentConfig<ReceiptV3Response> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType(ReceiptV3Response.class, "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("receipt", config.getDocumentType());
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
  void givenOffTheShelfFinDocParams_whenFactoryInvoked_shouldReturnCorrectDocumentConfigs() {

    DocumentConfig<FinancialDocumentResponse> config = DocumentConfigFactory
        .getDocumentConfigForOffTheShelfDocType(FinancialDocumentResponse.class,
            "12324343");

    Assert.assertNotNull(config);
    Assert.assertEquals("financial_doc", config.getDocumentType());
    Assert.assertTrue(config.getEndpoints().size() == 2);
    Assert.assertEquals("off_the_shelf", config.getApiType());
    Endpoint endpoint = config.getEndpoints().get(0);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("invoice", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("invoices", endpoint.getUrlName());
    endpoint = config.getEndpoints().get(1);
    Assert.assertEquals("12324343", endpoint.getApiKey());
    Assert.assertEquals("3", endpoint.getVersion());
    Assert.assertEquals("receipt", endpoint.getKeyName());
    Assert.assertEquals("mindee", endpoint.getOwner());
    Assert.assertEquals("expense_receipts", endpoint.getUrlName());
  }
}
