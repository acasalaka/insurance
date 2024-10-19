package apap.ti.insurance2206823682.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdatePolicyRequestDTO {
    
    @NotNull(message = "Tanggal expired tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date expiryDate;

    
}
