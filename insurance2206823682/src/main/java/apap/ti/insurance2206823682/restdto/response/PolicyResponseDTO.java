package apap.ti.insurance2206823682.restdto.response;

import java.util.Date;



import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PolicyResponseDTO {

    private String id;


    private CompanyResponseDTO company;
    

    private PatientResponseDTO patient;
    
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;
    
    private long totalCoverage;

    private long totalCovered;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;
    
}
