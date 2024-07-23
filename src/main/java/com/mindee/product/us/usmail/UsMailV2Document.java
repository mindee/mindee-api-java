package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * US Mail API version 2.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsMailV2Document extends Prediction {

  /**
   * The addresses of the recipients.
   */
  @JsonProperty("recipient_addresses")
  protected List<UsMailV2RecipientAddress> recipientAddresses = new ArrayList<>();
  /**
   * The names of the recipients.
   */
  @JsonProperty("recipient_names")
  protected List<StringField> recipientNames = new ArrayList<>();
  /**
   * The address of the sender.
   */
  @JsonProperty("sender_address")
  protected UsMailV2SenderAddress senderAddress;
  /**
   * The name of the sender.
   */
  @JsonProperty("sender_name")
  protected StringField senderName;

  @Override
  public boolean isEmpty() {
    return (
      this.senderName == null
      && this.senderAddress == null
      && (this.recipientNames == null || this.recipientNames.isEmpty())
      && (this.recipientAddresses == null || this.recipientAddresses.isEmpty())
      );
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(
        String.format(":Sender Name: %s%n", this.getSenderName())
    );
    outStr.append(
        String.format(":Sender Address:%n%s", this.getSenderAddress().toFieldList())
    );
    String recipientNames = SummaryHelper.arrayToString(
        this.getRecipientNames(),
        "%n                  "
    );
    outStr.append(
        String.format(":Recipient Names: %s%n", recipientNames)
    );
    String recipientAddressesSummary = "";
    if (!this.getRecipientAddresses().isEmpty()) {
      int[] recipientAddressesColSizes = new int[]{17, 37, 19, 13, 24, 7, 27};
      recipientAddressesSummary =
        String.format("%n%s%n  ", SummaryHelper.lineSeparator(recipientAddressesColSizes, "-"))
          + "| City            "
          + "| Complete Address                    "
          + "| Is Address Change "
          + "| Postal Code "
          + "| Private Mailbox Number "
          + "| State "
          + "| Street                    "
          + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(recipientAddressesColSizes, "="));
      recipientAddressesSummary += SummaryHelper.arrayToString(this.getRecipientAddresses(), recipientAddressesColSizes);
      recipientAddressesSummary += String.format("%n%s", SummaryHelper.lineSeparator(recipientAddressesColSizes, "-"));
    }
    outStr.append(
        String.format(":Recipient Addresses: %s%n", recipientAddressesSummary)
    );
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
