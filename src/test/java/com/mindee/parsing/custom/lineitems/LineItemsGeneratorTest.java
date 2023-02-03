package com.mindee.parsing.custom.lineitems;

import java.util.Arrays;
import java.util.List;

import com.mindee.geometry.PolygonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LineItemsGeneratorTest {

  @Test
  public void withFieldsToConvertTo2LinesItemsMustGetOnly2Lines() {
    // given
    List<Field> fields = Arrays.asList(
        new Field(
            "birthDates",
            Arrays.asList(
                new FieldValue(
                    "01/01/1990",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.764, 0.351),
                        Arrays.asList(0.846, 0.351),
                        Arrays.asList(0.846, 0.36),
                        Arrays.asList(0.764, 0.36)))),
                new FieldValue(
                    "01/01/20",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.765, 0.387),
                        Arrays.asList(0.847, 0.387),
                        Arrays.asList(0.847, 0.396),
                        Arrays.asList(0.766, 0.396)))))),
        new Field("names",
            Arrays.asList(
                new FieldValue(
                    "Chez Mindee",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.059, 0.351),
                        Arrays.asList(0.129, 0.351),
                        Arrays.asList(0.129, 0.36),
                        Arrays.asList(0.059, 0.36)))),
                new FieldValue(
                    "Kevin",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.136, 0.351),
                        Arrays.asList(0.224, 0.351),
                        Arrays.asList(0.224, 0.36),
                        Arrays.asList(0.136, 0.36)))),
                new FieldValue(
                    "Mindee",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.059, 0.388),
                        Arrays.asList(0.129, 0.388),
                        Arrays.asList(0.129, 0.397),
                        Arrays.asList(0.059, 0.397)))),
                new FieldValue(
                  "Ianare",
                    PolygonUtils.getFrom(Arrays.asList(
                        Arrays.asList(0.136, 0.388),
                        Arrays.asList(0.189, 0.388),
                        Arrays.asList(0.189, 0.397),
                        Arrays.asList(0.136, 0.397)))))));

    // then
    LineItems lineItems = LineItemsGenerator.generate(
        fields,
        new Anchor("birthDates"));

    Assertions.assertNotNull(lineItems);
    Assertions.assertEquals(2, lineItems.getRows().size());
    Assertions.assertEquals("Chez Mindee",
        lineItems.getRows().get(0).getFields().get("names").getValues().get(0)
            .getContent());
    Assertions.assertEquals("Kevin",
        lineItems.getRows().get(0).getFields().get("names").getValues().get(1)
            .getContent());
    Assertions.assertEquals("01/01/1990",
        lineItems.getRows().get(0).getFields().get("birthDates").getValues().get(0)
            .getContent());
    Assertions.assertEquals("Mindee",
        lineItems.getRows().get(1).getFields().get("names").getValues().get(0)
            .getContent());
    Assertions.assertEquals("Ianare",
        lineItems.getRows().get(1).getFields().get("names").getValues().get(1)
            .getContent());
    Assertions.assertEquals("01/01/20",
        lineItems.getRows().get(1).getFields().get("birthDates").getValues().get(0)
            .getContent());
  }

}
