package com.example;

import com.mindee.input.LocalInputSource;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;

public class SampleClientTest {
  @Test
  public void hello() throws IOException, InterruptedException {
    SampleClient client = new SampleClient();

    String filePath = "/home/ianare/Documents/sample_baltic_invoice.pdf";

    // Load a file from disk
    LocalInputSource inputSource = new LocalInputSource(new File(filePath));

    client.parseEstInvoice(inputSource);
  }
}
