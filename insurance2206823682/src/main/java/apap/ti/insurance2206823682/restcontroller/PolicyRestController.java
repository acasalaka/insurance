package apap.ti.insurance2206823682.restcontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.ti.insurance2206823682.restdto.request.AddPolicyRequestRestDTO;
import apap.ti.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.ti.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.ti.insurance2206823682.restservice.PolicyRestService;
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

    @PostMapping("/stat")
    public ResponseEntity<?> getPolicyStat(@RequestParam("period") String period, @RequestParam("year") int year) {
        List<Integer> stats;

        // Check the period value and generate statistics accordingly
        if (period.equalsIgnoreCase("Monthly")) {
            stats = policyRestService.getMonthlyPolicyStats(year);
        } else if (period.equalsIgnoreCase("Quarterly")) {
            stats = policyRestService.getQuarterlyPolicyStats(year);
        } else {
            return ResponseEntity.badRequest().body("Invalid period specified");
        }

        Map<String, List<?>> response = new HashMap<>();
        response.put("stats", stats);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getDetailPolicy(@RequestParam("id") String id){
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

    @PostMapping("/create")
    public ResponseEntity<?> addProyek(@RequestParam("nik") String nik, @Valid @RequestBody AddPolicyRequestRestDTO policyDTO, BindingResult bindingResult) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        if (bindingResult.hasFieldErrors()) {
            String errorMessages = "";
            List<FieldError> errors = bindingResult.getFieldErrors();
            for (FieldError error : errors) {
                errorMessages += error.getDefaultMessage() + "; ";
            }

            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(errorMessages);
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }


        PolicyResponseDTO policy = policyRestService.addPolicy(policyDTO, nik);
        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak dapat dibuat"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        }

        baseResponseDTO.setStatus(HttpStatus.CREATED.value());
        baseResponseDTO.setData(policy);
        baseResponseDTO.setMessage(String.format("Policy dengan ID %s berhasil ditambahkan", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.CREATED);
    }
}
