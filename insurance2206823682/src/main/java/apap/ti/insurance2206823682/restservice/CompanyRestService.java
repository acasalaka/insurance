package apap.ti.insurance2206823682.restservice;

import java.util.UUID;

import apap.ti.insurance2206823682.restdto.response.CompanyResponseDTO;

public interface CompanyRestService {
    CompanyResponseDTO getCompanyById(UUID idCompany);

}
