package com.example;

import com.example.product.invoice.CombinedInvoiceDocument;
import com.mindee.input.LocalInputSource;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SampleClientTest {
  private final SampleClient client;

  public SampleClientTest() {
    client = new SampleClient();
  }

  protected void runFile(File filePath) throws IOException, InterruptedException {
    LocalInputSource inputSource = new LocalInputSource(filePath);
    CombinedInvoiceDocument invoice = client.parseLvaInvoice(inputSource);

    // print to console the invoice data as rST
    System.out.println(invoice);

    // save the contents in a rST file
    Path output = Paths.get(filePath.getPath().replace(".pdf", ".rst"));
    byte[] bytes = invoice.toString().getBytes(StandardCharsets.UTF_8);
    Files.write(output, bytes);
  }

  @Test
  public void runFiles() throws IOException, InterruptedException {
    File folder = new File("/home/ianare/Documents/lva");
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));
    for (File file : files) {
      runFile(file);
    }
  }
}
