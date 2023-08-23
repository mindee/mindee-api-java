package com.mindee.product.custom;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindee.parsing.common.PredictResponse;
import com.mindee.parsing.custom.lineitems.Anchor;
import com.mindee.parsing.custom.lineitems.Line;
import com.mindee.parsing.custom.lineitems.LineItems;
import com.mindee.parsing.custom.lineitems.LineItemsGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

class CustomV1WithLineItemsTest {

  @Test
  void givenACustomDocument_expected_3_lines() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.findAndRegisterModules();

    JavaType type = objectMapper.getTypeFactory().constructParametricType(PredictResponse.class,
      CustomV1.class);
    PredictResponse<CustomV1> customDocumentInference = objectMapper.readValue(
      new File("src/test/resources/products/custom/response_v1/line_items/single_table_01.json"),
      type);

    LineItems lineItems = LineItemsGenerator.generate(
      Arrays.asList("beneficiary_birth_date", "beneficiary_number", "beneficiary_name", "beneficiary_rank"),
      customDocumentInference.getDocument().getInference().getPrediction().getFields(),
      new Anchor("beneficiary_name", 0.011d)
    );

    Assertions.assertNotNull(lineItems);
    Assertions.assertEquals(3, lineItems.getRows().size());
    Line firstLine = lineItems.getRows().get(0);
    Assertions.assertEquals(0.059, firstLine.getBbox().getMinX());
    Assertions.assertEquals(0.351, firstLine.getBbox().getMinY());
    Assertions.assertEquals(0.3, firstLine.getBbox().getMaxX());
    Assertions.assertEquals(0.36, firstLine.getBbox().getMaxY());
    Assertions.assertEquals(4, firstLine.getFields().size());
    Assertions.assertTrue(firstLine.getFields().containsKey("beneficiary_birth_date"));
    Assertions.assertTrue(firstLine.getFields().containsKey("beneficiary_number"));
    Assertions.assertTrue(firstLine.getFields().containsKey("beneficiary_name"));
    Assertions.assertTrue(firstLine.getFields().containsKey("beneficiary_rank"));
    Assertions.assertEquals("2010-07-18", lineItems.getRows().get(1).getFields().get("beneficiary_birth_date").getValue());
    Assertions.assertEquals("3", lineItems.getRows().get(2).getFields().get("beneficiary_rank").getValue());
  }
}
