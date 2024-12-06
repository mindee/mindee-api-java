package com.mindee.input;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.mindee.MindeeClient;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.product.invoice.InvoiceV4;
import java.io.IOException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class URLInputSourceIT {
  private static MindeeClient client;

  @BeforeAll
  static void clientSetUp() {
    client = new MindeeClient();
  }

  @Test
  public void testURLInputSource_shouldSendApiCall() throws IOException {
    URLInputSource remoteSource = URLInputSource.builder(
        "https://github.com/mindee/client-lib-test-data/blob/main/products/invoice_splitter/invoice_5p.pdf?raw=true"
    ).build();
    remoteSource.fetchFile();
    LocalInputSource localSource = remoteSource.toLocalInputSource();
    assertTrue(localSource.getFilename().endsWith(".tmp"));
    assertTrue(localSource.getFilename().startsWith("mindee_temp_20"));
    PredictResponse<InvoiceV4> response = client.parse(InvoiceV4.class, localSource);
    assertEquals(5, response.getDocument().getNPages());
  }
}
