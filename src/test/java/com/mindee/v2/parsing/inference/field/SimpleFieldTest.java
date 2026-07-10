package com.mindee.v2.parsing.inference.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MindeeV2 - SimpleField numeric accessors")
class SimpleFieldTest {

  private static SimpleField of(Object value) {
    return new SimpleField(value, FieldConfidence.Certain, null);
  }

  @Test
  @DisplayName("getDoubleValue accepts BigDecimal (wire path)")
  void getDoubleValue_fromBigDecimal() {
    assertEquals(1.5, of(new BigDecimal("1.5")).getDoubleValue());
  }

  @Test
  @DisplayName("getDoubleValue accepts Double (programmatic path)")
  void getDoubleValue_fromDouble() {
    assertEquals(2.25, of(2.25d).getDoubleValue());
  }

  @Test
  @DisplayName("getDoubleValue accepts Integer / Long / Float")
  void getDoubleValue_fromOtherNumbers() {
    assertEquals(42.0, of(42).getDoubleValue());
    assertEquals(9_000_000_000d, of(9_000_000_000L).getDoubleValue());
    assertEquals(0.5, of(0.5f).getDoubleValue(), 1e-6);
  }

  @Test
  @DisplayName("getDoubleValue on null value returns null")
  void getDoubleValue_null() {
    assertNull(of(null).getDoubleValue());
  }

  @Test
  @DisplayName("getDoubleValue on non-numeric throws ClassCastException")
  void getDoubleValue_nonNumeric_throws() {
    assertThrows(ClassCastException.class, () -> of("nope").getDoubleValue());
    assertThrows(ClassCastException.class, () -> of(Boolean.TRUE).getDoubleValue());
  }

  @Test
  @DisplayName("getBigDecimalValue accepts BigDecimal (wire path)")
  void getBigDecimalValue_fromBigDecimal() {
    assertEquals(new BigDecimal("1.5"), of(new BigDecimal("1.5")).getBigDecimalValue());
  }

  @Test
  @DisplayName("getBigDecimalValue converts other Number types via toString to preserve digits")
  void getBigDecimalValue_fromOtherNumbers() {
    assertEquals(new BigDecimal("2.25"), of(2.25d).getBigDecimalValue());
    assertEquals(new BigDecimal("42"), of(42).getBigDecimalValue());
    assertEquals(new BigDecimal("9000000000"), of(9_000_000_000L).getBigDecimalValue());
  }

  @Test
  @DisplayName("getBigDecimalValue on null value returns null")
  void getBigDecimalValue_null() {
    assertNull(of(null).getBigDecimalValue());
  }

  @Test
  @DisplayName("getBigDecimalValue on non-numeric throws ClassCastException")
  void getBigDecimalValue_nonNumeric_throws() {
    assertThrows(ClassCastException.class, () -> of("nope").getBigDecimalValue());
    assertThrows(ClassCastException.class, () -> of(Boolean.TRUE).getBigDecimalValue());
  }

  @Test
  @DisplayName("toString renders any Number in double-style format")
  void toString_number() {
    assertEquals("12.0", of(12).toString());
    assertEquals("1.5", of(new BigDecimal("1.5")).toString());
    assertEquals("2.25", of(2.25d).toString());
  }
}
