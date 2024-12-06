package apap.ti.insurance2206823682;

import apap.tk.insurance2206823682.restcontroller.PolicyRestController;
import apap.tk.insurance2206823682.restservice.PolicyRestService;
import apap.tk.insurance2206823682.restdto.response.BaseResponseDTO;
import apap.tk.insurance2206823682.restdto.response.PolicyResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PolicyRestControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PolicyRestService policyRestService;

    @InjectMocks
    private PolicyRestController policyRestController;

    @BeforeEach
    public void setup() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(policyRestController).build();
    }

    @Test
    void testGetPolicyById_NotFound() throws Exception {
        String policyId = UUID.randomUUID().toString();

        // Mock service layer to return null (policy not found)
        when(policyRestService.getPolicyById(policyId)).thenReturn(null);

        // Perform HTTP GET request and verify response
        mockMvc.perform(get("/api/policy/detail?id=" + policyId))
                .andExpect(status().isNotFound()) // Check for 404 status
                .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
                .andExpect(jsonPath("$.message").value("Data policy tidak ditemukan"));

        // Verify the service method was called once
        verify(policyRestService, times(1)).getPolicyById(policyId);
    }

    @Test
    void testGetPolicyById_Success() throws Exception {
        String policyId = UUID.randomUUID().toString();

        // Mock service layer to return a mocked policy
        PolicyResponseDTO mockedPolicyResponseDTO = new PolicyResponseDTO();
        mockedPolicyResponseDTO.setId(policyId);
        mockedPolicyResponseDTO.setExpiryDate(new Date());

        when(policyRestService.getPolicyById(policyId)).thenReturn(mockedPolicyResponseDTO);

        // Perform HTTP GET request and verify response
        mockMvc.perform(get("/api/policy/detail?id=" + policyId))
                .andExpect(status().isOk()) // Check for 200 OK status
                .andExpect(jsonPath("$.status").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("Policy dengan ID " + policyId + " berhasil ditemukan"))
                .andExpect(jsonPath("$.data.id").value(policyId));

        // Verify the service method was called once
        verify(policyRestService, times(1)).getPolicyById(policyId);
    }
}
