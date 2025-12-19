package com.mindee.product.us.usmail;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.mindee.parsing.SummaryHelper;
import com.mindee.parsing.common.Prediction;
import com.mindee.parsing.standard.BooleanField;
import com.mindee.parsing.standard.StringField;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * US Mail API version 3.0 document data.
 */
@Getter
@EqualsAndHashCode(callSuper = false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsMailV3Document extends Prediction {

  /**
   * Whether the mailing is marked as return to sender.
   */
  @JsonProperty("is_return_to_sender")
  protected BooleanField isReturnToSender;
  /**
   * The addresses of the recipients.
   */
  @JsonProperty("recipient_addresses")
  protected List<UsMailV3RecipientAddress> recipientAddresses = new ArrayList<>();
  /**
   * The names of the recipients.
   */
  @JsonProperty("recipient_names")
  protected List<StringField> recipientNames = new ArrayList<>();
  /**
   * The address of the sender.
   */
  @JsonProperty("sender_address")
  protected UsMailV3SenderAddress senderAddress;
  /**
   * The name of the sender.
   */
  @JsonProperty("sender_name")
  protected StringField senderName;

  @Override
  public boolean isEmpty() {
    return (this.senderName == null
      && this.senderAddress == null
      && (this.recipientNames == null || this.recipientNames.isEmpty())
      && (this.recipientAddresses == null || this.recipientAddresses.isEmpty())
      && this.isReturnToSender == null);
  }

  @Override
  public String toString() {
    StringBuilder outStr = new StringBuilder();
    outStr.append(String.format(":Sender Name: %s%n", this.getSenderName()));
    outStr.append(String.format(":Sender Address:%n%s", this.getSenderAddress().toFieldList()));
    String recipientNames = SummaryHelper
      .arrayToString(this.getRecipientNames(), "%n                  ");
    outStr.append(String.format(":Recipient Names: %s%n", recipientNames));
    String recipientAddressesSummary = "";
    if (!this.getRecipientAddresses().isEmpty()) {
      int[] recipientAddressesColSizes = new int[] { 17, 37, 19, 13, 24, 7, 27, 17 };
      recipientAddressesSummary = String
        .format("%n%s%n  ", SummaryHelper.lineSeparator(recipientAddressesColSizes, "-"))
        + "| City            "
        + "| Complete Address                    "
        + "| Is Address Change "
        + "| Postal Code "
        + "| Private Mailbox Number "
        + "| State "
        + "| Street                    "
        + "| Unit            "
        + String.format("|%n%s%n  ", SummaryHelper.lineSeparator(recipientAddressesColSizes, "="));
      recipientAddressesSummary += SummaryHelper
        .arrayToString(this.getRecipientAddresses(), recipientAddressesColSizes);
      recipientAddressesSummary += String
        .format("%n%s", SummaryHelper.lineSeparator(recipientAddressesColSizes, "-"));
    }
    outStr.append(String.format(":Recipient Addresses: %s%n", recipientAddressesSummary));
    outStr.append(String.format(":Return to Sender: %s%n", this.getIsReturnToSender()));
    return SummaryHelper.cleanSummary(outStr.toString());
  }
}
