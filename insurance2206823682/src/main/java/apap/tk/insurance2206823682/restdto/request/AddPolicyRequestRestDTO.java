package apap.tk.insurance2206823682.restdto.request;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import apap.tk.insurance2206823682.model.Coverage;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestRestDTO {

    // @Column(nullable = false)
    private UUID companyId;

    // @Column(nullable = false)
    private UUID patientId;

    private Date expiryDate;
}


