package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.ReceiptDeserializer;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.PaymentDetails;
import com.mindee.model.fields.Tax;
import com.mindee.model.fields.Time;
import com.mindee.model.ocr.PageContent;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonDeserialize(using = ReceiptDeserializer.class)
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true, includeFieldNames = true)
@Data
public class ReceiptResponse extends BaseDocumentResponse {

  private ReceiptDocument receipt;
  private List<ReceiptPage> receipts;

  @Getter
  @AllArgsConstructor
  @EqualsAndHashCode
  @ToString
  private static abstract class BaseReceipt {

    private final Locale locale;
    private final Amount totalIncl;
    private final Date date;
    private final Field category;
    private final Field merchantName;
    private final Time time;
    private final List<Tax> taxes;
    private final Amount totalTax;
    private final Amount totalExcl;

  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class ReceiptPage extends BaseReceipt {

    private final int page;
    private final Orientation orientation;
    private final PageContent fullText;

    @Builder
    public ReceiptPage(Locale locale, Amount totalIncl, Date date,
        Field category, Field merchantName, Time time,
        List<Tax> taxes, Amount totalTax, Amount totalExcl,
        Orientation orientation, int page, PageContent fullText) {
      super(locale, totalIncl, date, category, merchantName, time, taxes, totalTax, totalExcl);
      this.page = page;
      this.orientation = orientation;
      this.fullText = fullText;
    }
  }

  @Override
  public String documentSummary(){
    StringBuilder stringBuilder = new StringBuilder("-----Receipt data-----\n");
    stringBuilder.append(String.format("Filename: %s\n",this.getFilename()));
    stringBuilder.append(String.format("Total amount including taxes: %f\n",this.receipt.getTotalIncl().getValue()));
    stringBuilder.append(String.format("Total amount excluding taxes: %f\n",this.receipt.getTotalExcl().getValue()));
    stringBuilder.append(String.format("Date: %s\n",this.receipt.getDate().getValue()));
    stringBuilder.append(String.format("Category: %s\n",this.receipt.getCategory().getValue()));
    stringBuilder.append(String.format("Time: %s\n",this.receipt.getTime().getValue()));
    stringBuilder.append(String.format("Merchant name: %s\n",this.receipt.getMerchantName().getValue()));
    stringBuilder.append(String.format("Taxes: %s\n",
        this.receipt.getTaxes().stream()
            .map(Tax::getTaxSummary)
            .collect(Collectors.joining("\n       "))
    ));
    stringBuilder.append(String.format("Total taxes: %s\n",this.receipt.getTotalTax().getValue()));
    stringBuilder.append(String.format("Locale: %s\n", this.receipt.getLocale().getLocaleSummary()));
    stringBuilder.append("----------------------");
    return stringBuilder.toString();
  }

  @Value
  @EqualsAndHashCode(callSuper = true)
  @ToString(callSuper = true, includeFieldNames = true)
  public static final class ReceiptDocument extends BaseReceipt {

    @Builder
    public ReceiptDocument(Locale locale, Amount totalIncl, Date date,
        Field category, Field merchantName, Time time,
        List<Tax> taxes, Amount totalTax, Amount totalExcl) {
      super(locale, totalIncl, date, category, merchantName, time, taxes, totalTax, totalExcl);
    }
  }

}
