package apap.tk.insurance2206823682.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import apap.tk.insurance2206823682.model.Company;

@Configuration
@EnableJpaAuditing
@Repository
public interface CompanyDb extends JpaRepository<Company, UUID> {

    List<Company> findByNameContainingIgnoreCase(String nama);

    List<Company> findByDeletedAtIsNull();

    
}
