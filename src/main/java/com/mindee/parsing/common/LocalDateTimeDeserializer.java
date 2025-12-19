package com.mindee.parsing.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;

/**
 * Deserializer for LocalDateTime.
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

  /**
   * Deserialize a LocalDateTime from a JSON string.
   */
  @Override
  public LocalDateTime deserialize(
      JsonParser jsonParser,
      DeserializationContext deserializationContext
  ) throws IOException {
    DateTimeFormatter formatter = new DateTimeFormatterBuilder()
      .parseCaseInsensitive()
      .append(DateTimeFormatter.ISO_LOCAL_DATE)
      .appendLiteral('T')
      .append(DateTimeFormatter.ISO_LOCAL_TIME)
      .optionalStart()
      .appendOffsetId()
      .toFormatter();
    String dateString = jsonParser.getValueAsString();
    TemporalAccessor temporalAccessor = formatter
      .parseBest(dateString, ZonedDateTime::from, LocalDateTime::from);
    if (temporalAccessor instanceof ZonedDateTime) {
      return ((ZonedDateTime) temporalAccessor)
        .withZoneSameInstant(ZoneOffset.UTC)
        .toLocalDateTime();
    } else {
      return ((LocalDateTime) temporalAccessor);
    }
  }
}
