package apap.tk.insurance2206823682.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import apap.tk.insurance2206823682.model.Policy;

@Configuration
@EnableJpaAuditing
@Repository
public interface PolicyDb extends JpaRepository<Policy, Long> {

    // Query untuk filter berdasarkan status dan total coverage range
    List<Policy> findByStatusAndTotalCoverageBetween(Integer status, Long minCoverage, Long maxCoverage);

    // Query untuk filter hanya berdasarkan status
    List<Policy> findByStatus(Integer status);

    Policy findById(String id);

    Policy findByIdAndPatientId(String id, UUID patientId);


    // Query to find policies by status and where isDeleted is not true
    List<Policy> findByStatusAndIsDeletedFalse(Integer status);

    // Query untuk filter hanya berdasarkan total coverage range
    List<Policy> findByTotalCoverageBetween(Long minCoverage, Long maxCoverage);

    List<Policy> findByTotalCoverageBetweenAndIsDeletedFalse(Long minCoverage, Long maxCoverage);

    // Query to find all policies that are not deleted
    List<Policy> findAllByIsDeletedFalse();

    // Fix method to find policies by Patient ID and check if isDeleted is false
    List<Policy> findByPatientIdAndIsDeletedFalse(UUID patientId);

    // Query to find policies by patientId, total coverage range, and isDeleted false
    List<Policy> findByPatientIdAndTotalCoverageBetweenAndIsDeletedFalse(UUID patientId, Long minCoverage, Long maxCoverage);

    // New method to find policies by patient ID and status with isDeleted being false
    List<Policy> findByPatientIdAndStatusAndIsDeletedFalse(UUID patientId, Integer status);

    @Query("SELECT p FROM Policy p " +
            "JOIN p.listCoverage c " +
            "WHERE c.id IN :coverageIds " +
            "AND p.isDeleted = false")
    List<Policy> findByCoverageIds(List<Long> coverageIds);
}