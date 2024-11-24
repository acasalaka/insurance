package apap.tk.insurance2206823682.restservice;

import java.util.List;

import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
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
}
