package com.mindee.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.PaymentDetails;
import com.mindee.model.fields.Tax;
import com.mindee.model.fields.Time;
import com.mindee.model.ocr.PageContent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DeserializationUtils {

  private static ObjectMapper mapper = new ObjectMapper();

  public static Amount amountFromJsonNode(JsonNode amountPrediction) throws IOException {
    return amountFromJsonNode(amountPrediction, "value");
  }

  public static Amount amountFromJsonNode(JsonNode amountPrediction, String valueKey)
      throws IOException {
    if (amountPrediction == null || getRawValueFromPrediction(amountPrediction, valueKey) == null) {
      return null;
    }
    Amount amount = Amount.builder()
        .rawValue(getRawValueFromPrediction(amountPrediction, valueKey))
        .confidence(getConfidenceFromPrediction(amountPrediction))
        .polygon(getPolygonFromPredication(amountPrediction))
        .page(getPageIdFromPrediction(amountPrediction))
        .reconstructed(false)
        .value(Double.valueOf(amountPrediction.get(valueKey).asDouble()))
        .build();

    return amount;
  }

  public static Date dateFromJsonNode(JsonNode datePrediction) throws IOException {
    return dateFromJsonNode(datePrediction, "value");
  }

  public static Date dateFromJsonNode(JsonNode datePrediction, String valueKey) throws IOException {
    if (datePrediction == null || getRawValueFromPrediction(datePrediction, valueKey) == null) {
      return null;
    }
    Date date = Date.builder()
        .rawValue(getRawValueFromPrediction(datePrediction, valueKey))
        .confidence(getConfidenceFromPrediction(datePrediction))
        .polygon(getPolygonFromPredication(datePrediction))
        .page(getPageIdFromPrediction(datePrediction))
        .reconstructed(false)
        .value(getLocalDateFromString(datePrediction.get(valueKey).asText(),
            DateTimeFormatter.ISO_DATE))
        .build();
    return date;
  }

  public static Field fieldFromJsonNode(JsonNode fieldPrediction) throws IOException {
    return fieldFromJsonNode(fieldPrediction, "value", null);
  }

  public static Field fieldFromJsonNode(JsonNode fieldPrediction, String valueKey,
      String[] extraFields) throws IOException {
    if (fieldPrediction == null || getRawValueFromPrediction(fieldPrediction, valueKey) == null) {
      return null;
    }
    Field.FieldBuilder fieldBuilder = Field.builder();
    if (extraFields != null) {
      for (String field : extraFields) {
        fieldBuilder.extraField(field, fieldPrediction.get(field).asText());
      }
    }

    Field field = fieldBuilder
        .rawValue(getRawValueFromPrediction(fieldPrediction, valueKey))
        .confidence(getConfidenceFromPrediction(fieldPrediction))
        .polygon(getPolygonFromPredication(fieldPrediction))
        .page(getPageIdFromPrediction(fieldPrediction))
        .reconstructed(false)
        .value(fieldPrediction.get(valueKey).asText())
        .build();

    return field;
  }

  public static Locale localeFromJsonNode(JsonNode localePrediction) throws IOException {
    return localeFromJsonNode(localePrediction, "value");
  }

  public static Locale localeFromJsonNode(JsonNode localePrediction, String valueKey)
      throws IOException {
    if (localePrediction == null || getRawValueFromPrediction(localePrediction, valueKey) == null) {
      return null;
    }
    Locale locale = Locale.builder()
        .rawValue(getRawValueFromPrediction(localePrediction, valueKey))
        .confidence(getConfidenceFromPrediction(localePrediction))
        .polygon(getPolygonFromPredication(localePrediction))
        .page(getPageIdFromPrediction(localePrediction))
        .reconstructed(false)
        .value(new java.util.Locale.Builder()
            .setLanguageTag(localePrediction.get(valueKey).asText())
            .build())
        .language(getRawValueFromPrediction(localePrediction, "language"))
        .country(getRawValueFromPrediction(localePrediction, "country"))
        .currency(getRawValueFromPrediction(localePrediction, "currency"))
        .build();

    return locale;
  }

  public static Orientation orientationFromJsonNode(JsonNode orientationPredication,
      String valueKey) {
    if (orientationPredication == null
        || getRawValueFromPrediction(orientationPredication, valueKey) == null) {
      return null;
    }
    final List<Integer> possibleOrientation = Arrays.asList(0, 90, 180, 270);
    Integer degrees = orientationPredication.get(valueKey).asInt();
    Orientation orientation = Orientation.builder()
        .rawValue(getRawValueFromPrediction(orientationPredication, valueKey))
        .confidence(getConfidenceFromPrediction(orientationPredication))
        .reconstructed(false)
        .value(possibleOrientation.contains(degrees) ? degrees : 0)
        .build();

    return orientation;
  }

  public static Tax taxFromJsonNode(JsonNode taxPrediction,
      String valueKey, String rateKey, String codeKey) throws IOException {
    if (taxPrediction == null || getRawValueFromPrediction(taxPrediction, valueKey) == null) {
      return null;
    }
    Tax tax = Tax.builder()
        .rawValue(getRawValueFromPrediction(taxPrediction, valueKey))
        .confidence(getConfidenceFromPrediction(taxPrediction))
        .polygon(getPolygonFromPredication(taxPrediction))
        .page(getPageIdFromPrediction(taxPrediction))
        .reconstructed(false)
        .value(taxPrediction.get(valueKey).asDouble())
        .rate(taxPrediction.get(rateKey) != null ? taxPrediction.get(rateKey).asDouble() : null)
        .code(getRawValueFromPrediction(taxPrediction, codeKey))
        .build();

    return tax;
  }

  public static Time timeFromJsonNode(JsonNode timePrediction) throws IOException {
    return timeFromJsonNode(timePrediction, "value");
  }

  public static Time timeFromJsonNode(JsonNode timePrediction, String valueKey) throws IOException {
    if (timePrediction == null || getRawValueFromPrediction(timePrediction, valueKey) == null) {
      return null;
    }
    Time time = Time.builder()
        .rawValue(getRawValueFromPrediction(timePrediction, valueKey))
        .confidence(getConfidenceFromPrediction(timePrediction))
        .polygon(getPolygonFromPredication(timePrediction))
        .page(getPageIdFromPrediction(timePrediction))
        .reconstructed(false)
        .value(LocalTime.parse(timePrediction.get(valueKey).asText()))
        .build();

    return time;
  }

  public static PaymentDetails paymentDetailsJsonNode(JsonNode paymentDetails, String valueKey,
      String accountNumberKey, String ibanKey, String routingNumberKey,
      String swiftKey) throws IOException {
    if (paymentDetails == null || getRawValueFromPrediction(paymentDetails, valueKey) == null) {
      return null;
    }
    PaymentDetails details = PaymentDetails.builder()
        .rawValue(getRawValueFromPrediction(paymentDetails, valueKey))
        .confidence(getConfidenceFromPrediction(paymentDetails))
        .polygon(getPolygonFromPredication(paymentDetails))
        .page(getPageIdFromPrediction(paymentDetails))
        .reconstructed(false)
        .accountNumber(getRawValueFromPrediction(paymentDetails, accountNumberKey))
        .iban(getRawValueFromPrediction(paymentDetails, ibanKey))
        .routingNumber(getRawValueFromPrediction(paymentDetails, routingNumberKey))
        .swift(getRawValueFromPrediction(paymentDetails, swiftKey))
        .build();
    return details;
  }

  public static List<List<Field>> getAllWordsFromOcrArrayNode(ArrayNode ocrPages, String wordsKey,
      String valueKey)
      throws IOException {
    List<List<Field>> allWords = new ArrayList<>();
    for (JsonNode ocrPage : ocrPages) {
      allWords.add(getAllWordsOnPageFromOcrJsonNode(ocrPage, wordsKey, valueKey));
    }
    return allWords;

  }

  public static List<Field> getAllWordsOnPageFromOcrJsonNode(JsonNode ocrPage, String wordsKey,
      String valueKey)
      throws IOException {

    ArrayNode words = (ArrayNode) ocrPage.get(wordsKey);
    List<Field> pageWords = new ArrayList<>();
    for (JsonNode word : words) {
      Field wordAsField = fieldFromJsonNode(word, valueKey, null);
      pageWords.add(wordAsField);
    }

    return pageWords;

  }

  public static PageContent getPageContentsFromOcr(ArrayNode ocrPages, int index, String wordsKey,
      String valueKey)
      throws IOException {
    if (ocrPages == null || ocrPages.size() == 0) {
      return null;
    }
    if (index < 0 || index >= ocrPages.size()) {
      throw new IllegalArgumentException("Page index invalid");
    }
    JsonNode ocrPage = ocrPages.get(index);

    return PageContent.builder()
        .words(getAllWordsOnPageFromOcrJsonNode(ocrPage, wordsKey, valueKey))
        .build();

  }

  public static String getRawValueFromPrediction(JsonNode abstractPrediction, String valueKey) {
    if (abstractPrediction.get(valueKey) != null
        && !abstractPrediction.get(valueKey).isNull()
        && abstractPrediction.get(valueKey).asText() != null
        && !abstractPrediction.get(valueKey).asText().equalsIgnoreCase("N/A")) {
      return abstractPrediction.get(valueKey).asText();
    }
    return null;
  }

  public static Double getConfidenceFromPrediction(JsonNode abstractPrediction) {
    try {
      return abstractPrediction.get("confidence").asDouble();
    } catch (Exception e) {
      return 0.0;
    }

  }

  public static Integer getPageIdFromPrediction(JsonNode abstractPrediction) throws IOException {
    if (abstractPrediction.get("page_id") != null) {
      return abstractPrediction.get("page_id").asInt();
    } else {
      return null;
    }
  }

  private static List<List<Double>> getPolygonFromPredication(JsonNode abstractPrediction)
      throws IOException {
    if (abstractPrediction.get("polygon") != null) {
      return mapper.readerFor(new TypeReference<List<List<Double>>>() {
      }).readValue(abstractPrediction.get("polygon"));

    }
    return null;
  }

  private static LocalDate getLocalDateFromString(String value, DateTimeFormatter formatter) {
    try {
      return LocalDate.parse(value, formatter);
    } catch (Exception e) {
      return null;
    }

  }

}
