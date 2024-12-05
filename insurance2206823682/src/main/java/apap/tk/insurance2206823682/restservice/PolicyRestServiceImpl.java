package apap.tk.insurance2206823682.restservice;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.model.Policy;
import apap.tk.insurance2206823682.model.UsedCoverageOfPolicy;
import apap.tk.insurance2206823682.repository.PolicyDb;
import apap.tk.insurance2206823682.repository.UsedCoverageOfPolicyDb;
import apap.tk.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
import apap.tk.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.service.CompanyService;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PolicyRestServiceImpl implements PolicyRestService {

    @Autowired
    private PolicyDb policyDb;

    @Autowired
    private CompanyService companyService;

    @Override
    public List<PolicyResponseDTO> getAllPolicy() {
        List<Policy> policyList = policyDb.findAllByIsDeletedFalse();
        return policyList.stream()
                .map(this::convertToPolicyResponseDTO)
                .collect(Collectors.toList());
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
        List<Policy> policyList = policyDb.findByStatusAndTotalCoverageBetweenAndIsDeletedFalse(status ,min, max);
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
        List<Policy> policyList = policyDb.findByPatientIdAndStatusAndTotalCoverageBetweenAndIsDeletedFalse(id, status, min, max);
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

        if (policy.isDeleted() == true){
            return null;
        }

        policy.setExpiryDate(policyDTO.getExpiryDate());
        policy.setUpdatedAt(new Date());

        // Get today's date
        // Date today = new Date();

        // if (policy.getStatus() != 3) {
        //     if (policyDTO.getExpiryDate().before(today)) {
        //         policy.setStatus(3);
        //     } else {
        //         if (policy.getTotalCovered() == 0) {
        //             policy.setStatus(0); // Created
        //         } else if (policy.getTotalCovered() > 0 && policy.getTotalCovered() < policy.getTotalCoverage()) {
        //             policy.setStatus(1); // Partially Claimed
        //         } else if (policy.getTotalCovered() == policy.getTotalCoverage()) {
        //             policy.setStatus(2);
        //         }
        //     }
        // }
        updateAllStatusPolicy();

        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    @Scheduled(cron = "0 0 0 * * ?") // Runs every day at midnight
    public List<PolicyResponseDTO> updateAllStatusPolicy(){
        List<Policy> policyList = policyDb.findAllByIsDeletedFalse();

        Date today = new Date();
        for (Policy policy : policyList){
            if (policy.getExpiryDate().before(today)) {
                policy.setStatus(3);
            }
            else if (policy.getTotalCovered() == 0) {
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
    public PolicyResponseDTO cancelStatusPolicy(String id){
        Policy policy = policyDb.findById(id);
        
        if (policy == null){
            return null;
        }

        if (policy.getStatus() != 0 || policy.isDeleted() == true){
            return convertToPolicyResponseDTO(policy);
        }

        policy.setStatus(4);
        policy.setUpdatedAt(new Date());
        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    public PolicyResponseDTO deletePolicy(String id){
        Policy policy = policyDb.findById(id);
        
        if (policy == null){
            return null;
        }

        if (policy.getStatus() != 0){
            return convertToPolicyResponseDTO(policy);
        }

        policy.setDeleted(true);
        policy.setUpdatedAt(new Date());
        var updatedPolicy = policyDb.save(policy);
        return convertToPolicyResponseDTO(updatedPolicy);
    }

    @Override
    public List<PolicyResponseDTO> getPoliciesByTreatments(List<Long> idTreatments){
    
        List<Policy> policies = policyDb.findAll();
        List<Policy> fixedPolicies = new ArrayList<Policy>();

        for (Policy policy : policies){
            boolean isAdded = false;
            for (Coverage coverage :  companyService.getCoverages(policy.getCompanyId())){
                for (Long id : idTreatments){
                    if (id == coverage.getId()){
                        fixedPolicies.add(policy);
                        isAdded = true;
                        break;
                    }
                }
                if (isAdded == true){
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
    public List<CoverageResponseDTO> getUsedCoverages(String id){
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
