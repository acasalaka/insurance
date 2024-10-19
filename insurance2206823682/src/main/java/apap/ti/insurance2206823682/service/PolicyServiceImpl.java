package apap.ti.insurance2206823682.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;
import apap.ti.insurance2206823682.repository.CompanyDb;
import apap.ti.insurance2206823682.repository.PatientDb;
import apap.ti.insurance2206823682.repository.PolicyDb;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    private PolicyDb policyDb;

    @Override
    public String createId(String name, String companyName) {
        // Split the name to check if it contains multiple parts
        String[] nameParts = name.split(" ");
        String firstInitial = "";
        String lastInitial = "";

        if (nameParts.length >= 2) {
            // If there are multiple parts, take the first character of the first and last
            // part
            firstInitial = String.valueOf(nameParts[0].charAt(0)).toUpperCase(); // First name initial
            lastInitial = String.valueOf(nameParts[nameParts.length - 1].charAt(0)).toUpperCase(); // Last name initial
                                                                                                   // (from the last
            // word)
        } else {
            // If there's only one part of the name, take the first two characters of the
            // first name
            firstInitial = nameParts[0].substring(0, Math.min(2, nameParts[0].length())).toUpperCase();
        }

        // Take the first 3 characters from the company name (or as much as available)
        String companyInitials = companyName.substring(0, Math.min(3, companyName.length())).toUpperCase();

        // Get the total number of policies in the system
        long totalPolicies = policyDb.count(); // Hypothetical method to count total policies

        // Add the total policies count + 1, padded to 4 digits
        String policyCountStr = String.format("%04d", totalPolicies + 1);

        // Use String.format to build the ID
        return String.format("POL%s%s%s%s", firstInitial, lastInitial, companyInitials, policyCountStr);
    }

    // POL+FN(1)LN(1)ORFN(2)JUMLAHPOLIS

    @Override
    public Policy addPolicy(Policy policy) {
        return policyDb.save(policy);
    }

    @Override
    public List<Policy> getFilteredPolicies(Integer status, Long minCoverage, Long maxCoverage) {
        // Jika status, minCoverage, dan maxCoverage semuanya diisi
        if (status != null && minCoverage != null && maxCoverage != null) {
            return policyDb.findByStatusAndTotalCoverageBetween(status, minCoverage, maxCoverage);
        } else if (status != null) {
            return policyDb.findByStatus(status);
        } else if (minCoverage != null && maxCoverage != null) {
            return policyDb.findByTotalCoverageBetween(minCoverage, maxCoverage);
        } else {
            return policyDb.findAll();
        }
    }

    @Override
    public Policy getPolicyById(String id){
        for (Policy policy: policyDb.findAll()) {
            if (policy.getId().equals(id)) {
                return policy;
            }
        }
        return null;



    }
}
