package apap.tk.insurance2206823682.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import apap.tk.insurance2206823682.model.Policy;
import apap.tk.insurance2206823682.repository.PolicyDb;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PolicyServiceImpl implements PolicyService {

    @Autowired
    PolicyDb policyDb;

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
        List<Policy> result;
        if (status != null && minCoverage != null && maxCoverage != null) {
            result = policyDb.findByStatusAndTotalCoverageBetween(status, minCoverage, maxCoverage);
        } else if (status != null) {
            result = policyDb.findByStatus(status);
        } else if (minCoverage != null && maxCoverage != null) {
            result = policyDb.findByTotalCoverageBetween(minCoverage, maxCoverage);
        } else {
            result = policyDb.findAll();
        }
        return updateStatusListPolicies(result);
    }

    @Override
    public List<Policy> updateStatusListPolicies(List<Policy> listPolicies){
        List<Policy> result = new ArrayList<>();
        for (Policy policy : listPolicies){
            policy = updateStatusPolicy(policy);
            result.add(policy);
        }
        return result;
    }

    @Override
    public Policy getPolicyById(String id) {
        for (Policy policy : policyDb.findAll()) {
            if (policy.getId().equals(id)) {
                return updateStatusPolicy(policy);
            }
        }
        return null;
    }
    

    @Override
    public Policy updatePolicy(Policy policy) {
        Policy getPolicy = getPolicyById(policy.getId());
        if (getPolicy != null) {
            getPolicy.setExpiryDate(policy.getExpiryDate());
            Date today = new Date();
            if (getPolicy.getExpiryDate().before(today) && getPolicy.getStatus() != 3 && getPolicy.getStatus() != 2) {
                getPolicy.setStatus(3); // 3 is the status for "Expired"
            }
            getPolicy.setUpdatedAt(today);

            policyDb.save(getPolicy);
            return getPolicy;
        }
        return null;
    }

    @Override
    @PostConstruct
    public void initialCheckForExpiredPolicies() {
        checkAndUpdateExpiredPolicies();
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public void checkAndUpdateExpiredPolicies() {
        List<Policy> allPolicies = policyDb.findAll();
        Date today = new Date();

        for (Policy policy : allPolicies) {
            if (policy.getExpiryDate().before(today) && policy.getStatus() != 3 && policy.getStatus() != 2) {
                policy.setStatus(3); // 3 is the status for "Expired"
                policyDb.save(policy);
            }
        }
    }

    @Override
    public Policy updateStatusPolicy(Policy policy){
        if (policy.getTotalCovered() > 0 && policy.getTotalCovered() != policy.getTotalCoverage()){
            policy.setStatus(1);
            policyDb.save(policy);
        } else if (policy.getTotalCovered() == policy.getTotalCoverage()){
            policy.setStatus(2);
            policyDb.save(policy);
        }
        return policy;
    }

    @Override
    public void deletePolicy(Policy policy){
        policy.setStatus(4);
        policyDb.save(policy);
    }

    @Override
    public List<Policy> getAllPolicy(){
        return policyDb.findAll();
    }
}
