package apap.ti.insurance2206823682.restdto.request;

import java.sql.Date;
import java.util.UUID;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestRestDTO {
    @NotNull(message = "Tanggal expired tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date expiryDate;

    @NotNull(message = "Company tidak boleh kosong")
    private UUID company;
    
}
