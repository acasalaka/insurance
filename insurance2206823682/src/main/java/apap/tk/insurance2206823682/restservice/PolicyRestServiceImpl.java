package apap.tk.insurance2206823682.restservice;

import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.model.Policy;
import apap.tk.insurance2206823682.model.UsedCoverageOfPolicy;
import apap.tk.insurance2206823682.repository.PolicyDb;
import apap.tk.insurance2206823682.repository.UsedCoverageOfPolicyDb;
import apap.tk.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.UserResponseDTO;
import apap.tk.insurance2206823682.service.CompanyService;
import apap.tk.insurance2206823682.service.PolicyService;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.TemporalType;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PolicyRestServiceImpl implements PolicyRestService {

    @Autowired
    private PolicyDb policyDb;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Override
    public List<PolicyResponseDTO> getAllPolicy() {
        List<Policy> policyList = policyDb.findAllByIsDeletedFalse();
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyResponseDTO createPolicy(UUID companyId, UUID patientId, Date expiryDate) {
        // Construct the URL for the API
        String url = String.format("http://localhost:8084/api/user/detail/id/%s", patientId.toString()); // API endpoint

        // Create a WebClient instance
        WebClient webClient = webClientBuilder.baseUrl(url).build();

        // Fetch user details asynchronously using WebClient
        UserResponseDTO userResponse = null;

        try {
            // Use ParameterizedTypeReference to handle the generic type correctly
            BaseResponseDTO<UserResponseDTO> apiResponse = webClient.get()
                    .uri("") // Empty because the base URL already includes the patient ID
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<BaseResponseDTO<UserResponseDTO>>() {
                    })
                    .block(); // Block here to make it synchronous (use in non-blocking context if needed)

            // Check if the response or data is null
            if (apiResponse == null || apiResponse.getData() == null) {
                System.out.println("Error: No user data returned for patientId: " + patientId);
                return null; // Handle the error gracefully by returning null
            }

            userResponse = apiResponse.getData(); // Extract user data

            // Log the user details to ensure data is loaded
            System.out.println("User PClass: " + userResponse.getPClass());
            System.out.println("User Name: " + userResponse.getName());

            // Determine insurance limit based on user class (pClass)
            long insuranceLimit = 0;
            if (userResponse.getPClass() == 1) {
                insuranceLimit = 50000000;
            } else if (userResponse.getPClass() == 2) {
                insuranceLimit = 35000000;
            } else if (userResponse.getPClass() == 3) {
                insuranceLimit = 25000000;
            }

            // Compare the insurance limit with the company's coverage
            if (insuranceLimit < companyService.getTotalCoverage(companyId)) {
                System.out.println("Error: Insurance limit is lower than company coverage.");
                return null; // Return null if insurance limit is lower than the company's coverage
            }

            // Create and set up the new policy object
            Policy newPolicy = new Policy();
            newPolicy.setId(
                    policyService.createId(userResponse.getName(), companyService.getCompanyById(companyId).getName()));
            newPolicy.setCompanyId(companyId);
            newPolicy.setPatientId(patientId);
            newPolicy.setStatus(0);
            newPolicy.setExpiryDate(expiryDate);
            newPolicy.setTotalCoverage(companyService.getTotalCoverage(companyId));
            newPolicy.setTotalCovered(0);
            newPolicy.setListCoverage(companyService.getCompanyById(companyId).getListCoverageCompany());
            newPolicy.setCreatedAt(new Date());
            newPolicy.setUpdatedAt(new Date());
            newPolicy.setCreatedBy(userResponse.getName());
            newPolicy.setUpdatedBy(userResponse.getName());
            newPolicy.setDeleted(false);

            // Save the policy to the database
            policyDb.save(newPolicy);
            System.out.println("Policy created successfully with ID: " + newPolicy.getId());

            // Return the converted PolicyResponseDTO
            return convertToPolicyResponseDTO(newPolicy);

        } catch (Exception e) {
            // Log any exception that occurred during the process
            System.out.println("Error while creating policy: " + e.getMessage());
            e.printStackTrace();
            return null; // Return null or handle as appropriate
        }
    }

    @Override
    public List<PolicyResponseDTO> getAllPolicyPatient(UUID id) {
        List<Policy> policyList = policyDb.findByPatientIdAndIsDeletedFalse(id);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByStatusAdmin(int status) {
        List<Policy> policyList = policyDb.findByStatusAndIsDeletedFalse(status);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByStatusPatient(int status, UUID id) {
        List<Policy> policyList = policyDb.findByPatientIdAndStatusAndIsDeletedFalse(id, status);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByRange(long min, long max) {
        List<Policy> policyList = policyDb.findByTotalCoverageBetweenAndIsDeletedFalse(min, max);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByRangeAndStatus(long min, long max, int status) {
        List<Policy> policyList = policyDb.findByStatusAndTotalCoverageBetweenAndIsDeletedFalse(status, min, max);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByRangePatient(long min, long max, UUID id) {
        List<Policy> policyList = policyDb.findByPatientIdAndTotalCoverageBetweenAndIsDeletedFalse(id, min, max);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PolicyResponseDTO> getPolicyListByRangeAndStatusPatient(long min, long max, UUID id, int status) {
        List<Policy> policyList = policyDb.findByPatientIdAndStatusAndTotalCoverageBetweenAndIsDeletedFalse(id, status,
                min, max);
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyResponseDTO getPolicyById(String id) {
        Policy policy = policyDb.findById(id);

        if (policy == null) {
            return null;
        }

        return convertToPolicyResponseDTO(policy);
    }

    @Override
    public PolicyResponseDTO getPolicyByIdAndIdPatient(String id, String idPatient) {
        Policy policy = policyDb.findByIdAndPatientId(id, UUID.fromString(idPatient));

        if (policy == null) {
            return null;
        }

        return convertToPolicyResponseDTO(policy);
    }

    @Override
    public PolicyResponseDTO updatePolicyExpiryDate(UpdatePolicyExpiryDateRequestRestDTO policyDTO) {
        Policy policy = policyDb.findById(policyDTO.getId());

        if (policy == null) {
            return null;
        }

        if (policy.isDeleted() == true) {
            return null;
        }

        policy.setExpiryDate(policyDTO.getExpiryDate());
        policy.setUpdatedAt(new Date());

        // Get today's date
        // Date today = new Date();

        // if (policy.getStatus() != 3) {
        // if (policyDTO.getExpiryDate().before(today)) {
        // policy.setStatus(3);
        // } else {
        // if (policy.getTotalCovered() == 0) {
        // policy.setStatus(0); // Created
        // } else if (policy.getTotalCovered() > 0 && policy.getTotalCovered() <
        // policy.getTotalCoverage()) {
        // policy.setStatus(1); // Partially Claimed
        // } else if (policy.getTotalCovered() == policy.getTotalCoverage()) {
        // policy.setStatus(2);
        // }
        // }
        // }
        updateAllStatusPolicy();

        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public List<PolicyResponseDTO> updateAllStatusPolicy() {
        List<Policy> policyList = policyDb.findAllByIsDeletedFalse();

        Date today = new Date();
        for (Policy policy : policyList) {
            if (policy.getExpiryDate().before(today)) {
                policy.setStatus(3);
            } else if (policy.getTotalCovered() == 0) {
                policy.setStatus(0); // Created
            } else if (policy.getTotalCovered() > 0 && policy.getTotalCovered() < policy.getTotalCoverage()) {
                policy.setStatus(1); // Partially Claimed
            } else if (policy.getTotalCovered() == policy.getTotalCoverage()) {
                policy.setStatus(2);
            }

        }

        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PolicyResponseDTO cancelStatusPolicy(String id) {
        Policy policy = policyDb.findById(id);

        if (policy == null) {
            return null;
        }

        if (policy.getStatus() != 0 || policy.isDeleted() == true) {
            return convertToPolicyResponseDTO(policy);
        }

        policy.setStatus(4);
        policy.setUpdatedAt(new Date());
        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    public PolicyResponseDTO deletePolicy(String id) {
        Policy policy = policyDb.findById(id);

        if (policy == null) {
            return null;
        }

        if (policy.getStatus() != 0) {
            return convertToPolicyResponseDTO(policy);
        }

        policy.setDeleted(true);
        policy.setUpdatedAt(new Date());
        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    public List<PolicyResponseDTO> getPoliciesByTreatments(List<Long> idTreatments) {

        List<Policy> policies = policyDb.findAll();
        List<Policy> fixedPolicies = new ArrayList<Policy>();

        for (Policy policy : policies) {
            boolean isAdded = false;
            for (Coverage coverage : companyService.getCoverages(policy.getCompanyId())) {
                for (Long id : idTreatments) {
                    if (id == coverage.getId()) {
                        fixedPolicies.add(policy);
                        isAdded = true;
                        break;
                    }
                }
                if (isAdded == true) {
                    break;
                }
            }
        }

        return fixedPolicies.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
    }

    public PolicyResponseDTO convertToPolicyResponseDTO(Policy policy) {
        var policyResponseDTO = new PolicyResponseDTO();
        policyResponseDTO.setId(policy.getId());
        policyResponseDTO.setCompanyId(policy.getCompanyId());
        policyResponseDTO.setCompanyName(companyService.getCompanyById(policy.getCompanyId()).getName());
        policyResponseDTO.setPatientId(policy.getPatientId());

        String status;
        if (policy.getStatus() == 0) {
            status = "Created";
        } else if (policy.getStatus() == 1) {
            status = "Partially Claimed";
        } else if (policy.getStatus() == 2) {
            status = "Fully Claimed";
        } else if (policy.getStatus() == 3) {
            status = "Expired";
        } else {
            status = "Cancelled";
        }

        policyResponseDTO.setStatus(status);
        policyResponseDTO.setExpiryDate(policy.getExpiryDate());
        policyResponseDTO.setTotalCoverage(policy.getTotalCoverage());
        policyResponseDTO.setTotalCovered(policy.getTotalCovered());
        policyResponseDTO.setCreatedAt(policy.getCreatedAt());
        policyResponseDTO.setUpdatedAt(policy.getUpdatedAt());
        policyResponseDTO.setCreatedBy(policy.getCreatedBy());
        policyResponseDTO.setUpdatedBy(policy.getUpdatedBy());
        policyResponseDTO.setDeleted(policy.isDeleted());

        return policyResponseDTO;
    }

    @Override
    public List<CoverageResponseDTO> getUsedCoverages(String id) {
        List<Coverage> coverages = companyService.getUsedCoverages(id);

        return coverages.stream()
                .map(this::convertToCoverageResponseDTO)
                .collect(Collectors.toList());

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
