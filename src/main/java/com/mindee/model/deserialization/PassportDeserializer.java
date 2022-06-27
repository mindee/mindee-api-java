package com.mindee.model.deserialization;

import static com.mindee.utils.DeserializationUtils.dateFromJsonNode;
import static com.mindee.utils.DeserializationUtils.fieldFromJsonNode;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.mindee.model.documenttype.PassportResponse;
import com.mindee.model.documenttype.PassportResponse.PassportDocument;
import com.mindee.model.documenttype.PassportResponse.PassportPage;
import com.mindee.model.fields.Field;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PassportDeserializer extends StdDeserializer<PassportResponse> {

  private static final ObjectMapper MAPPER = new ObjectMapper();

  public PassportDeserializer(Class<?> vc) {
    super(vc);
  }

  public PassportDeserializer() {
    this(null);
  }

  @Override
  public PassportResponse deserialize(JsonParser jsonParser,
      DeserializationContext deserializationContext)
      throws IOException, JacksonException {

    PassportResponse passportResponse = new PassportResponse();
    passportResponse.setType("passport");
    List<PassportPage> pages = new ArrayList<>();

    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    passportResponse.setRawResponse(MAPPER.treeToValue(node, Map.class));
    JsonNode inference = node.get("document").get("inference");
    JsonNode documentLevelPrediction = inference.get("prediction");
    ArrayNode jsonPages = (ArrayNode) inference.get("pages");
    for (JsonNode pageNode : jsonPages) {

      JsonNode predication = pageNode.get("prediction");
      ArrayNode givenNameNodes = (ArrayNode) predication.get("given_names");
      List<Field> givenNames = new ArrayList<>();
      for (JsonNode givenNameNode : givenNameNodes) {
        givenNames.add(fieldFromJsonNode(givenNameNode));
      }
      PassportPage page = PassportPage.builder()
          .page(pageNode.get("id").asInt())
          .country(fieldFromJsonNode(predication.get("country")))
          .id_number(fieldFromJsonNode(predication.get("id_number")))
          .birthDate(dateFromJsonNode(predication.get("birth_date"), "value"))
          .expiryDate(dateFromJsonNode(predication.get("expiry_date"), "value"))
          .issuanceDate(dateFromJsonNode(predication.get("issuance_date"), "value"))
          .birthPlace(fieldFromJsonNode(predication.get("birth_place")))
          .gender(fieldFromJsonNode(predication.get("gender")))
          .surname(fieldFromJsonNode(predication.get("surname")))
          .mrz1(fieldFromJsonNode(predication.get("mrz1")))
          .mrz2(fieldFromJsonNode(predication.get("mrz2")))
          .givenNames(givenNames)
          .build();

      pages.add(page);
    }
    ArrayNode givenNameNodes = (ArrayNode) documentLevelPrediction.get("given_names");
    List<Field> documentLevelGivenNames = new ArrayList<>();
    for (JsonNode givenNameNode : givenNameNodes) {
      documentLevelGivenNames.add(fieldFromJsonNode(givenNameNode));
    }
    PassportDocument document = PassportDocument.builder()
        .country(fieldFromJsonNode(documentLevelPrediction.get("country")))
        .id_number(fieldFromJsonNode(documentLevelPrediction.get("id_number")))
        .birthDate(dateFromJsonNode(documentLevelPrediction.get("birth_date"), "value"))
        .expiryDate(dateFromJsonNode(documentLevelPrediction.get("expiry_date"), "value"))
        .issuanceDate(dateFromJsonNode(documentLevelPrediction.get("issuance_date"), "value"))
        .birthPlace(fieldFromJsonNode(documentLevelPrediction.get("birth_place")))
        .gender(fieldFromJsonNode(documentLevelPrediction.get("gender")))
        .surname(fieldFromJsonNode(documentLevelPrediction.get("surname")))
        .mrz1(fieldFromJsonNode(documentLevelPrediction.get("mrz1")))
        .mrz2(fieldFromJsonNode(documentLevelPrediction.get("mrz2")))
        .givenNames(documentLevelGivenNames)
        .build();

    passportResponse.setPassport(document);
    passportResponse.setPassports(pages);

    return passportResponse;
  }

}
