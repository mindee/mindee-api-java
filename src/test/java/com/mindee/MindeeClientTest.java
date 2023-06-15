package com.mindee;

import com.mindee.input.LocalInputSource;
import com.mindee.http.CustomEndpoint;
import com.mindee.http.MindeeApi;
import com.mindee.http.RequestParameters;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.parsing.common.Document;
import com.mindee.parsing.common.Job;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.custom.CustomV1;
import com.mindee.product.invoice.InvoiceV4;
import com.mindee.pdf.PdfOperation;
import com.mindee.pdf.SplitPdf;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MindeeClientTest {

  MindeeClient client;
  MindeeApi mindeeApi;
  PdfOperation pdfOperation;

  @BeforeEach
  public void setUp() {

    mindeeApi = Mockito.mock(MindeeApi.class);
    pdfOperation = Mockito.mock(PdfOperation.class);
    client = new MindeeClient(pdfOperation, mindeeApi);
  }

  @Test
  void givenAClientForCustom_withFile_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);

    Document<CustomV1> document = client.parse(
        new LocalInputSource(file),
        new CustomEndpoint("", "", ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForCustomAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);

    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    Mockito.when(
            pdfOperation.split(
                Mockito.any()))
        .thenReturn(new SplitPdf(new byte[0], 0));

    Document<CustomV1> document = client.parse(
        new LocalInputSource(file),
        new CustomEndpoint("", "", ""),
        new PageOptions(
            pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any(), Mockito.any());
    Mockito.verify(pdfOperation, Mockito.times(1))
        .split(Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withFile_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);

    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);

    Document<InvoiceV4> document = client.parse(
        InvoiceV4.class,
        new LocalInputSource(file));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withInputStream_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");

    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);

    Document<InvoiceV4> document = client.parse(
        InvoiceV4.class,
        new LocalInputSource(
            Files.newInputStream(file.toPath()),
            ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoice_withByteArray_parse_thenShouldCallMindeeApi()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);

    Document<InvoiceV4> document = client.parse(
        InvoiceV4.class,
        new LocalInputSource(
            Files.readAllBytes(file.toPath()),
            ""));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
  }

  @Test
  void givenAClientForInvoiceAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly()
      throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    Mockito.when(
            pdfOperation.split(
                Mockito.any()))
        .thenReturn(new SplitPdf(new byte[0], 0));

    Document<InvoiceV4> document = client.parse(
        InvoiceV4.class,
        new LocalInputSource(file),
        new PageOptions(
            pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0));

    Assertions.assertNotNull(document);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(Mockito.any(), Mockito.any());
    Mockito.verify(pdfOperation, Mockito.times(1))
        .split(Mockito.any());
  }

  @Test
  void givenADocumentUrl_whenParsed_shouldCallApiWithCorrectParams() throws IOException {

    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<RequestParameters> requestParametersArgumentCaptor = ArgumentCaptor.forClass(
        RequestParameters.class);

    URL docUrl = new URL("https://this.document.does.not.exist");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    Document<InvoiceV4> document = client.parse(
        InvoiceV4.class, docUrl);

    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), requestParametersArgumentCaptor.capture());
    Assertions.assertEquals(InvoiceV4.class, classArgumentCaptor.getValue());
    Assertions.assertEquals(docUrl, requestParametersArgumentCaptor.getValue().getFileUrl());
    Assertions.assertNull(requestParametersArgumentCaptor.getValue().getFile());
    Assertions.assertNull(requestParametersArgumentCaptor.getValue().getFileName());
  }

  @Test
  void givenACustomDocumentUrl_whenParsed_shouldCallApiWithCorrectParams() throws IOException {

    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<CustomEndpoint> customEndpointArgumentCaptor = ArgumentCaptor.forClass(
        CustomEndpoint.class);
    ArgumentCaptor<RequestParameters> requestParametersArgumentCaptor = ArgumentCaptor.forClass(
        RequestParameters.class);

    URL docUrl = new URL("https://this.document.does.not.exist");
    CustomEndpoint endpoint = new CustomEndpoint("dsddw", "dcsdcd", "dsfdd");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(null);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    Document<CustomV1> document = client.parse(
        docUrl, endpoint);

    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), customEndpointArgumentCaptor.capture(),
            requestParametersArgumentCaptor.capture());
    Assertions.assertEquals(CustomV1.class, classArgumentCaptor.getValue());
    Assertions.assertEquals(docUrl, requestParametersArgumentCaptor.getValue().getFileUrl());
    Assertions.assertEquals(endpoint, customEndpointArgumentCaptor.getValue());
    Assertions.assertNull(requestParametersArgumentCaptor.getValue().getFile());
    Assertions.assertNull(requestParametersArgumentCaptor.getValue().getFileName());
  }

  @Test
  void givenAnAsyncDoc_whenEnqueued_shouldInvokeApiCorrectly() throws IOException {

    File file = new File("src/test/resources/invoice/invoice.pdf");
    LocalInputSource localInputSource = new LocalInputSource(file);

    Job job = new Job(LocalDateTime.now(),"someid",LocalDateTime.now(),"Completed");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(job);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    String jobId = client.enqueue(InvoiceV4.class, localInputSource,Boolean.TRUE, null)
        .getJob()
        .map(Job::getId)
        .orElse("");

    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<RequestParameters> requestParametersArgumentCaptor = ArgumentCaptor.forClass(
        RequestParameters.class);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), requestParametersArgumentCaptor.capture());

    RequestParameters requestParameters = requestParametersArgumentCaptor.getValue();
    Assertions.assertEquals(InvoiceV4.class,classArgumentCaptor.getValue());
    Assertions.assertEquals(Boolean.TRUE,requestParameters.getAsyncCall());
    Assertions.assertEquals("invoice.pdf",requestParameters.getFileName());
    Assertions.assertEquals(Boolean.TRUE,requestParameters.getAllWords());
    Assertions.assertNotNull(requestParameters.getFile());
    Assertions.assertTrue(requestParameters.getFile().length > 0);
    Assertions.assertNull(requestParameters.getFileUrl());
    Assertions.assertEquals("someid",jobId);
  }

  @Test
  void givenAnAsyncUrl_whenEnqued_shouldInvokeApiCorrectly() throws IOException {

    Job job = new Job(LocalDateTime.now(),"someid",LocalDateTime.now(),"Completed");
    PredictResponse predictResponse = new PredictResponse();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(job);
    Mockito.when(
            mindeeApi.predict(
                Mockito.any(),
                Mockito.any()))
        .thenReturn(predictResponse);
    String jobId = client.enqueue(InvoiceV4.class,new URL("https://fake.pdf"))
        .getJob()
        .map(Job::getId)
        .orElse("");
    ArgumentCaptor<Class> classArgumentCaptor = ArgumentCaptor.forClass(Class.class);
    ArgumentCaptor<RequestParameters> requestParametersArgumentCaptor = ArgumentCaptor.forClass(
        RequestParameters.class);
    Mockito.verify(mindeeApi, Mockito.times(1))
        .predict(classArgumentCaptor.capture(), requestParametersArgumentCaptor.capture());

    RequestParameters requestParameters = requestParametersArgumentCaptor.getValue();
    Assertions.assertEquals(InvoiceV4.class,classArgumentCaptor.getValue());
    Assertions.assertEquals(Boolean.TRUE,requestParameters.getAsyncCall());
    Assertions.assertNull(requestParameters.getFileName());
    Assertions.assertEquals(Boolean.FALSE,requestParameters.getAllWords());
    Assertions.assertNull(requestParameters.getFile());
    Assertions.assertEquals(new URL("https://fake.pdf"),requestParameters.getFileUrl());
    Assertions.assertEquals("someid",jobId);
  }
}
