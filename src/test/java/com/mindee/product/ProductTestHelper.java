package com.mindee.product;

import org.junit.jupiter.api.Assertions;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ProductTestHelper {

  public static void assertStringEqualsFile(String expected, String filePath) throws IOException {
    String[] actualLines = expected.split(System.lineSeparator());
    List<String> expectedLines = Files.readAllLines(Paths.get(filePath));
    String expectedSummary = String.join(String.format("%n"), expectedLines);
    String actualSummary = String.join(String.format("%n"), actualLines);

    Assertions.assertEquals(expectedSummary, actualSummary);
  }
}
