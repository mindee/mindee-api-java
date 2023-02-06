package com.mindee.parsing.custom.lineitems;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.mindee.geometry.PolygonUtils;
import com.mindee.parsing.custom.ListField;
import com.mindee.parsing.custom.ListFieldValue;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LineItemsGeneratorTest {

  @Test
  void withFieldsToConvertTo2LinesItemsMustGetOnly2Lines() {
    // given
    Map<String, ListField> fakes = new HashMap<>();

    fakes.put(
      "birthDates",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "01/01/1990",
                1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.764, 0.351),
                    Arrays.asList(0.846, 0.351),
                    Arrays.asList(0.846, 0.36),
                    Arrays.asList(0.764, 0.36)))),
            new ListFieldValue(
                "01/01/20",
              1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.765, 0.387),
                    Arrays.asList(0.847, 0.387),
                    Arrays.asList(0.847, 0.396),
                    Arrays.asList(0.766, 0.396))))))
    );

    fakes.put(
      "names",
      new ListField(
        1.0,
        Arrays.asList(
            new ListFieldValue(
                "Chez Mindee",
              1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.059, 0.351),
                    Arrays.asList(0.129, 0.351),
                    Arrays.asList(0.129, 0.36),
                    Arrays.asList(0.059, 0.36)))),
            new ListFieldValue(
                "Kevin",
              1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.136, 0.351),
                    Arrays.asList(0.224, 0.351),
                    Arrays.asList(0.224, 0.36),
                    Arrays.asList(0.136, 0.36)))),
            new ListFieldValue(
                "Mindee",
              1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.059, 0.388),
                    Arrays.asList(0.129, 0.388),
                    Arrays.asList(0.129, 0.397),
                    Arrays.asList(0.059, 0.397)))),
            new ListFieldValue(
              "Ianare",
              1.0,
                PolygonUtils.getFrom(Arrays.asList(
                    Arrays.asList(0.136, 0.388),
                    Arrays.asList(0.189, 0.388),
                    Arrays.asList(0.189, 0.397),
                    Arrays.asList(0.136, 0.397))))))
    );

    // then
    LineItems lineItems = LineItemsGenerator.generate(
      Arrays.asList("names", "birthDates"),
      fakes,
      new Anchor("birthDates"));

    Assertions.assertNotNull(lineItems);
    Assertions.assertEquals(2, lineItems.getRows().size());
    Assertions.assertEquals("Chez Mindee Kevin",
        lineItems.getRows().get(0).getFields().get("names").getValue());
    Assertions.assertEquals("01/01/1990",
        lineItems.getRows().get(0).getFields().get("birthDates").getValue());
    Assertions.assertEquals("Mindee Ianare",
        lineItems.getRows().get(1).getFields().get("names").getValue());
    Assertions.assertEquals("01/01/20",
        lineItems.getRows().get(1).getFields().get("birthDates").getValue());
  }

}
