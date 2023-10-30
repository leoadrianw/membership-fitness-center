package fitnesscenter.membershipfitnesscenter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtoForgotPasswordRequest {
    @JsonProperty("reset_token")
    private String resetToken;
    @JsonProperty("new_password")
    private String newPassword;
    @JsonProperty("confirm_new_password")
    private String confirmNewPassword;
}
