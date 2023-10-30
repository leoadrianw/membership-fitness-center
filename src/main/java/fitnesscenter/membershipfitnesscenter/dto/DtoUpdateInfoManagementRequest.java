package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoUpdateInfoManagementRequest {
    @JsonProperty("new_full_name")
    private String newFullName;
    @JsonProperty("new_credit_card_info")
    private String newCreditCardInfo;
    @JsonProperty("new_password")
    private String newPassword;
}
