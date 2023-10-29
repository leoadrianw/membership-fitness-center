package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoRegisterRequest {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    @JsonProperty("credit_card")
    private DtoCreditCard creditCard;
}
