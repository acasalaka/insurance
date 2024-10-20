package apap.ti.insurance2206823682.restservice;

import apap.ti.insurance2206823682.restdto.response.CoverageResponseDTO;

public interface CoverageRestService {

    CoverageResponseDTO getCoverageById(Long idCoverage);
    
}
