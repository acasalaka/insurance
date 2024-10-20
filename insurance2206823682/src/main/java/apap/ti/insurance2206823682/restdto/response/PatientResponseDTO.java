package apap.ti.insurance2206823682.restdto.response;

import java.util.Date;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientResponseDTO {

    private UUID id;
    private String nik;
    private String name;
    private int gender;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String email;
    private int pClass;


    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    private Date updatedAt;

}
