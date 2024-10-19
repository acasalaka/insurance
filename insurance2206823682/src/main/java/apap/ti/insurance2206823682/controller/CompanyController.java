package apap.ti.insurance2206823682.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.ti.insurance2206823682.dto.request.AddCompanyRequestDTO;
import apap.ti.insurance2206823682.dto.request.UpdateCompanyRequestDTO;
import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Coverage;
import apap.ti.insurance2206823682.service.CompanyService;
import apap.ti.insurance2206823682.service.CoverageService;
import apap.ti.insurance2206823682.service.PatientService;
import apap.ti.insurance2206823682.service.PolicyService;
import jakarta.validation.Valid;

@Controller
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @Autowired
    private CoverageService coverageService;

    // @Autowired
    // private PatientService patientService;

    // @Autowired
    // private PolicyService policyService;

    @GetMapping("/")
    private String home(Model model) {
        // tambahin model attribute buat total nya
        return "home";
    }

    @GetMapping("/company/all")
    private String viewAllCompany(Model model) {
        var listCompany = companyService.getAllCompany();

        // Prepare a map to store total coverage for each company
        Map<UUID, String> companyTotalCoverageMap = new HashMap<>();
        Map<UUID, String> companyCoverageMap = new HashMap<>();

        // Calculate total coverage for each company and store it in the map
        for (Company company : listCompany) {
            String totalCoverage = companyService.calculateTotalCoverageForCompany(company);
            companyTotalCoverageMap.put(company.getId(), totalCoverage);
            String formattedCoverage = companyService.formatCoverageNames(company.getListCoverage());
            companyCoverageMap.put(company.getId(), formattedCoverage);
        }
        model.addAttribute("companyCoverageMap", companyCoverageMap);
        model.addAttribute("companyTotalCoverageMap", companyTotalCoverageMap);
        model.addAttribute("listCompany", listCompany);

        return "viewall-company";
    }

    @GetMapping("/company/add")
    public String formAddPekerja(Model model) {
        var companyDTO = new AddCompanyRequestDTO();
        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", companyDTO);
        return "form-add-company";
    }

    @PostMapping("/company/add")
    public String addCompany(@Valid @ModelAttribute("companyDTO") AddCompanyRequestDTO addCompanyRequestDTO,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
            return "form-add-company";
        }

        var company = new Company();
        company.setName(addCompanyRequestDTO.getName());
        company.setEmail(addCompanyRequestDTO.getEmail());
        company.setAddress(addCompanyRequestDTO.getAddress());
        company.setContact(addCompanyRequestDTO.getContact());
        company.setListCoverage(addCompanyRequestDTO.getListCoverage());

        companyService.addCompany(company);

        model.addAttribute("responseMessage", String.format("Berhasil menambahkan Company %s.", company.getName()));
        return "response-company";

    }

    @PostMapping(value = "/company/add", params = { "addRow" })
    public String addRowCoverageCompany(@ModelAttribute("companyDTO") AddCompanyRequestDTO addCompanyRequestDTO,
            Model model) {
        if (addCompanyRequestDTO.getListCoverage() == null || addCompanyRequestDTO.getListCoverage().isEmpty()) {
            addCompanyRequestDTO.setListCoverage(new ArrayList<>());
        }

        addCompanyRequestDTO.getListCoverage().add(new Coverage());

        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", addCompanyRequestDTO);
        return "form-add-company";
    }

    @PostMapping(value = "/company/add", params = { "deleteRow" })
    public String deleteRowCoverageCompany(@ModelAttribute AddCompanyRequestDTO addCompanyRequestDTO,
            @RequestParam("deleteRow") int row, Model model) {
        addCompanyRequestDTO.getListCoverage().remove(row);

        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", addCompanyRequestDTO);
        return "form-add-company";
    }

    @GetMapping("/company/{id}")
    public String detailCompany(@PathVariable("id") UUID id, Model model) {
        var company = companyService.getCompanyById(id);
        String companyTotalCoverage = companyService.calculateTotalCoverageForCompany(company);

        model.addAttribute("companyTotalCoverage", companyTotalCoverage);
        model.addAttribute("company", company);
        return "view-company";

    }

    @GetMapping("/company/{id}/update")
    public String updateCompany(@PathVariable("id") UUID id, Model model) {
        var company = companyService.getCompanyById(id);
        var updateCompanyDTO = new UpdateCompanyRequestDTO();

        updateCompanyDTO.setId(id);
        updateCompanyDTO.setName(company.getName());
        updateCompanyDTO.setContact(company.getContact());
        updateCompanyDTO.setEmail(company.getEmail());
        updateCompanyDTO.setAddress(company.getAddress());
        updateCompanyDTO.setListCoverage(company.getListCoverage());

        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", updateCompanyDTO);
        return "form-update-company";
    }

    @PostMapping("/company/update")
    public String updateCompany(@ModelAttribute("companyDTO") @Valid UpdateCompanyRequestDTO companyDTO,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
            return "form-update-company";
        }
        var companyFromDTO = new Company();
        companyFromDTO.setId(companyDTO.getId());
        companyFromDTO.setName(companyDTO.getName());
        companyFromDTO.setContact(companyDTO.getContact());
        companyFromDTO.setEmail(companyDTO.getEmail());
        companyFromDTO.setAddress(companyDTO.getAddress());
        companyFromDTO.setListCoverage(companyDTO.getListCoverage());

        System.out.println("ini print");
        System.out.println(companyDTO.getId());

        companyService.updateCompany(companyFromDTO);

        model.addAttribute("responseMessage",
                String.format("Data Company %s berhasil diubah.", companyFromDTO.getName()));
        return "response-company";
    }

    // Method for adding a new row of coverage
    @PostMapping(value = "/company/update", params = { "addRow" })
    public String addRowCoverageUpdateCompany(@ModelAttribute("companyDTO") UpdateCompanyRequestDTO companyDTO,
            Model model) {
        if (companyDTO.getListCoverage() == null || companyDTO.getListCoverage().isEmpty()) {
            companyDTO.setListCoverage(new ArrayList<>());
        }

        companyDTO.getListCoverage().add(new Coverage());

        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", companyDTO);
        return "form-update-company";
    }

    // Method for deleting a row of coverage
    @PostMapping(value = "/company/update", params = { "deleteRow" })
    public String deleteRowCoverageUpdateCompany(@ModelAttribute UpdateCompanyRequestDTO companyDTO,
            @RequestParam("deleteRow") int row,
            Model model) {
        if (companyDTO.getListCoverage() != null && !companyDTO.getListCoverage().isEmpty()) {
            companyDTO.getListCoverage().remove(row);
        }

        model.addAttribute("listCoverageExisting", coverageService.getAllCoverage());
        model.addAttribute("companyDTO", companyDTO);
        return "form-update-company";
    }

    @GetMapping("/company/{id}/delete")
    public String deleteCompany(@PathVariable("id") UUID id, Model model) {
        var company = companyService.getCompanyById(id);
        companyService.deleteCompany(company);

        model.addAttribute("responseMessage", String.format("Berhasil menghapus Data Company %s", company.getName()));

        return "response-company";

    }

}
