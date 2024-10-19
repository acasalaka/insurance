package apap.ti.insurance2206823682.model;

import jakarta.persistence.*;
import java.util.Date;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "policy")
public class Policy {

    @Id
    @Column(nullable = false)
    private String id;
    
    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
    
    private int status;
    
    @Temporal(TemporalType.DATE)
    private Date expiryDate;
    
    private long totalCoverage;

    private long totalCovered;
    
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

