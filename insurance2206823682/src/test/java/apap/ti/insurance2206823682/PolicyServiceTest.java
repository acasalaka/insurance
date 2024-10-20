package apap.ti.insurance2206823682;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;
import apap.ti.insurance2206823682.repository.PolicyDb;
import apap.ti.insurance2206823682.service.PolicyService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PolicyServiceTest {

    @Autowired
    private PolicyService policyService;

    @MockBean
    private PolicyDb policyDb;

    @Test
    void testGetFilteredPolicies() throws ParseException {
        Integer statusInt = 1;
        Long minCoverage = 1000000L;
        Long maxCoverage = 15000000L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate1 = sdf.parse("2024-12-15");
        Date expiryDate2 = sdf.parse("2024-11-20");

        // Not matched with any
        Policy policy1 = new Policy();
        Company company1 = new Company();
        company1.setName("Gotham Insurance");
        policy1.setCompany(company1);
        Patient patient1 = new Patient();
        patient1.setName("James Gordon");
        policy1.setId("POLJGGOT0001");
        policy1.setPatient(patient1);
        policy1.setStatus(0);
        policy1.setExpiryDate(expiryDate1);
        policy1.setTotalCoverage(500000L);
        policy1.setTotalCovered(0L);

        // Status not matched, coverage matched
        Policy policy2 = new Policy();
        Company company2 = new Company();
        company2.setName("Metropolis Insurance");
        policy2.setCompany(company2);
        Patient patient2 = new Patient();
        patient2.setName("Clark Kent");
        policy2.setId("POLCKMET0002");
        policy2.setPatient(patient2);
        policy2.setStatus(0);
        policy2.setExpiryDate(expiryDate2);
        policy2.setTotalCoverage(12000000L);
        policy2.setTotalCovered(50000L);

        // Status matched, coverage not matched
        Policy policy3 = new Policy();
        Company company3 = new Company();
        company3.setName("Central City Insurance");
        policy3.setCompany(company3);
        Patient patient3 = new Patient();
        patient3.setName("Iris West");
        policy3.setId("POLIWCEN0003");
        policy3.setPatient(patient3);
        policy3.setStatus(1);
        policy3.setExpiryDate(expiryDate1);
        policy3.setTotalCoverage(20000000L);
        policy3.setTotalCovered(100000L);

        // All matched
        Policy policy4 = new Policy();
        Company company4 = new Company();
        company4.setName("Star City Insurance");
        policy4.setCompany(company4);
        Patient patient4 = new Patient();
        patient4.setName("Oliver Queen");
        policy4.setId("POLOQSTA0004");
        policy4.setPatient(patient4);
        policy4.setStatus(1);
        policy4.setExpiryDate(expiryDate2);
        policy4.setTotalCoverage(10000000L);
        policy4.setTotalCovered(250000L);

        List<Policy> mockPolicies = Arrays.asList(policy4);
        when(policyDb.findByStatusAndTotalCoverageBetween(statusInt, minCoverage, maxCoverage))
                .thenReturn(mockPolicies);

        List<Policy> policyFiltered = policyService.getFilteredPolicies(statusInt, minCoverage, maxCoverage);

        assertNotNull(policyFiltered);
        assertEquals(1, policyFiltered.size());
        assertEquals("POLOQSTA0004", policyFiltered.get(0).getId());
        assertEquals("Oliver Queen", policyFiltered.get(0).getPatient().getName());
        assertEquals("Star City Insurance", policyFiltered.get(0).getCompany().getName());
        assertEquals(10000000L, policyFiltered.get(0).getTotalCoverage());
        assertEquals(250000L, policyFiltered.get(0).getTotalCovered());
        assertEquals(statusInt, policyFiltered.get(0).getStatus());

        verify(policyDb, times(1)).findByStatusAndTotalCoverageBetween(statusInt, minCoverage, maxCoverage);
    }

    @Test
    void testGetFilteredPolicies2() throws ParseException {
        Integer statusInt = null; // No status filtering
        Long minCoverage = 500000L;
        Long maxCoverage = 12000000L;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate1 = sdf.parse("2024-12-25");
        Date expiryDate2 = sdf.parse("2024-11-30");

        // Not matched with any
        Policy policy1 = new Policy();
        Company company1 = new Company();
        company1.setName("Hell's Kitchen Insurance");
        policy1.setCompany(company1);
        Patient patient1 = new Patient();
        patient1.setName("Matt Murdock");
        policy1.setId("POLMMHE0001");
        policy1.setPatient(patient1);
        policy1.setStatus(0);
        policy1.setExpiryDate(expiryDate1);
        policy1.setTotalCoverage(200000L);
        policy1.setTotalCovered(0L);

        // All matched
        Policy policy2 = new Policy();
        Company company2 = new Company();
        company2.setName("Daily Planet Insurance");
        policy2.setCompany(company2);
        Patient patient2 = new Patient();
        patient2.setName("Lois Lane");
        policy2.setId("POLLLDAI0002");
        policy2.setPatient(patient2);
        policy2.setStatus(0);
        policy2.setExpiryDate(expiryDate2);
        policy2.setTotalCoverage(8000000L);
        policy2.setTotalCovered(100000L);

        // Status matched, coverage not matched
        Policy policy3 = new Policy();
        Company company3 = new Company();
        company3.setName("Star Labs Insurance");
        policy3.setCompany(company3);
        Patient patient3 = new Patient();
        patient3.setName("Cisco Ramon");
        policy3.setId("POLCRSTA0003");
        policy3.setPatient(patient3);
        policy3.setStatus(1);
        policy3.setExpiryDate(expiryDate1);
        policy3.setTotalCoverage(15000000L);
        policy3.setTotalCovered(200000L);

        // All matched
        Policy policy4 = new Policy();
        Company company4 = new Company();
        company4.setName("Stark Industries Insurance");
        policy4.setCompany(company4);
        Patient patient4 = new Patient();
        patient4.setName("Tony Stark");
        policy4.setId("POLTSSTA0004");
        policy4.setPatient(patient4);
        policy4.setStatus(0);
        policy4.setExpiryDate(expiryDate2);
        policy4.setTotalCoverage(7000000L);
        policy4.setTotalCovered(500000L);

        List<Policy> mockPolicies = Arrays.asList(policy2, policy4);
        when(policyDb.findByTotalCoverageBetween(minCoverage, maxCoverage))
                .thenReturn(mockPolicies);

        List<Policy> policyFiltered = policyService.getFilteredPolicies(statusInt, minCoverage, maxCoverage);

        assertNotNull(policyFiltered);
        assertEquals(2, policyFiltered.size());

        assertEquals("POLLLDAI0002", policyFiltered.get(0).getId());
        assertEquals("Lois Lane", policyFiltered.get(0).getPatient().getName());
        assertEquals("Daily Planet Insurance", policyFiltered.get(0).getCompany().getName());
        assertEquals(8000000L, policyFiltered.get(0).getTotalCoverage());
        assertEquals(100000L, policyFiltered.get(0).getTotalCovered());
        assertEquals(1, policyFiltered.get(0).getStatus());

        assertEquals("POLTSSTA0004", policyFiltered.get(1).getId());
        assertEquals("Tony Stark", policyFiltered.get(1).getPatient().getName());
        assertEquals("Stark Industries Insurance", policyFiltered.get(1).getCompany().getName());
        assertEquals(7000000L, policyFiltered.get(1).getTotalCoverage());
        assertEquals(500000L, policyFiltered.get(1).getTotalCovered());
        assertEquals(1, policyFiltered.get(1).getStatus());

        verify(policyDb, times(1)).findByTotalCoverageBetween(minCoverage, maxCoverage);
    }
}

