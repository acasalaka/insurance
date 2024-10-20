package apap.ti.insurance2206823682.restservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Coverage;
import apap.ti.insurance2206823682.repository.CoverageDb;
import apap.ti.insurance2206823682.restdto.response.CoverageResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class CoverageRestServiceImpl implements CoverageRestService {


    @Autowired
    private CoverageDb coverageDb;

    @Override
    public CoverageResponseDTO getCoverageById(Long idCoverage){
        var coverage = coverageDb.findById(idCoverage).orElse(null);

        if (coverage == null) {
            return null;
        }
        
        return coverageToCoverageResponseDTO(coverage);
    }

    private CoverageResponseDTO coverageToCoverageResponseDTO(Coverage coverage) {
        var coverageResponseDTO = new CoverageResponseDTO();
        coverageResponseDTO.setId(coverage.getId());
        coverageResponseDTO.setName(coverage.getName());
        coverageResponseDTO.setCoverageAmount(coverage.getCoverageAmount());
        coverageResponseDTO.setCreatedAt(coverage.getCreatedAt());
        coverageResponseDTO.setUpdatedAt(coverage.getUpdatedAt());
        return coverageResponseDTO;
    }

    



    
}
