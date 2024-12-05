package apap.tk.insurance2206823682.restservice;

import java.util.List;
import java.util.UUID;

import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;

public interface PolicyRestService {
    List<PolicyResponseDTO> getAllPolicy();
    List<PolicyResponseDTO> getPolicyListByStatusAdmin(int status);
    List<PolicyResponseDTO> getPolicyListByRange(long min, long max);
    PolicyResponseDTO getPolicyById(String id);
    PolicyResponseDTO updatePolicyExpiryDate(UpdatePolicyExpiryDateRequestRestDTO policyDTO);
    List<PolicyResponseDTO> updateAllStatusPolicy();
    PolicyResponseDTO cancelStatusPolicy(String id);
    PolicyResponseDTO deletePolicy(String id);
    List<PolicyResponseDTO> getAllPolicyPatient(UUID id);
    List<PolicyResponseDTO> getPolicyListByStatusPatient(int status, UUID id);
    List<PolicyResponseDTO> getPolicyListByRangePatient(long min, long max, UUID id);
    PolicyResponseDTO getPolicyByIdAndIdPatient(String id, String idPatient);
    List<PolicyResponseDTO> getPoliciesByTreatments(List<Long> idTreatments);
    List<PolicyResponseDTO> getPolicyListByRangeAndStatus(long min, long max, int status);
    List<PolicyResponseDTO> getPolicyListByRangeAndStatusPatient(long min, long max, UUID id, int status);
    List<CoverageResponseDTO> getUsedCoverages(String id);

}

