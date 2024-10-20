package apap.ti.insurance2206823682.restdto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatisticsPolicyResponseDTO {
    
    private List<Integer> policyStats;
    private List<String> labels;

}