package apap.ti.insurance2206823682.service;

import apap.ti.insurance2206823682.model.Patient;

import java.util.List;
import java.util.UUID;


public interface PatientService {
    Patient getPatientByNIK (String nik);
    List<Patient> gettAllPatient();
    Patient addPatient(Patient patient);
    long getInitialLimit(Patient patient);
    long getAvailableLimit(Patient patient);
    String getYearMonthDaysFromBirthDate(Patient patient);
    Patient updatePatient(Patient patient);
    Patient getPatientById(UUID id);
    Patient updateClassPatient(Patient patient);
}
