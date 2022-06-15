package com.mindee.model.documenttype;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.mindee.model.deserialization.ReceiptDeserializer;
import com.mindee.model.fields.Amount;
import com.mindee.model.fields.Date;
import com.mindee.model.fields.Field;
import com.mindee.model.fields.Locale;
import com.mindee.model.fields.Orientation;
import com.mindee.model.fields.Tax;
import com.mindee.model.fields.Time;
import java.util.List;
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

    @Builder
    public ReceiptPage(Locale locale, Amount totalIncl, Date date,
        Field category, Field merchantName, Time time,
        List<Tax> taxes, Amount totalTax, Amount totalExcl,
        Orientation orientation, int page) {
      super(locale, totalIncl, date, category, merchantName, time, taxes, totalTax, totalExcl);
      this.page = page;
      this.orientation = orientation;
    }
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
