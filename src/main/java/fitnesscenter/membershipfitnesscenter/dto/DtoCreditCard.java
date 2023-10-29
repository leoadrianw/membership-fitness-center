package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoCreditCard {
    @JsonProperty("card_no")
    private String cardNo;
    private String cvv;
    @JsonProperty("expired_date")
    private String expiredDate;
    @JsonProperty("owner_name")
    private String ownerName;
}
