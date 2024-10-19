package apap.ti.insurance2206823682.dto.request;

import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import apap.ti.insurance2206823682.model.Company;
import apap.ti.insurance2206823682.model.Policy;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddPatientPolicyRequestDTO {
    @NotBlank(message = "NIK pasien tidak boleh kosong")
    @Size(min = 3, max = 100, message = "NIK pasien harus antara 3 dan 100 karakter")
    private String nik;

    @NotBlank(message = "Nama pasien tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Nama pasien harus antara 3 dan 100 karakter")
    private String name;

    @NotNull(message = "Gender tidak boleh kosong")
    private Integer gender;  // Use Integer instead of int for null validation
    
    @NotNull(message = "Tanggal lahir tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date birthDate;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotNull(message = "Kelas tidak boleh kosong")
    private Integer pClass;  // Use Integer instead of int for null validation

    @NotNull(message = "Tanggal expired tidak boleh kosong")
    @DateTimeFormat(pattern = "yyyy-MM-dd")    
    private Date expiryDate;

    @NotNull(message = "Company tidak boleh kosong")
    private Company company;

    // @NotNull
    // private Boolean isCoverageLoaded;

}