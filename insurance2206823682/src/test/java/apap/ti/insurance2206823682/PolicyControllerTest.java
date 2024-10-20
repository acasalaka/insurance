package apap.ti.insurance2206823682;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;
import apap.ti.insurance2206823682.service.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
public class PolicyControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private PolicyService policyService;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    /**
     * Create mock data for testing.
     */
    private List<Policy> createMockPolicies() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date expiryDate1 = sdf.parse("2024-12-15");
        Date expiryDate2 = sdf.parse("2024-11-20");

        // Policy 1
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

        // Policy 2
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

        // Policy 3
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

        // Policy 4
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

        return Arrays.asList(policy1, policy2, policy3, policy4);
    }

    @Test
    @DisplayName("Test Get All Policies Without Filters")
    public void testGetPoliciesWithoutFilters() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Mock data
        List<Policy> mockPolicies = createMockPolicies();
        Mockito.when(policyService.getFilteredPolicies(null, null, null)).thenReturn(mockPolicies);

        // Perform the request and verify results
        mockMvc.perform(get("/policy/all"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewall-policy"))
                .andExpect(model().attributeExists("listPolicy"))
                .andExpect(model().attribute("listPolicy", hasSize(4)))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLJGGOT0001")),
                                hasProperty("patient", hasProperty("name", is("James Gordon"))),
                                hasProperty("company", hasProperty("name", is("Gotham Insurance"))),
                                hasProperty("totalCoverage", is(500000L))))))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLOQSTA0004")),
                                hasProperty("patient", hasProperty("name", is("Oliver Queen"))),
                                hasProperty("company", hasProperty("name", is("Star City Insurance"))),
                                hasProperty("totalCoverage", is(10000000L))))));

        // Verify that the service method was called
        verify(policyService, times(1)).getFilteredPolicies(null, null, null);
    }

    @Test
    @DisplayName("Test Get Policies With Status Filter")
    public void testGetPoliciesWithStatusFilter() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Arrange mock data
        List<Policy> filteredPolicies = Arrays.asList(createMockPolicies().get(0)); // Only "Created" policy
        Mockito.when(policyService.getFilteredPolicies(0, null, null)).thenReturn(filteredPolicies);

        // Perform the request and verify results
        mockMvc.perform(get("/policy/all")
                .param("status", "0"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewall-policy"))
                .andExpect(model().attributeExists("listPolicy"))
                .andExpect(model().attribute("listPolicy", hasSize(1)))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLJGGOT0001")),
                                hasProperty("patient", hasProperty("name", is("James Gordon"))), // Corrected for nested
                                                                                                 // property
                                hasProperty("company", hasProperty("name", is("Gotham Insurance"))), // Corrected for
                                                                                                     // nested property
                                hasProperty("totalCoverage", is(500000L))))));

        // Verify that the service method was called with correct filter
        verify(policyService, times(1)).getFilteredPolicies(0, null, null);
    }

    @Test
    @DisplayName("Test Get Policies With Coverage Filter and No Results")
    public void testGetPoliciesWithCoverageFilterNoResults() throws Exception {
        // No policies match the coverage range
        Mockito.when(policyService.getFilteredPolicies(null, 5000000L, 10000000L)).thenReturn(Arrays.asList());

        // Perform the request and verify no policies are returned
        mockMvc.perform(get("/policy/all")
                .param("minCoverage", "5000000")
                .param("maxCoverage", "10000000"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewall-policy"))
                .andExpect(model().attributeExists("listPolicy"))
                .andExpect(model().attribute("listPolicy", hasSize(0)));

        // Verify that the service method was called with coverage range
        verify(policyService, times(1)).getFilteredPolicies(null, 5000000L, 10000000L);
    }

    @Test
    @DisplayName("Test Get Policies With Coverage Filter")
    public void testGetPoliciesWithCoverageFilter() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // Policies with coverage between 1,000,000 and 12,000,000
        List<Policy> filteredPolicies = Arrays.asList(
                createMockPolicies().get(0), // 500,000
                createMockPolicies().get(1), // 12,000,000
                createMockPolicies().get(3) // 10,000,000
        );

        Mockito.when(policyService.getFilteredPolicies(null, 1000000L, 12000000L)).thenReturn(filteredPolicies);

        // Perform the request and verify results
        mockMvc.perform(get("/policy/all")
                .param("minCoverage", "1000000")
                .param("maxCoverage", "12000000"))
                .andExpect(status().isOk())
                .andExpect(view().name("viewall-policy"))
                .andExpect(model().attributeExists("listPolicy"))
                .andExpect(model().attribute("listPolicy", hasSize(3)))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLJGGOT0001")),
                                hasProperty("patient", hasProperty("name", is("James Gordon"))), // Correct for nested
                                                                                                 // patient name
                                hasProperty("company", hasProperty("name", is("Gotham Insurance"))), // Correct for
                                                                                                     // nested company
                                                                                                     // name
                                hasProperty("totalCoverage", is(500000L))))))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLCKMET0002")),
                                hasProperty("patient", hasProperty("name", is("Clark Kent"))), // Correct for nested
                                                                                               // patient name
                                hasProperty("company", hasProperty("name", is("Metropolis Insurance"))), // Correct for
                                                                                                         // nested
                                                                                                         // company name
                                hasProperty("totalCoverage", is(12000000L))))))
                .andExpect(model().attribute("listPolicy", hasItem(
                        allOf(
                                hasProperty("id", is("POLOQSTA0004")),
                                hasProperty("patient", hasProperty("name", is("Oliver Queen"))), // Correct for nested
                                                                                                 // patient name
                                hasProperty("company", hasProperty("name", is("Star City Insurance"))), // Correct for
                                                                                                        // nested
                                                                                                        // company name
                                hasProperty("totalCoverage", is(10000000L))))));

        // Verify that the service method was called with the coverage range
        verify(policyService, times(1)).getFilteredPolicies(null, 1000000L, 12000000L);
    }
}