package apap.ti.insurance2206823682.dto.request;

import java.util.UUID;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateCompanyRequestDTO extends AddCompanyRequestDTO {
    private UUID id;
}
