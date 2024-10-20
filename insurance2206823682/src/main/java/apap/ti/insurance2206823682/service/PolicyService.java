package apap.ti.insurance2206823682.service;

import java.util.List;

import apap.ti.insurance2206823682.model.Policy;

public interface PolicyService {
    String createId(String name, String companyName);
    Policy addPolicy(Policy policy);
    List<Policy> getFilteredPolicies(Integer status, Long minCoverage, Long maxCoverage);
    Policy getPolicyById(String id);
    Policy updatePolicy(Policy policy);
    void initialCheckForExpiredPolicies();
    void checkAndUpdateExpiredPolicies();
    Policy updateStatusPolicy(Policy policy);
    List<Policy> updateStatusListPolicies(List<Policy> listPolicies);
    void deletePolicy(Policy policy);
    List<Policy> getAllPolicy();
}
