package apap.ti.insurance2206823682.restservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;
import apap.ti.insurance2206823682.repository.PolicyDb;
import apap.ti.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.ti.insurance2206823682.restdto.response.PatientResponseDTO;
import apap.ti.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.ti.insurance2206823682.service.CompanyService;
import apap.ti.insurance2206823682.service.PatientService;
import apap.ti.insurance2206823682.service.PolicyService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PolicyRestServiceImpl implements PolicyRestService {

    @Autowired
    private PolicyDb policyDb;

    @Autowired
    private PatientRestService patientRestService;

    @Autowired
    private CompanyRestService companyRestService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PatientService patientService;

    @Override
    public List<Integer> getMonthlyPolicyStats(int year) {
        // Initialize a list to hold the count of policies for each month (12 months)
        List<Integer> stats = new ArrayList<>(Collections.nCopies(12, 0));

        // Fetch all policies
        List<Policy> policies = policyDb.findAll(); // Assuming findAll() retrieves all policies

        // Use Calendar to extract year and month from java.util.Date
        Calendar calendar = Calendar.getInstance();

        // Iterate over the policies and filter them by year
        for (Policy policy : policies) {
            Date createdAt = policy.getCreatedAt(); // Get the created date
            calendar.setTime(createdAt); // Set the policy's date in the Calendar

            int policyYear = calendar.get(Calendar.YEAR); // Get the year from the policy's createdAt
            if (policyYear == year) {
                int month = calendar.get(Calendar.MONTH); // Get the month (0 = January, 11 = December)
                stats.set(month, stats.get(month) + 1); // Increment count for the corresponding month
            }
        }

        return stats; // Return the list containing counts for each month
    }

    @Override
    public List<Integer> getQuarterlyPolicyStats(int year) {
        // Initialize a list to hold the count of policies for each quarter (4 quarters)
        List<Integer> stats = new ArrayList<>(Collections.nCopies(4, 0));

        // Fetch all policies
        List<Policy> policies = policyDb.findAll(); // Assuming findAll() retrieves all policies

        // Use Calendar to extract year and month from java.util.Date
        Calendar calendar = Calendar.getInstance();

        // Iterate over the policies and filter them by year
        for (Policy policy : policies) {
            Date createdAt = policy.getCreatedAt();
            calendar.setTime(createdAt);

            int policyYear = calendar.get(Calendar.YEAR); // Get year from the policy's createdAt
            if (policyYear == year) {
                int month = calendar.get(Calendar.MONTH); // Get month (0 = January, 11 = December)
                int quarter = month / 3; // Calculate the quarter (0 = Q1, 1 = Q2, 2 = Q3, 3 = Q4)
                stats.set(quarter, stats.get(quarter) + 1); // Increment count for the corresponding quarter
            }
        }

        return stats; // Return the list containing counts for each quarter
    }

    @Override
    public PolicyResponseDTO getPolicyById(String idPolicy) {
        var policy = policyDb.findById(idPolicy).orElse(null);

        if (policy == null) {
            return null;
        }
        

        return policyToPolicyResponseDTO(policy);
    }

    private PolicyResponseDTO policyToPolicyResponseDTO(Policy policy) {
        var policyResponseDTO = new PolicyResponseDTO();
        policyResponseDTO.setId(policy.getId());

        CompanyResponseDTO companyResponseDTO = companyRestService.getCompanyById(policy.getCompany().getId());
        policyResponseDTO.setCompany(companyResponseDTO);

        PatientResponseDTO patientResponseDTO = patientRestService.getPatientById(policy.getPatient().getId());
        policyResponseDTO.setPatient(patientResponseDTO);

        policyResponseDTO.setStatus(policy.getStatus());
        policyResponseDTO.setExpiryDate(policy.getExpiryDate());
        policyResponseDTO.setTotalCoverage(policy.getTotalCoverage());
        policyResponseDTO.setTotalCovered(policy.getTotalCovered());
        policyResponseDTO.setCreatedAt(policy.getCreatedAt());
        policyResponseDTO.setUpdatedAt(policy.getUpdatedAt());

        return policyResponseDTO;
    }

    @Override
    public PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO policyDTO, String nik) {
        Company searchedCompany = companyService.getCompanyById(policyDTO.getCompany());
        Patient searchedPatient = patientService.getPatientByNIK(nik);
        Policy policy = new Policy();
        

        for (Policy policyItem : searchedPatient.getListPolicy()){
            if (policyItem.getPatient().getId().equals(searchedPatient.getId()) && policyItem.getCompany().getId().equals(searchedCompany.getId())){
                return null;
            }
        }
        
        policy.setId(policyService.createId(searchedPatient.getName(), searchedCompany.getName()));
        policy.setCompany(searchedCompany);
        policy.setPatient(searchedPatient);
        policy.setStatus(0);
        policy.setExpiryDate(policyDTO.getExpiryDate());
        policy.setTotalCoverage(companyService.getTotalCoverage(searchedCompany));
        policy.setTotalCovered(0);

        Policy savedPolicy = policyDb.save(policy);
        return policyToPolicyResponseDTO(savedPolicy);
    }



}
