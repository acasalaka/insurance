package apap.ti.insurance2206823682.restservice;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.repository.PatientDb;
import apap.ti.insurance2206823682.restdto.response.PatientResponseDTO;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PatientRestServiceImpl implements PatientRestService {
    
    @Autowired
    private PatientDb patientDb;
    
    @Override
    public PatientResponseDTO getPatientById(UUID idPatient){
        var patient = patientDb.findById(idPatient).orElse(null);

        if (patient == null) {
            return null;
        }
        

        return patientToPatientResponseDTO(patient);
    }


    private PatientResponseDTO patientToPatientResponseDTO(Patient patient) {
        var patientResponseDTO = new PatientResponseDTO();
        patientResponseDTO.setId(patient.getId());
        patientResponseDTO.setNik(patient.getNik());
        patientResponseDTO.setName(patient.getName());
        patientResponseDTO.setGender(patient.getGender());
        patientResponseDTO.setBirthDate(patient.getBirthDate());
        patientResponseDTO.setEmail(patient.getEmail());
        patientResponseDTO.setPClass(patient.getPClass());
        patientResponseDTO.setCreatedAt(patient.getCreatedAt());
        patientResponseDTO.setUpdatedAt(patient.getUpdatedAt());
        return patientResponseDTO;
    }



}
