package com.example;

import com.example.product.invoice.CombinedInvoiceDocument;
import com.mindee.http.MindeeHttpException;
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
    // generate an output path
    Path output = Paths.get(filePath.getPath().replace(".pdf", ".rst"));
    if (Files.exists(output)) {
      System.out.println("File exists: " + output);
      return;
    }

    LocalInputSource inputSource = new LocalInputSource(filePath);
    CombinedInvoiceDocument invoice = client.parseEstInvoice(inputSource);

    // print to console the invoice data as rST
    System.out.println(invoice);

    // save the contents in a rST file
    byte[] bytes = invoice.toString().getBytes(StandardCharsets.UTF_8);
    Files.write(output, bytes);
  }

  @Test
  public void runFiles() throws IOException, InterruptedException {
    File folder = new File("/home/ianare/Documents/invoices_EST/PDF");
    File[] files = folder.listFiles((dir, name) -> name.endsWith(".pdf"));
    for (File file : files) {
      try {
        runFile(file);
      } catch (MindeeHttpException err) {
        System.out.println(file.getPath());
        System.out.println(err.toString());
      }
    }
  }
}
