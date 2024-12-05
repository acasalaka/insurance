package apap.tk.insurance2206823682.restcontroller;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CompanyResponseDTO;
import apap.tk.insurance2206823682.restdto.response.CoverageResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import apap.tk.insurance2206823682.restservice.CompanyRestService;

@RestController
@RequestMapping("/api/company")
public class CompanyRestController {
    
    @Autowired
    private CompanyRestService companyRestService;

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        var baseResponseDTO = new BaseResponseDTO<List<CompanyResponseDTO>>();

        List<CompanyResponseDTO> listCoverages = companyRestService.getAllCompany();

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCoverages);
        baseResponseDTO.setMessage(String.format("List company berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

    @GetMapping("/get_coverages")
    public ResponseEntity<?> getListCoverages(@RequestParam("id") String id) {
        var baseResponseDTO = new BaseResponseDTO<List<CoverageResponseDTO>>();

        List<CoverageResponseDTO> listCoverages = companyRestService.getCoveragesByIdCompany(UUID.fromString(id));

        baseResponseDTO.setStatus(HttpStatus.OK.value());
        baseResponseDTO.setData(listCoverages);
        baseResponseDTO.setMessage(String.format("List coverage dari company berhasil ditemukan"));
        baseResponseDTO.setTimestamp(new Date());

        return new ResponseEntity<>(baseResponseDTO, HttpStatus.OK);
    }

}
