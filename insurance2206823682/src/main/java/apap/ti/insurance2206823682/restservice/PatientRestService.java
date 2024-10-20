package apap.ti.insurance2206823682.restservice;

import java.util.UUID;

import apap.ti.insurance2206823682.restdto.response.PatientResponseDTO;

public interface PatientRestService {
    PatientResponseDTO getPatientById(UUID idPatient);
    
}
