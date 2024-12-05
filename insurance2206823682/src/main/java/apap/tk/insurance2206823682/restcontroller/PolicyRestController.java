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
import apap.tk.insurance2206823682.restdto.request.ListPolicyWithListTreatmentRequestRestDTO;
import apap.tk.insurance2206823682.restdto.request.UpdatePolicyExpiryDateRequestRestDTO;
import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.UserResponseDTO;
import apap.tk.insurance2206823682.restservice.PolicyRestService;
import jakarta.validation.Valid;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/policy")
public class PolicyRestController {

    @Autowired
    private PolicyRestService policyRestService;

    @PostMapping("/create")
    public ResponseEntity<?> createPolicy(@RequestBody AddPolicyRequestRestDTO policyDTO) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        try {
            PolicyResponseDTO newPolicy = policyRestService.createPolicy(policyDTO.getCompanyId(), policyDTO.getPatientId(), policyDTO.getExpiryDate());

            if (newPolicy == null){
                throw new Exception("Policy tidak dapat dibuat. Patient class tidak cukup");
            }

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(newPolicy);
            baseResponseDTO.setMessage("Berhasil membuat Policy baru");
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        } catch (Exception e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllPolicy(
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "min", required = false) String min,
            @RequestParam(value = "max", required = false) String max) {

        if (status == null && (min == null || min.equals("")) && (max == null || max.equals(""))) {
            var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
            List<PolicyResponseDTO> listPolicy = policyRestService.getAllPolicy();

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        if ((min == null || min.equals("")) && (max == null || max.equals(""))) {
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
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        if (status == null && !min.equals("") && !min.equals("")) {
            var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

            long minTotalCoverage = 0;
            long maxTotalCoverage = 0;

            try {
                minTotalCoverage = Long.parseLong(min);
                maxTotalCoverage = Long.parseLong(max);
            } catch (NumberFormatException e) {
                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage(e.getMessage());
                baseResponseDTO.setTimestamp(new Date());
            }

            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRange(minTotalCoverage,
                    maxTotalCoverage);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        else {
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

            long minTotalCoverage = 0;
            long maxTotalCoverage = 0;

            try {
                minTotalCoverage = Long.parseLong(min);
                maxTotalCoverage = Long.parseLong(max);
            } catch (NumberFormatException e) {
                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage(e.getMessage());
                baseResponseDTO.setTimestamp(new Date());
            }

            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRangeAndStatus(minTotalCoverage,
                    maxTotalCoverage, statusInt);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

    }

    @GetMapping("/patient/all")
    public ResponseEntity<?> getAllPolicyPatient(@RequestParam("id") String id,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "min", required = false) String min,
            @RequestParam(value = "max", required = false) String max) {

        if (status == null && (min == null || min.equals("")) && (max == null || max.equals(""))) {
            var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
            List<PolicyResponseDTO> listPolicy = policyRestService.getAllPolicyPatient(UUID.fromString(id));

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        if ((min == null || min.equals("")) && (max == null || max.equals(""))) {
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

            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByStatusPatient(statusInt,
                    UUID.fromString(id));

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        if (status == null && !min.equals("") && !min.equals("")) {
            var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

            long minTotalCoverage = 0;
            long maxTotalCoverage = 0;

            try {
                minTotalCoverage = Long.parseLong(min);
                maxTotalCoverage = Long.parseLong(max);
            } catch (NumberFormatException e) {
                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage(e.getMessage());
                baseResponseDTO.setTimestamp(new Date());
            }

            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRangePatient(minTotalCoverage,
                    maxTotalCoverage, UUID.fromString(id));

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(
                    String.format("List policy dengan total coverage pada rentang %s dan %s berhasil ditemukan", min,
                            max));
            baseResponseDTO.setTimestamp(new Date());

            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }

        else {
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

            long minTotalCoverage = 0;
            long maxTotalCoverage = 0;

            try {
                minTotalCoverage = Long.parseLong(min);
                maxTotalCoverage = Long.parseLong(max);
            } catch (NumberFormatException e) {
                baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
                baseResponseDTO.setMessage(e.getMessage());
                baseResponseDTO.setTimestamp(new Date());
            }

            List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRangeAndStatusPatient(
                    minTotalCoverage,
                    maxTotalCoverage, UUID.fromString(id), statusInt);

            baseResponseDTO.setStatus(HttpStatus.OK.value());
            baseResponseDTO.setData(listPolicy);
            baseResponseDTO.setMessage(String.format("List policy berhasil ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
        }
    }

    @GetMapping("/admin/list-policy")
    public ResponseEntity<?> getListPolicyByStatus(@RequestParam("status") String status) {
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

    @GetMapping("/patient/list-policy")
    public ResponseEntity<?> getListPolicyByStatusPatient(@RequestParam("status") String status,
            @RequestParam("id") String id) {
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

        List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByStatusPatient(statusInt,
                UUID.fromString(id));

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("List policy dengan status %s berhasil ditemukan", status));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/admin/list-policy-by-range")
    public ResponseEntity<?> getListPolicyByRange(@RequestParam("min") String min, @RequestParam("max") String max) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        long minTotalCoverage = 0;
        long maxTotalCoverage = 0;

        try {
            minTotalCoverage = Long.parseLong(min);
            maxTotalCoverage = Long.parseLong(max);
        } catch (NumberFormatException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
        }

        List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRange(minTotalCoverage, maxTotalCoverage);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(
                String.format("List policy dengan total coverage pada rentang %s dan %s berhasil ditemukan", min, max));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/patient/list-policy-by-range")
    public ResponseEntity<?> getListPolicyByRange(@RequestParam("min") String min, @RequestParam("max") String max,
            @RequestParam("id") UUID id) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        long minTotalCoverage = 0;
        long maxTotalCoverage = 0;

        try {
            minTotalCoverage = Long.parseLong(min);
            maxTotalCoverage = Long.parseLong(max);
        } catch (NumberFormatException e) {
            baseResponseDTO.setStatus(HttpStatus.BAD_REQUEST.value());
            baseResponseDTO.setMessage(e.getMessage());
            baseResponseDTO.setTimestamp(new Date());
        }

        List<PolicyResponseDTO> listPolicy = policyRestService.getPolicyListByRangePatient(minTotalCoverage,
                maxTotalCoverage, id);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(
                String.format("List policy dengan total coverage pada rentang %s dan %s berhasil ditemukan", min, max));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<?> getPolicyById(@RequestParam("id") String id) {
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

    @GetMapping("/patient/detail")
    public ResponseEntity<?> getPolicyById(@RequestParam("id") String id, @RequestParam("id") String idPatient) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.getPolicyByIdAndIdPatient(id, idPatient);

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
    public ResponseEntity<?> updateExpiryDatePolicy(
            @Valid @RequestBody UpdatePolicyExpiryDateRequestRestDTO policyDTO) {
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
        baseResponseDTO
                .setMessage(String.format("Expiry Date Policy dengan ID %s berhasil diperbarui", policy.getId()));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/update-status")
    public ResponseEntity<?> updateStatusPolicy() {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        List<PolicyResponseDTO> listPolicy = policyRestService.updateAllStatusPolicy();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listPolicy);
        baseResponseDTO.setMessage(String.format("Status dari semua policy berhasil diperbarui"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @PutMapping("/cancel")
    public ResponseEntity<?> cancelPolicy(@RequestParam("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();
        PolicyResponseDTO policy = policyRestService.cancelStatusPolicy(id);

        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } else if (!policy.getStatus().equalsIgnoreCase("Created") || policy.isDeleted() == true) {
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
    public ResponseEntity<?> deletePolicy(@RequestParam("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<PolicyResponseDTO>();

        PolicyResponseDTO policy = policyRestService.deletePolicy(id);

        if (policy == null) {
            baseResponseDTO.setStatus(HttpStatus.NOT_FOUND.value());
            baseResponseDTO.setMessage(String.format("Data policy tidak ditemukan"));
            baseResponseDTO.setTimestamp(new Date());
            return new ResponseEntity<>(baseResponseDTO, HttpStatus.NOT_FOUND);
        } else if (!policy.getStatus().equalsIgnoreCase("Created")) {
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

    @PostMapping("/policy-list-by-treatment")
    public ResponseEntity<?> getPolicyListByTreatment(
            @RequestBody ListPolicyWithListTreatmentRequestRestDTO policyDTO) {
        var baseResponseDTO = new BaseResponseDTO<List<PolicyResponseDTO>>();

        List<PolicyResponseDTO> listPolicies = policyRestService.getPoliciesByTreatments(policyDTO.getIdsTreatments());
        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setMessage("Data policies berhasil dicari");
        baseResponseDTO.setTimestamp(new Date());
        baseResponseDTO.setData(listPolicies);
        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/get_used_coverages")
    public ResponseEntity<?> getListUsedCoverage(@RequestParam("policyId") String policyId) {
        var baseResponseDTO = new BaseResponseDTO<List<CoverageResponseDTO>>();

        List<CoverageResponseDTO> listCoverages = policyRestService.getUsedCoverages(policyId);

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCoverages);
        baseResponseDTO.setMessage(String.format("List used coverage dari policy berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}
