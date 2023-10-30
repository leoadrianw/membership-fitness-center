package fitnesscenter.membershipfitnesscenter.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class DtoBillRequest {
    private BigDecimal expectedBillAmount;
}
