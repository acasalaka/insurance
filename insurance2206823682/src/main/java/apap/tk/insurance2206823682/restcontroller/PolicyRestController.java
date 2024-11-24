package apap.tk.insurance2206823682.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.tk.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.restservice.PolicyRestService;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/policy")
public class PolicyRestController {

    @Autowired
    private PolicyRestService policyRestService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllPolicy() {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        List<PolicyResponseDTO> listPolicy = policyRestService.getAllPolicy();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/list-policy")
    public ResponseEntity<?> getListPolicyByStatus(@RequestParam("status") String status){
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        int statusInt;
        
        if (status.equalsIgnoreCase("Created")) {
            statusInt = 0;
        } else if (status.equalsIgnoreCase("Partially Claimed")) {
            statusInt = 1;
        } else if (status.equalsIgnoreCase("Fully Claimed")) {
            statusInt = 2;
        } else if (status.equalsIgnoreCase("Expired")) {
            statusInt = 3;
        } else {
            statusInt = 4; // Default to "Cancelled" or any other case
        }

        List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByStatusAdmin(statusInt);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("List policy dengan status %s berhasil ditemukan", status));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/list-policy-by-range")
    public ResponseEntity<?> getListPolicyByRange(@RequestParam("min") String min, @RequestParam("max") String max){
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        long minTotalCoverage = 0;
        long maxTotalCoverage = 0;

        try {
            minTotalCoverage = Long.parseLong(min);
            maxTotalCoverage = Long.parseLong(max);
        } catch (NumberFormatException e){
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
        }

        List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRange(minTotalCoverage, maxTotalCoverage);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("List policy dengan total coverage pada rentang %s dan %s berhasil ditemukan", min, max));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getPolicyById(@RequestParam("id") String id){
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.getPolicyById(id);

        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Policy dengan ID %s berhasil ditemukan", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-expirydate")
    public ResponseEntity<?> updateExpiryDatePolicy(@Valid @RequestBody UpdatePolicyExpiryDateRequestRestDTO policyDTO){
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.updatePolicyExpiryDate(policyDTO);

        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Expiry Date Policy dengan ID %s berhasil diperbarui", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatusPolicy(){
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        List<PolicyResponseDTO> listPolicy = policyRestService.updateAllStatusPolicy();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("Status dari semua policy berhasil diperbarui"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelPolicy(@RequestParam("id") String id){
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        PolicyResponseDTO policy = policyRestService.cancelStatusPolicy(id);

        if (policy == null){
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } else if (!policy.getStatus().equalsIgnoreCase("Created") || policy.isDeleted() == true){
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak dapat dicancel"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } else {
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Data policy dengan id %s berhasil dibatalkan", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePolicy(@RequestParam("id") String id){
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.deletePolicy(id);

        if (policy == null){
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } else if (!policy.getStatus().equalsIgnoreCase("Created")){
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak dapat dihapus"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } else {
            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setMessage(String.format("Data policy dengan id %s berhasil dihapus", policy.getId()));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }
    }
    
    @GetMapping("/policy-list-by-treatment")
    public ResponseEntity<?> getPolicyListByTreatment(@Valid @RequestBody UpdatePolicyExpiryDateRequestRestDTO policyDTO){
        
        return null;

    }
}
