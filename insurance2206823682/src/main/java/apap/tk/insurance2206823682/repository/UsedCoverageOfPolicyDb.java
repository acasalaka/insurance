package apap.tk.insurance2206823682.repository;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;


import apap.tk.insurance2206823682.model.UsedCoverageOfPolicy;

@Configuration
@EnableJpaAuditing
@Repository
public interface UsedCoverageOfPolicyDb extends JpaRepository<UsedCoverageOfPolicy, Long> {
    
}
