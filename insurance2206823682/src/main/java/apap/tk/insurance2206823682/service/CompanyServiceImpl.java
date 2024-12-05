package apap.tk.insurance2206823682.service;



import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.model.UsedCoverageOfPolicy;
import apap.tk.insurance2206823682.repository.CompanyDb;
import apap.tk.insurance2206823682.repository.CoverageDb;
import apap.tk.insurance2206823682.repository.UsedCoverageOfPolicyDb;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDb companyDb;

    @Autowired
    private CoverageDb coverageDb;

    @Autowired
    private UsedCoverageOfPolicyDb usedCoverageOfPolicyDb;

    @Override
    public Company addCompany(Company company) {
        return companyDb.save(company);
    }

    @Override
    public Company getCompanyById(UUID id){
        return companyDb.findById(id).orElse(null);
    }

    @Override
    public List<Coverage> getCoverages(UUID id){
        Company company = getCompanyById(id);
        return company.getListCoverageCompany();
    }

    @Override
    public Long getTotalCoverage(UUID id){
        Long total = 0L;

        for (Coverage coverage : getCoverages(id)){
            total += coverage.getCoverageAmount();
        }

        return total;
    }

    @Override
    public List<Coverage> getUsedCoverages(String policyId){
        List<UsedCoverageOfPolicy> usedCoverageOfPolicy = usedCoverageOfPolicyDb.findByPolicyId(policyId);
        List<Coverage> coverages = new ArrayList<>();
        for (UsedCoverageOfPolicy used : usedCoverageOfPolicy){
            Coverage coverageAdded = coverageDb.findById(used.getCoverageId()).orElse(null);
            coverages.add(coverageAdded);
        }
        return coverages;
    }
}

