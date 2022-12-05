package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptDocument;
import com.mindee.model.documenttype.ReceiptV3Response.ReceiptPage;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.Tax;
import com.mindee.model.fields.Time;
import com.mindee.model.ocr.PageContent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

@JsonIgnoreProperties(ignoreUnknown = true)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
@Data
public class ReceiptV3Response extends BaseDocumentResponse implements PredictionApiResponse<ReceiptDocument, ReceiptPage> {

  private ReceiptDocument document;
  private List<ReceiptPage> pages;

  @Override
  public String documentSummary() {
    StringBuilder stringBuilder = new StringBuilder("-----Receipt data-----\n");
    stringBuilder.append(String.format("Filename: %s\n", this.getFilename()));
    stringBuilder.append(String.format("Total amount including taxes: %f\n",
      (this.document.getTotalIncl()!=null?this.document.getTotalIncl().getValue():0.0d) ));
    stringBuilder.append(String.format("Total amount excluding taxes: %f\n",
      (this.document.getTotalExcl()!=null?this.document.getTotalExcl().getValue():0.0d)));
    stringBuilder.append(String.format("Date: %s\n", this.document.getDate().getValue()));
    stringBuilder.append(String.format("Category: %s\n", this.document.getCategory().getValue()));
    stringBuilder.append(String.format("Time: %s\n", this.document.getTime().getValue()));
    stringBuilder.append(
        String.format("Merchant name: %s\n", this.document.getMerchantName().getValue()));
    stringBuilder.append(String.format("Taxes: %s\n",
        this.document.getTaxes().stream()
            .map(Tax::taxSummary)
            .collect(Collectors.joining("\n       "))
    ));
    stringBuilder.append(String.format("Total taxes: %s\n", (this.document.getTotalTax()!=null?this.document.getTotalTax().getValue():"")));
    stringBuilder.append(
        String.format("Locale: %s\n", this.document.getLocale().localeSummary()));
    stringBuilder.append("----------------------");
    return stringBuilder.toString();
  }


  @Getter
  @EqualsAndHashCode
  @ToString
  @AllArgsConstructor
  @SuperBuilder(toBuilder = true)
  private static abstract class BaseReceipt {

    private final Locale locale;
    @JsonProperty("total_incl")
    private final Amount totalIncl;
    private final Date date;
    private final Field category;
    @JsonProperty("supplier")
    private final Field merchantName;
    private final Time time;
    private final List<Tax> taxes;
    private final Amount totalTax;
    private final Amount totalExcl;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class ReceiptPage extends BaseReceipt implements PageLevelResponse {

    private final int page;
    private final Orientation orientation;
    @JsonProperty("page_content")
    private final PageContent fullText;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  @JsonIgnoreProperties(ignoreUnknown = true)
  @SuperBuilder(toBuilder = true)
  @Jacksonized
  public static final class ReceiptDocument extends BaseReceipt implements DocumentLevelResponse {

  }

}
