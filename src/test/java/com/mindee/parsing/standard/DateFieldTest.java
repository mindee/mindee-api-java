package com.mindee.parsing.standard;

import com.mindee.geometry.Point;
import com.mindee.geometry.Polygon;
import java.time.LocalDate;
import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DateFieldTest {

  @Test
  void testConstructor_mustCreate() {
    DateField date =
        new DateField(
            LocalDate.parse("2018-04-01"),
            0.1,
            new Polygon(Arrays.asList(
                new Point(0.016, 0.707),
                new Point(0.414, 0.707),
                new Point(0.414, 0.831),
                new Point(0.016, 0.831)
            )),
            0,
            true
        );

    Assertions.assertEquals("2018-04-01", date.getValue().toString());
    Assertions.assertTrue(date.getIsComputed());
  }
}
