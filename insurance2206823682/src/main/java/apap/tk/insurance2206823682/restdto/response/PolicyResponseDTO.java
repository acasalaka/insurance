package apap.tk.insurance2206823682.restdto.response;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyResponseDTO {

    private String id;

    private UUID companyId;
    private String companyName;

    private UUID patientId;
        
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    
    private long totalCoverage;

    private long totalCovered;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;

    private String createdBy;

    private String updatedBy;

    private boolean isDeleted;
}
