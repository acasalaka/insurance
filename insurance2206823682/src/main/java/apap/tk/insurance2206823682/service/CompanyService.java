package apap.tk.insurance2206823682.service;

import java.util.List;
import java.util.UUID;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;

public interface CompanyService {
    Company addCompany(Company company);
    Company getCompanyById(UUID id);
    List<Coverage> getCoverages(UUID id);
    List<Coverage> getUsedCoverages(String policyId);
    Long getTotalCoverage(UUID id);
}
