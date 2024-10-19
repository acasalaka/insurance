package apap.ti.insurance2206823682.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.insurance2206823682.service.CompanyService;
import apap.ti.insurance2206823682.service.CoverageService;
import apap.ti.insurance2206823682.service.PatientService;
import apap.ti.insurance2206823682.service.PolicyService;
import jakarta.validation.Valid;
import apap.ti.insurance2206823682.dto.request.AddCompanyRequestDTO;
import apap.ti.insurance2206823682.dto.request.AddPatientPolicyRequestDTO;
import apap.ti.insurance2206823682.dto.request.AddPolicyRequestDTO;
import apap.ti.insurance2206823682.dto.request.UpdatePolicyRequestDTO;
import apap.ti.insurance2206823682.dto.request.UpgradePatientRequestDTO;
import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Coverage;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.model.Policy;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.text.NumberFormat;
import java.util.ArrayList;

@Controller
public class PolicyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CoverageService coverageService;

    @Autowired
    private PolicyService policyService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/policy/create/search-patient")
    private String searchPatient(Model model) {
        return "search-patient";
    }

    @PostMapping("/policy/create/search-patient")
    public String searchPatient(@RequestParam("nik") String nik, Model model) {
        Patient searchedPatient = patientService.getPatientByNIK(nik);
        if (searchedPatient == null) {
            model.addAttribute("nik", nik);
            return "response-patient-not-found";
        }

        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        model.addAttribute("patient", searchedPatient);
        model.addAttribute("insuranceLimit", currencyFormat.format(patientService.getInitialLimit(searchedPatient)));
        model.addAttribute("yearMonthDay", patientService.getYearMonthDaysFromBirthDate(searchedPatient));

        return "response-patient-found";
    }

    @GetMapping("/policy/create-with-patient")
    private String addPatient(Model model) {
        var patientDTO = new AddPatientPolicyRequestDTO();
        var listCompany = companyService.getAllCompany();

        model.addAttribute("listCompany", listCompany);
        model.addAttribute("patientDTO", patientDTO);
        return "form-add-patient";
    }

    @PostMapping(value = "/policy/create-with-patient", params = { "viewCoverage" })
    public String viewCoverageCompany(@ModelAttribute("patientDTO") AddPatientPolicyRequestDTO patientDTO,
            Model model) {
        // Get the selected company by ID
        Company selectedCompany = companyService.getCompanyById(patientDTO.getCompany().getId());

        if (selectedCompany != null) {
            // Fetch the coverages and total coverage
            List<Coverage> listCoverage = selectedCompany.getListCoverage();
            String totalCoverage = companyService.calculateTotalCoverageForCompany(selectedCompany);

            // Add the fetched data to the model
            model.addAttribute("listCoverage", listCoverage);
            model.addAttribute("totalCoverage", totalCoverage);
        }

        // Retain patientDTO and listCompany for redisplay in the form
        model.addAttribute("patientDTO", patientDTO);
        model.addAttribute("listCompany", companyService.getAllCompany());

        return "form-add-patient";
    }

    @PostMapping("/policy/create-with-patient")
    public String addPatient(@Valid @ModelAttribute("patientDTO") AddPatientPolicyRequestDTO patientDTO,
            BindingResult bindingResult, Model model) {
        Company selectedCompany = companyService.getCompanyById(patientDTO.getCompany().getId());
        if (bindingResult.hasErrors()) {
            if (selectedCompany != null) {
                // Fetch the coverages and total coverage
                List<Coverage> listCoverage = selectedCompany.getListCoverage();
                String totalCoverage = companyService.calculateTotalCoverageForCompany(selectedCompany);
                model.addAttribute("listCoverage", listCoverage);
                model.addAttribute("totalCoverage", totalCoverage);
            }

            model.addAttribute("listCompany", companyService.getAllCompany());
            return "form-add-patient";
        }

        var patient = new Patient();
        patient.setNik(patientDTO.getNik());
        patient.setName(patientDTO.getName());
        patient.setGender(patientDTO.getGender());
        patient.setBirthDate(patientDTO.getBirthDate());
        patient.setEmail(patientDTO.getEmail());
        patient.setPClass(patientDTO.getPClass());

        var policy = new Policy();
        Company companyFound = companyService.getCompanyById(patientDTO.getCompany().getId());
        String idPolicy = policyService.createId(patientDTO.getName(), companyFound.getName());
        policy.setId(idPolicy);
        policy.setCompany(companyFound);
        policy.setPatient(patient);
        policy.setStatus(0);
        policy.setExpiryDate(patientDTO.getExpiryDate());
        policy.setTotalCoverage(companyService.getTotalCoverage(companyFound));
        policy.setTotalCovered(0);

        patient.setListPolicy(new ArrayList<>());

        if (patientService.getAvailableLimit(patient) - policy.getTotalCoverage() < 0) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formatedLimit = currencyFormat.format(patientService.getAvailableLimit(patient));
            String formattedCoverage = currencyFormat.format(policy.getTotalCoverage());

            model.addAttribute("patientLimit", formatedLimit);
            model.addAttribute("companyCoverage", formattedCoverage);
            return "response-policy-unsuccessful";
        }

        List<Policy> newListPolicy = new ArrayList<>();
        newListPolicy.add(policy);
        patient.setListPolicy(newListPolicy);

        patientService.addPatient(patient);
        policyService.addPolicy(policy);

        model.addAttribute("responseMessage", String.format("Berhasil menambahkan policy dengan ID %s kepada pasien %s",
                idPolicy, patient.getName()));
        return "response-policy";

    }

    @GetMapping("/policy/{nik}/create")
    public String addPolicy(@PathVariable("nik") String nik, Model model) {
        Patient searchedPatient = patientService.getPatientByNIK(nik);

        var patientDTO = new AddPolicyRequestDTO();
        var listCompany = companyService.getAllCompany();

        model.addAttribute("listCompany", listCompany);
        model.addAttribute("patientDTO", patientDTO);
        model.addAttribute("patient", searchedPatient);

        model.addAttribute("insuranceLimit", patientService.getInitialLimit(searchedPatient));
        model.addAttribute("availableLimit", patientService.getAvailableLimit(searchedPatient));

        return "form-add-policy";
    }

    @PostMapping(value = "/policy/{nik}/create", params = { "viewCoverage" })
    public String viewCoverageCompany(@ModelAttribute("patientDTO") AddPolicyRequestDTO patientDTO,
            @PathVariable("nik") String nik, Model model) {
        Patient searchedPatient = patientService.getPatientByNIK(nik);
        var listCompany = companyService.getAllCompany();

        Company selectedCompany = companyService.getCompanyById(patientDTO.getCompany().getId());

        if (selectedCompany != null) {
            // Fetch the coverages and total coverage
            List<Coverage> listCoverage = selectedCompany.getListCoverage();
            String totalCoverage = companyService.calculateTotalCoverageForCompany(selectedCompany);

            // Add the fetched data to the model
            model.addAttribute("listCoverage", listCoverage);
            model.addAttribute("totalCoverage", totalCoverage);
        }

        model.addAttribute("listCompany", listCompany);
        model.addAttribute("patientDTO", patientDTO);
        model.addAttribute("patient", searchedPatient);
        model.addAttribute("insuranceLimit", patientService.getInitialLimit(searchedPatient));
        model.addAttribute("availableLimit", patientService.getAvailableLimit(searchedPatient));

        return "form-add-policy";
    }

    @PostMapping("/policy/{nik}/create")
    public String addPolicy(@PathVariable("nik") String nik,
            @Valid @ModelAttribute("patientDTO") AddPolicyRequestDTO patientDTO, BindingResult bindingResult,
            Model model) {
        Patient searchedPatient = patientService.getPatientByNIK(nik);
        Company selectedCompany = companyService.getCompanyById(patientDTO.getCompany().getId());

        if (bindingResult.hasErrors()) {
            if (selectedCompany != null) {
                // Fetch the coverages and total coverage
                List<Coverage> listCoverage = selectedCompany.getListCoverage();
                String totalCoverage = companyService.calculateTotalCoverageForCompany(selectedCompany);
                model.addAttribute("listCoverage", listCoverage);
                model.addAttribute("totalCoverage", totalCoverage);
            }
            model.addAttribute("listCompany", companyService.getAllCompany());
            model.addAttribute("patient", searchedPatient);
            model.addAttribute("insuranceLimit", patientService.getInitialLimit(searchedPatient));
            model.addAttribute("availableLimit", patientService.getAvailableLimit(searchedPatient));

            return "form-add-policy";
        }

        var policy = new Policy();
        Company companyFound = companyService.getCompanyById(patientDTO.getCompany().getId());
        String idPolicy = policyService.createId(searchedPatient.getName(), companyFound.getName());
        policy.setId(idPolicy);
        policy.setCompany(companyFound);
        policy.setPatient(searchedPatient);
        policy.setStatus(0);
        policy.setExpiryDate(patientDTO.getExpiryDate());
        policy.setTotalCoverage(companyService.getTotalCoverage(companyFound));
        policy.setTotalCovered(0);

        if (patientService.getAvailableLimit(searchedPatient) - policy.getTotalCoverage() < 0) {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            String formatedLimit = currencyFormat.format(patientService.getAvailableLimit(searchedPatient));
            String formattedCoverage = currencyFormat.format(policy.getTotalCoverage());

            model.addAttribute("patientLimit", formatedLimit);
            model.addAttribute("companyCoverage", formattedCoverage);
            model.addAttribute("patient", searchedPatient);
            return "response-policy-unsuccessful";
        }

        for (Policy policyItem : searchedPatient.getListPolicy()){
            if (policyItem.getCompany().getId().equals(companyFound.getId())){
                model.addAttribute("responseMessage", String.format("Gagal menambahkan policy, sudah terdapat policy dengan Company %s", selectedCompany.getName()));
                return "response-policy";
            }
        }

        Policy policyAdded = policyService.addPolicy(policy);

        List<Policy> newListPolicy = searchedPatient.getListPolicy();
        newListPolicy.add(policyAdded);
        searchedPatient.setListPolicy(newListPolicy);

        patientService.updatePatient(searchedPatient);

        model.addAttribute("responseMessage", String.format("Berhasil menambahkan policy dengan ID %s kepada pasien %s",
                idPolicy, searchedPatient.getName()));
        return "response-policy";

    }

    @GetMapping("/policy/all")
    public String viewAllPolicies(
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "minCoverage", required = false) Long minCoverage,
            @RequestParam(value = "maxCoverage", required = false) Long maxCoverage,
            Model model) {

        // Get filtered list of policies based on provided filters
        List<Policy> filteredPolicies = policyService.getFilteredPolicies(status, minCoverage, maxCoverage);

        // Add the list of policies and filters back to the model for rendering
        model.addAttribute("listPolicy", filteredPolicies);
        model.addAttribute("status", status);
        model.addAttribute("minCoverage", minCoverage);
        model.addAttribute("maxCoverage", maxCoverage);

        return "viewall-policy"; // Return the corresponding Thymeleaf template
    }

    @GetMapping("/policy/{id}")
    public String viewPolicy(@PathVariable("id") String id, Model model) {
        Policy searchedPolicy = policyService.getPolicyById(id);
        model.addAttribute("policy", searchedPolicy);

        return "view-policy";
    }

    @GetMapping("/policy/{id}/update")
    public String updatePolicy(@PathVariable("id") String id, Model model) {
        Policy searchedpPolicy = policyService.getPolicyById(id);
        var policyDTO = new UpdatePolicyRequestDTO();
        policyDTO.setId(id);
        policyDTO.setExpiryDate(searchedpPolicy.getExpiryDate());

        model.addAttribute("policyDTO", policyDTO);
        model.addAttribute("policy", searchedpPolicy);
        List<Coverage> listCoverage = searchedpPolicy.getCompany().getListCoverage();
        String totalCoverage = companyService.calculateTotalCoverageForCompany(searchedpPolicy.getCompany());

        // Add the fetched data to the model
        model.addAttribute("listCoverage", listCoverage);
        model.addAttribute("totalCoverage", totalCoverage);

        return "form-update-policy";
    }

    @PostMapping("/policy/update")
    public String updatePolicy(@Valid @ModelAttribute("policyDTO") UpdatePolicyRequestDTO policyDTO,
            BindingResult bindingResult, Model model) {
        Policy searchedPolicy = policyService.getPolicyById(policyDTO.getId());

        // If there are binding errors, return the form with errors
        if (bindingResult.hasErrors()) {
            model.addAttribute("policyDTO", policyDTO);
            model.addAttribute("policy", searchedPolicy);
            List<Coverage> listCoverage = searchedPolicy.getCompany().getListCoverage();
            String totalCoverage = companyService.calculateTotalCoverageForCompany(searchedPolicy.getCompany());

            // Add the fetched data to the model
            model.addAttribute("listCoverage", listCoverage);
            model.addAttribute("totalCoverage", totalCoverage);
            return "form-update-policy";
        }

        // Update the policy's expiry date
        searchedPolicy.setExpiryDate(policyDTO.getExpiryDate());

        // Save the updated policy
        policyService.updatePolicy(searchedPolicy);

        model.addAttribute("responseMessage",
                String.format("Berhasil melakukan update terhadap policy dengan ID %s", searchedPolicy.getId()));
        return "response-policy";
    }

    @GetMapping("/policy/{id}/delete")
    public String deleteCompany(@PathVariable("id") String id, Model model) {
        var policy = policyService.getPolicyById(id);
        policyService.deletePolicy(policy);

        model.addAttribute("responseMessage", String.format("Berhasil melakuakn pembatalan Policy dengan ID %s", policy.getId()));

        return "response-policy";
    }

}
