package apap.tk.insurance2206823682.restdto.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ListPolicyWithListTreatmentRequestRestDTO {
 
    private List<Long> idsTreatments;
}
