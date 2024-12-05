package apap.tk.insurance2206823682.restservice;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonFormat;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.model.Policy;
import apap.tk.insurance2206823682.repository.CompanyDb;
import apap.tk.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.service.CompanyService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CompanyRestServiceImpl implements CompanyRestService {
    
    @Autowired
    CompanyService companyService;

    @Autowired
    CompanyDb companyDb;

    @Override
    public List<CoverageResponseDTO> getCoveragesByIdCompany(UUID id){
        List<Coverage> coverages = companyService.getCoverages(id);

        return coverages.stream()
        .map(this::convertToCoverageResponseDTO)
        .collect(Collectors.toList());

    }

    @Override
    public List<CompanyResponseDTO> getAllCompany(){
        List<Company> companies = companyDb.findAll();


        return companies.stream()
        .map(this::converToCompanyResponseDTO)
        .collect(Collectors.toList());
    }


    public CompanyResponseDTO converToCompanyResponseDTO(Company company) {
        var dto = new CompanyResponseDTO();
        dto.setId(company.getId());
        dto.setName(company.getName());
        dto.setContact(company.getContact());
        dto.setAddress(company.getAddress());
        dto.setEmail(company.getEmail());
        dto.setCreatedAt(company.getCreatedAt());
        dto.setUpdatedAt(company.getUpdatedAt());
        dto.setDeletedAt(company.getDeletedAt());

        return dto;
    }

    public CoverageResponseDTO convertToCoverageResponseDTO(Coverage coverage) {
        var dto = new CoverageResponseDTO();
        dto.setId(coverage.getId());
        dto.setName(coverage.getName());
        dto.setCoverageAmount(coverage.getCoverageAmount());
        dto.setCreatedAt(coverage.getCreatedAt());
        dto.setUpdatedAt(coverage.getUpdatedAt());
        
        return dto;
    }


}
