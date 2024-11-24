package apap.tk.insurance2206823682.repository;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Repository;

import apap.tk.insurance2206823682.model.Coverage;
import jakarta.transaction.Transactional;

@Configuration
@EnableJpaAuditing
@Repository
public interface CoverageDb extends JpaRepository<Coverage, Long> {
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO coverage (id, name, coverage_amount, created_at, updated_at) VALUES " +
            "(1, 'X-ray', 150000, NOW(), NOW()), " +
            "(2, 'CT Scan', 1000000, NOW(), NOW()), " +
            "(3, 'MRI', 2500000, NOW(), NOW()), " +
            "(4, 'Ultrasound', 300000, NOW(), NOW()), " +
            "(5, 'Blood Clotting Test', 50000, NOW(), NOW()), " +
            "(6, 'Blood Glucose Test', 30000, NOW(), NOW()), " +
            "(7, 'Liver Function Test', 75000, NOW(), NOW()), " +
            "(8, 'Complete Blood Count', 50000, NOW(), NOW()), " +
            "(9, 'Urinalysis', 40000, NOW(), NOW()), " +
            "(10, 'COVID-19 testing', 150000, NOW(), NOW()), " +
            "(11, 'Cholesterol Test', 60000, NOW(), NOW()), " +
            "(12, 'Inpatient care', 1000000, NOW(), NOW()), " +
            "(13, 'Surgery', 7000000, NOW(), NOW()), " +
            "(14, 'ICU', 2000000, NOW(), NOW()), " +
            "(15, 'ER', 500000, NOW(), NOW()), " +
            "(16, 'Flu shot', 100000, NOW(), NOW()), " +
            "(17, 'Hepatitis vaccine', 200000, NOW(), NOW()), " +
            "(18, 'COVID-19 Vaccine', 200000, NOW(), NOW()), " +
            "(19, 'MMR Vaccine', 350000, NOW(), NOW()), " +
            "(20, 'HPV Vaccine', 800000, NOW(), NOW()), " +
            "(21, 'Pneumococcal Vaccine', 900000, NOW(), NOW()), " +
            "(22, 'Herpes Zoster Vaccine', 1500000, NOW(), NOW()), " +
            "(23, 'Physical exam', 250000, NOW(), NOW()), " +
            "(24, 'Mammogram', 500000, NOW(), NOW()), " +
            "(25, 'Colonoscopy', 3000000, NOW(), NOW()), " +
            "(26, 'Dental X-ray', 200000, NOW(), NOW()), " +
            "(27, 'Fillings', 400000, NOW(), NOW()), " +
            "(28, 'Dental scaling', 500000, NOW(), NOW()), " +
            "(29, 'Physical therapy', 250000, NOW(), NOW()), " +
            "(30, 'Occupational therapy', 300000, NOW(), NOW()), " +
            "(31, 'Speech therapy', 300000, NOW(), NOW()), " +
            "(32, 'Psychiatric evaluation', 600000, NOW(), NOW()), " +
            "(33, 'Natural delivery', 3500000, NOW(), NOW()), " +
            "(34, 'C-section', 12000000, NOW(), NOW())", 
            nativeQuery = true)
    void insertCoverages();
}
