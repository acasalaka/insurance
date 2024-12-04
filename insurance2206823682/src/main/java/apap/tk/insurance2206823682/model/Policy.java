package apap.tk.insurance2206823682.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "policy")
@Entity
public class Policy {

    @Id
    private String id;

    // @Column(nullable = false)
    private UUID companyId;

    // @Column(nullable = false)
    private UUID patientId;

    private int status;
    private Date expiryDate;
    private long totalCoverage;
    private long totalCovered;

    @ManyToMany
    @JoinTable(
        name = "policy_coverage",  // Name of the join table
        joinColumns = @JoinColumn(name = "policy_id"),  // Foreign key for the Policy
        inverseJoinColumns = @JoinColumn(name = "coverage_id")  // Foreign key for the Coverage
    )
    private List<Coverage> listCoverage;

    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;


    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;

}