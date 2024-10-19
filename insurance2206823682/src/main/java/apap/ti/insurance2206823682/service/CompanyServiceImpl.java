package apap.ti.insurance2206823682.service;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Coverage;
import apap.ti.insurance2206823682.repository.CompanyDb;

@Service
public class CompanyServiceImpl implements CompanyService {
    @Autowired
    private CompanyDb companyDb;

    @Override
    public List<Company> getAllCompany() {
        return companyDb.findByDeletedAtIsNull();
    }

    @Override
    public Company addCompany(Company company) {
        return companyDb.save(company);
    }

    @Override
    public long getTotalCoverage(Company company){
        long totalCoverage = 0;
        for (Coverage coverage : company.getListCoverage()){
            totalCoverage += coverage.getCoverageAmount();
        }
        return totalCoverage;
    }

    @Override
    public String calculateTotalCoverageForCompany(Company company) {
        // Sum up all the coverage amounts for this company
        double totalCoverage = company.getListCoverage().stream()
                .mapToDouble(Coverage::getCoverageAmount)
                .sum();

        // Format the total coverage amount as Rupiah
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        String formattedTotal = currencyFormat.format(totalCoverage);

        // Return the formatted string with "Rp" instead of default symbol
        return formattedTotal.replace("Rp", "Rp ").replace(",00", ",00");
    }

    @Override
    public String formatCoverageNames(List<Coverage> coverages) {
        if (coverages == null || coverages.isEmpty()) {
            return "";
        }
        
        // Extract coverage names from the list of Coverage objects
        List<String> coverageNames = coverages.stream()
                .map(Coverage::getName) // Assuming getName() method exists in Coverage class
                .collect(Collectors.toList());

        // Join the list of coverage names into a single string
        String joinedNames = String.join(", ", coverageNames);
        
        return joinedNames;
    }

    @Override
    public Company getCompanyById(UUID id) {
        for (Company company: getAllCompany()) {
            if (company.getId().equals(id)) {
                return company;
            }
        }
        return null;
    }

    @Override
    public Company updateCompany(Company company) {
        Company getCompany = getCompanyById(company.getId());
        if (getCompany != null) {
            getCompany.setName(company.getName());
            getCompany.setContact(company.getContact());
            getCompany.setEmail(company.getEmail());
            getCompany.setAddress(company.getAddress());
            getCompany.setListCoverage(company.getListCoverage());
            getCompany.setUpdatedAt(new Date());

            companyDb.save(getCompany);
            return getCompany;
        }

        return null;
    }

    @Override
    public void deleteCompany(Company company){
        company.setDeletedAt(new Date());
        companyDb.save(company);
    }




}
