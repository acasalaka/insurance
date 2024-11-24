package apap.tk.insurance2206823682.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.repository.CompanyDb;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDb companyDb;

    @Override
    public Company addCompany(Company company) {
        return companyDb.save(company);
    }
}

