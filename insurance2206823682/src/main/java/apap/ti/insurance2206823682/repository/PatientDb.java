package apap.ti.insurance2206823682.repository;

import java.util.UUID;

import org.springframework.context.annotation.Configuration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import apap.ti.insurance2206823682.model.Patient;


@Configuration
@EnableJpaAuditing
@Repository
public interface PatientDb extends JpaRepository<Patient, UUID> {
    
}