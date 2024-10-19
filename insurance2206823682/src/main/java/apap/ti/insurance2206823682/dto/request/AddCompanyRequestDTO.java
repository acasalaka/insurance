package apap.ti.insurance2206823682.dto.request;

import java.util.List;

import apap.ti.insurance2206823682.model.Coverage;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class AddCompanyRequestDTO {
    @NotBlank(message = "Nama perusahaan tidak boleh kosong")
    @Size(min = 3, max = 100, message = "Nama perusahaan harus antara 3 dan 100 karakter")
    private String name;

    @NotBlank(message = "Email tidak boleh kosong")
    @Email(message = "Format email tidak valid")
    private String email;

    @NotBlank(message = "Kontak tidak boleh kosong")
    private String contact;

    @NotBlank(message = "Alamat tidak boleh kosong")
    private String address;

    @NotEmpty(message = "Setidaknya satu coverage harus dipilih")
    private List<Coverage> listCoverage;
    
}
