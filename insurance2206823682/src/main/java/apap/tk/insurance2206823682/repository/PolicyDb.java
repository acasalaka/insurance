package apap.tk.insurance2206823682.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import apap.tk.insurance2206823682.model.Policy;

@Configuration
@EnableJpaAuditing
@Repository
public interface PolicyDb extends JpaRepository<Policy, String> {
    // Query untuk filter berdasarkan status dan total coverage range
    List<Policy> findByStatusAndTotalCoverageBetween(Integer status, Long minCoverage, Long maxCoverage);

    // Query untuk filter hanya berdasarkan status
    List<Policy> findByStatus(Integer status);

    // Query to find policies by status and where isDeleted is not true
    List<Policy> findByStatusAndIsDeletedFalse(Integer status);

    List<Policy> findByPatientIdAndStatusAndIsDeletedFalse(UUID patientId, Integer status);



    // Query untuk filter hanya berdasarkan total coverage range
    List<Policy> findByTotalCoverageBetween(Long minCoverage, Long maxCoverage);

    List<Policy> findByTotalCoverageBetweenAndIsDeletedFalse(Long minCoverage, Long maxCoverage);

    List<Policy> findByPatientIdAndTotalCoverageBetweenAndIsDeletedFalse(Long minCoverage, Long maxCoverage, UUID id);


    List<Policy> findAllByIsDeletedFalse();    
    List<Policy> findByPatientIdAndIsDeletedFalse(UUID patientId);
    Policy findByIdAndIdPatient(String id, UUID patientId);
}
