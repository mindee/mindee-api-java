package com.mindee.v1;

import static com.mindee.TestingUtilities.getResourcePath;

import com.mindee.input.LocalInputSource;
import com.mindee.input.PageOptions;
import com.mindee.input.PageOptionsOperation;
import com.mindee.v1.clientOptions.PredictOptions;
import com.mindee.v1.parsing.common.AsyncPredictResponse;
import com.mindee.v1.parsing.common.Document;
import com.mindee.v1.parsing.common.Job;
import com.mindee.v1.parsing.common.PredictResponse;
import com.mindee.v1.product.invoice.InvoiceV4;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class MindeeClientTest {

  @Test
  void givenAClientForInvoice_withFile_parse_thenShouldCallMindeeApi() throws IOException {

    var predictResponse = new PredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    PredictResponse<InvoiceV4> response = mindeeClient
      .parse(InvoiceV4.class, new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf")));

    Assertions.assertNotNull(response.getDocument());
  }

  @Test
  void givenAClientForInvoice_withInputStream_parse_thenShouldCallMindeeApi() throws IOException {

    var predictResponse = new PredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    PredictResponse<InvoiceV4> response = mindeeClient
      .parse(
        InvoiceV4.class,
        new LocalInputSource(
          Files.newInputStream(getResourcePath("file_types/pdf/blank_1.pdf")),
          ""
        )
      );

    Assertions.assertNotNull(response.getDocument());
  }

  @Test
  void givenAClientForInvoice_withByteArray_parse_thenShouldCallMindeeApi() throws IOException {

    var predictResponse = new PredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    PredictResponse<InvoiceV4> response = mindeeClient
      .parse(
        InvoiceV4.class,
        new LocalInputSource(Files.readAllBytes(getResourcePath("file_types/pdf/blank_1.pdf")), "")
      );

    Assertions.assertNotNull(response.getDocument());
  }

  @Test
  void givenAClientForInvoiceAndPageOptions_parse_thenShouldOperateCutOnPagesAndCallTheHttpClientCorrectly() throws IOException {

    List<Integer> pageNumberToKeep = new ArrayList<>();
    pageNumberToKeep.add(1);
    var predictResponse = new PredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    PredictResponse<InvoiceV4> response = mindeeClient
      .parse(
        InvoiceV4.class,
        new LocalInputSource(getResourcePath("file_types/pdf/multipage.pdf")),
        new PageOptions(pageNumberToKeep, PageOptionsOperation.KEEP_ONLY, 0)
      );

    Assertions.assertNotNull(response.getDocument());
  }

  @Test
  void givenADocumentUrl_whenParsed_shouldCallApiWithCorrectParams() throws IOException {
    URL docUrl = new URL("https://this.document.does.not.exist");
    var predictResponse = new PredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    PredictResponse<InvoiceV4> response = mindeeClient.parse(InvoiceV4.class, docUrl);
    Assertions.assertNotNull(response.getDocument());
  }

  @Test
  void givenAnAsyncDoc_whenEnqueued_shouldInvokeApiCorrectly() throws IOException {

    var localInputSource = new LocalInputSource(getResourcePath("file_types/pdf/blank_1.pdf"));

    Job job = new Job(LocalDateTime.now(), "someid", LocalDateTime.now(), "Completed", null);

    var predictResponse = new AsyncPredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(job);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    var predictOptions = PredictOptions.builder().allWords(Boolean.TRUE).build();
    var jobId = mindeeClient
      .enqueue(InvoiceV4.class, localInputSource, predictOptions)
      .getJob()
      .getId();

    Assertions.assertEquals("someid", jobId);
  }

  @Test
  void givenAnAsyncUrl_whenEnqueued_shouldInvokeApiCorrectly() throws IOException {

    Job job = new Job(LocalDateTime.now(), "someid", LocalDateTime.now(), "completed", null);

    var predictResponse = new AsyncPredictResponse<InvoiceV4>();
    predictResponse.setDocument(new Document<>());
    predictResponse.setApiRequest(null);
    predictResponse.setJob(job);

    var mindeeClient = new MindeeClient(new FakeMindeeApiV1<>(predictResponse));

    var jobId = mindeeClient.enqueue(InvoiceV4.class, new URL("https://fake.pdf")).getJob().getId();

    Assertions.assertEquals("someid", jobId);
  }
}
