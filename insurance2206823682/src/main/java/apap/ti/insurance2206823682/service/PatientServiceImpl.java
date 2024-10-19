package apap.ti.insurance2206823682.service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;
import apap.ti.insurance2206823682.repository.PatientDb;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientDb patientDb;

    @Override
    public List<Patient> gettAllPatient() {
        return patientDb.findAll();
    }

    @Override
    public Patient getPatientByNIK(String nik) {
        for (Patient patient : gettAllPatient()) {
            if (patient.getNik().equals(nik)) {
                return patient;
            }
        }
        return null;
    }

    @Override
    public Patient addPatient(Patient patient) {
        return patientDb.save(patient);
    }

    @Override
    public long getInitialLimit(Patient patient) {
        if (patient.getPClass() == 1) {
            return 100000000L;
        } else if (patient.getPClass() == 2) {
            return 50000000L;
        } else {
            return 25000000L;
        }
    }

    @Override
    public long getAvailableLimit(Patient patient) {
        long totalCoveragePolicy = 0;
        for (Policy policy : patient.getListPolicy()) {
            totalCoveragePolicy += policy.getTotalCoverage();
        }
        return (getInitialLimit(patient) - totalCoveragePolicy);
    }

    @Override
    public String getYearMonthDaysFromBirthDate(Patient patient) {
        Date birthDate = patient.getBirthDate(); // java.util.Date or java.sql.Date
        if (birthDate == null) {
            return "";
        }

        LocalDate birthLocalDate;

        // Check if the birthDate is an instance of java.sql.Date and handle it
        // accordingly
        if (birthDate instanceof java.sql.Date) {
            // Convert java.sql.Date to java.time.LocalDate
            birthLocalDate = ((java.sql.Date) birthDate).toLocalDate();
        } else {
            // Convert java.util.Date to LocalDate
            birthLocalDate = birthDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }

        LocalDate currentDate = LocalDate.now();

        // Calculate the period between birth date and current date
        Period period = Period.between(birthLocalDate, currentDate);
        int years = period.getYears();
        int months = period.getMonths();
        int days = period.getDays();

        // Return formatted string like "X years, Y months, Z days"
        return String.format("%d years, %d months, %d days", years, months, days);
    }

    @Override
    public Patient updatePatient(Patient patient){
        Patient getPatient = getPatientByNIK(patient.getNik());
        if (getPatient != null) {
            getPatient.setId(patient.getId());
            getPatient.setNik(patient.getNik());
            getPatient.setName(patient.getName());
            getPatient.setGender(patient.getGender());
            getPatient.setBirthDate(patient.getBirthDate());
            getPatient.setEmail(patient.getEmail());
            getPatient.setPClass(patient.getPClass());
            patient.setListPolicy(patient.getListPolicy());

            patientDb.save(getPatient);
            return getPatient;
        }
        return null;
    }
}
