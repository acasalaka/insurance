package apap.ti.insurance2206823682.dto.request;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpgradePatientRequestDTO {
    private UUID id;

    @NotNull(message = "Kelas tidak boleh kosong")
    private Integer pClass;
      
}
