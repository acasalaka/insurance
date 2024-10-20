package apap.ti.insurance2206823682.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import apap.ti.insurance2206823682.dto.request.UpgradePatientRequestDTO;
import apap.ti.insurance2206823682.model.Patient;
import apap.ti.insurance2206823682.service.PatientService;
import jakarta.validation.Valid;

@Controller
public class PatientController {

    @Autowired
    private PatientService patientService;

    @GetMapping("/patient/{id}/upgrade-class")
    public String upgradeClassPolicy(@PathVariable("id") UUID id, Model model) {
        Patient searchedPatient = patientService.getPatientById(id);
        var upgradeDTO = new UpgradePatientRequestDTO();
        upgradeDTO.setId(id);
        upgradeDTO.setPClass(searchedPatient.getPClass());

        model.addAttribute("upgradeDTO", upgradeDTO);
        model.addAttribute("patient", searchedPatient);
        model.addAttribute("yearMonthDay", patientService.getYearMonthDaysFromBirthDate(searchedPatient));

        return "form-upgrade-class";
    }

    @PostMapping("/patient/upgrade-class")
    public String upgradeClassPolicy(@Valid @ModelAttribute UpgradePatientRequestDTO upgradeDTO, BindingResult bindingResult, Model model) {
        Patient searchedPatient = patientService.getPatientById(upgradeDTO.getId());
        if (bindingResult.hasErrors()) {
            model.addAttribute("upgradeDTO", upgradeDTO);
            model.addAttribute("patient", searchedPatient);
            model.addAttribute("yearMonthDay", patientService.getYearMonthDaysFromBirthDate(searchedPatient));
        }
        searchedPatient.setPClass(upgradeDTO.getPClass());
        patientService.updateClassPatient(searchedPatient);

        model.addAttribute("responseMessage", String.format("Class pasien %s berhasil di-upgrade menjadi Class %d.", searchedPatient.getName(), upgradeDTO.getPClass()));
        return "response-patient";
    }

}
