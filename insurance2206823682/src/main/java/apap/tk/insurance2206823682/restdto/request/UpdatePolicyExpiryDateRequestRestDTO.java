package apap.tk.insurance2206823682.restdto.request;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdatePolicyExpiryDateRequestRestDTO {
    private String id;
    private Date expiryDate;
}
