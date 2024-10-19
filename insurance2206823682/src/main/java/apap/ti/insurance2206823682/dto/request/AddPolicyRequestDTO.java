package apap.ti.insurance2206823682.dto.request;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import apap.ti.insurance2206823682.model.Company;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPolicyRequestDTO {
    @NotNull(message = "Tanggal expired tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date expiryDate;

    @NotNull(message = "Company tidak boleh kosong")
    private Company company;

    // @NotNull
    // private Boolean isCoverageLoaded;
}
