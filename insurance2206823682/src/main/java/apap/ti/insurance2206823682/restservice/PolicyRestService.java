package apap.ti.insurance2206823682.restservice;

import java.util.List;

import apap.ti.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206823682.restdto.response.PolicyResponseDTO;

public interface PolicyRestService {
    List<Integer> getMonthlyPolicyStats(int year);
    List<Integer> getQuarterlyPolicyStats(int year);
    PolicyResponseDTO getPolicyById(String idPolicy);
    PolicyResponseDTO addPolicy(AddPolicyRequestRestDTO policyDTO, String nik);
}
