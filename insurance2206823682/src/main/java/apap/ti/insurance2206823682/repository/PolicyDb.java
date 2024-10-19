package apap.ti.insurance2206823682.repository;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import apap.ti.insurance2206823682.model.Policy;

@Configuration
@EnableJpaAuditing
@Repository
public interface PolicyDb extends JpaRepository<Policy, String> {
    // Query untuk filter berdasarkan status dan total coverage range
    List<Policy> findByStatusAndTotalCoverageBetween(Integer status, Long minCoverage, Long maxCoverage);

    // Query untuk filter hanya berdasarkan status
    List<Policy> findByStatus(Integer status);

    // Query untuk filter hanya berdasarkan total coverage range
    List<Policy> findByTotalCoverageBetween(Long minCoverage, Long maxCoverage);
    
}
