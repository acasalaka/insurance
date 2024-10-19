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
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company")
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    
    @Column(nullable = false)
    private String name;
    
    private String contact;
    
    @Column(nullable = false)
    private String email;
    
    private String address;
    
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Policy> listPolicy;
    
    @ManyToMany
    @JoinTable(
        name = "company_coverage",
        joinColumns = @JoinColumn(name = "company_id"),
        inverseJoinColumns = @JoinColumn(name = "coverage_id"))
    private List<Coverage> listCoverage;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @Temporal(TemporalType.TIMESTAMP)
    private Date deletedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

}
