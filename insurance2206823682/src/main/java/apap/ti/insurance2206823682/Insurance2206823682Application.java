package apap.ti.insurance2206823682;


import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import apap.ti.insurance2206823682.service.CompanyService;
import apap.ti.insurance2206823682.service.CoverageService;
import jakarta.transaction.Transactional;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class Insurance2206823682Application {

    public static void main(String[] args) {
        SpringApplication.run(Insurance2206823682Application.class, args);
    }

    @SuppressWarnings("rawtypes")
    @Bean
    @Transactional
    CommandLineRunner run(CompanyService companyService, CoverageService coverageService) {
        return args -> {
            // Insert predefined Coverage data if it's not present
            if (coverageService.countCoverages() == 0) {
                coverageService.initData();
            }
		};
    }
}
