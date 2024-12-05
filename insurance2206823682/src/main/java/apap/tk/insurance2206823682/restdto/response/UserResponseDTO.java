package apap.tk.insurance2206823682.restdto.response;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserResponseDTO {
    private UUID id;
    private String name;
    private String username;
    private String password;
    private String email;
    private String gender;
    private String role;
    private String nik;
    private String birthPlace;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private int pClass;
    private int specialist;
    private int yearsOfExperience;
    private Long fee;
    private List<Integer> schedules;


    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, timezone="Asia/Jakarta")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private boolean isDeleted;
}
