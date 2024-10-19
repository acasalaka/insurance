package apap.ti.insurance2206823682.service;

import java.util.List;
import java.util.UUID;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Coverage;

public interface CompanyService {
    List<Company> getAllCompany();
    Company addCompany(Company company);
    String calculateTotalCoverageForCompany(Company company);
    String formatCoverageNames(List<Coverage> coverageNames);
    Company getCompanyById(UUID id);
    Company updateCompany(Company company);
    void deleteCompany(Company company);
    long getTotalCoverage(Company company);
    boolean checkCanDeleteCompany(Company company);
}
