package apap.ti.insurance2206823682.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String nik;

    @Column(nullable = false)
    private String name;

    private int gender;

    @Temporal(TemporalType.DATE)
    private Date birthDate;

    @Column(nullable = false)
    private String email;

    private int pClass;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Policy> listPolicy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

}
