package apap.tk.insurance2206823682.restservice;

import java.util.List;
import java.util.UUID;

import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;

public interface CompanyRestService {
    List<CoverageResponseDTO> getCoveragesByIdCompany(UUID id);

}
