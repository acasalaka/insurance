package apap.tk.insurance2206823682;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.github.javafaker.Faker;

import apap.tk.insurance2206823682.model.Company;
import apap.tk.insurance2206823682.model.Coverage;
import apap.tk.insurance2206823682.model.Policy;
import apap.tk.insurance2206823682.restservice.PolicyRestService;
import apap.tk.insurance2206823682.service.CompanyService;
import apap.tk.insurance2206823682.service.CoverageService;
import apap.tk.insurance2206823682.service.PolicyService;
import jakarta.transaction.Transactional;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class Insurance2206823682Application {

    public static void main(String[] args) {
        SpringApplication.run(Insurance2206823682Application.class, args);
    }

    @SuppressWarnings("rawtypes")
    @Transactional
    @Bean
    CommandLineRunner run(PolicyRestService policyRestService, PolicyService policyService, CompanyService companyService, CoverageService coverageService) {
        return args -> {
            @SuppressWarnings("deprecation")
			var faker = new Faker(new Locale("in-ID"));

            if (coverageService.countCoverages() == 0) {
                coverageService.initData();
            }

			for (int i = 0; i < 10; i++) {
                // Create and save a company
                Company company = new Company();
                company.setName(faker.company().name());
                company.setContact(faker.phoneNumber().cellPhone());
                company.setEmail("fakedeveloper@test.com");
                company.setAddress(faker.address().fullAddress());
                company.setCreatedAt(new Date());
                List<Coverage> listCoverageCompany = new ArrayList<>();
                listCoverageCompany.add(coverageService.getById(1L));
                listCoverageCompany.add(coverageService.getById(2L));
                listCoverageCompany.add(coverageService.getById(3L));
                listCoverageCompany.add(coverageService.getById(5L));

                company.setListCoverageCompany(listCoverageCompany);

                companyService.addCompany(company);

                // Create a policy with randomized status and set fields based on status
                Policy policy = new Policy();
                policy.setId(policyService.createId(faker.name().fullName(), company.getName()));
                policy.setCompanyId(company.getId());
                policy.setPatientId(UUID.randomUUID());
    
                // int status = faker.number().numberBetween(0, 5); // Randomized status between 0 and 4
                int status = 0; // Randomized status between 0 and 4

                policy.setStatus(status);
    
                int totalCoverage = faker.number().numberBetween(1, 1000000); // Random total coverage
                int totalCovered = 0;
    
                if (status == 0) {
                    // Status 0: totalCovered is 0, totalCoverage is random
                    totalCovered = 0;
                } else if (status == 1) {
                    // Status 1: 0 < totalCovered < totalCoverage
                    totalCovered = faker.number().numberBetween(1, totalCoverage);
                } else if (status == 2) {
                    // Status 2: totalCovered == totalCoverage
                    totalCovered = totalCoverage;
                } else if (status == 3) {
                    // Status 3: expiryDate is before today's date
                    policy.setExpiryDate(faker.date().past(10 * 365, TimeUnit.DAYS));
                } else {
                    // Default case, no specific condition for status 4
                    totalCovered = faker.number().numberBetween(0, totalCoverage);
                }
    
                // Set totalCoverage and totalCovered
                policy.setTotalCoverage(totalCoverage);
                policy.setTotalCovered(totalCovered);
    
                // Set default expiryDate if not already set
                if (policy.getExpiryDate() == null) {
                    policy.setExpiryDate(faker.date().future(365, TimeUnit.DAYS)); // Expiry date in the future
                }
    
                policy.setCreatedAt(new Date());
                policyService.addPolicy(policy);
            }
		};
    }
}
