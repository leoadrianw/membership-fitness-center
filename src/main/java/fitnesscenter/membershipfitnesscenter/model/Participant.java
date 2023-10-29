package fitnesscenter.membershipfitnesscenter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @JsonProperty("phone_number")
    @Column(name="phone_number")
    private String phoneNumber;

    @JsonProperty("is_verified")
    @Column(name="is_verified")
    private boolean isVerified;

    @JsonProperty("verification_code")
    @Column(name="verification_code")
    private String verificationCode;

    @JsonProperty("reset_token")
    @Column(name="reset_token")
    private String resetToken;

    @JsonProperty("payment_status")
    @Column(name="payment_status")
    private boolean paymentStatus;

    @JsonProperty("payment_otp")
    @Column(name="payment_otp")
    private String paymentOTP;

    @JsonProperty("payment_otp_expiration")
    @Column(name="payment_otp_expiration")
    private LocalDateTime paymentOTPExpiration;

    @JsonProperty("bill_amount")
    @Column(name="bill_amount")
    private BigDecimal billAmount;

    @JsonProperty("is_bill_verified")
    @Column(name="is_bill_verified")
    private boolean isBillVerified;

    @JsonProperty("credit_card_info")
    @Column(name="credit_card_info")
    private String creditCardInfo;

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public String getResetToken() {
        return resetToken;
    }


}
