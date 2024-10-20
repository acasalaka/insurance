package apap.ti.insurance2206823682.restservice;

import java.util.ArrayList;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.repository.CompanyDb;
import apap.ti.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206823682.restdto.response.CoverageResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompanyRestServiceImpl implements CompanyRestService{

    @Autowired
    private CompanyDb companyDb;

    @Autowired
    private CoverageRestService coverageRestService;


    @Override
    public CompanyResponseDTO getCompanyById(UUID idCompany){
        var company = companyDb.findById(idCompany).orElse(null);

        if (company == null) {
            return null;
        }
        
        return companyToCompanyResponseDTO(company);
    }

    public CompanyResponseDTO companyToCompanyResponseDTO(Company company){
        var companyResponseDTO = new CompanyResponseDTO();
        companyResponseDTO.setId(company.getId());
        companyResponseDTO.setName(company.getName());
        companyResponseDTO.setEmail(company.getEmail());
        companyResponseDTO.setAddress(company.getAddress());

        if (company.getListCoverage() != null){
            var listCoverageResponseDTO = new ArrayList<CoverageResponseDTO>();
            company.getListCoverage().forEach(coverage -> {
                listCoverageResponseDTO.add(coverageRestService.getCoverageById(coverage.getId()));
            });

            companyResponseDTO.setListCoverage(listCoverageResponseDTO);
        }
    
        companyResponseDTO.setCreatedAt(company.getCreatedAt());
        companyResponseDTO.setUpdatedAt(company.getUpdatedAt());
        return companyResponseDTO;

    }
}
