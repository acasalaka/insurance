package apap.ti.insurance2206823682.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "coverage")
public class Coverage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private long coverageAmount;

    @ManyToMany(mappedBy = "listCoverage")
    private List<Company> listCompany;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = new Date();
        this.updatedAt = new Date();
    }

    public String getFormattedCoverageAmount() {
        Locale indonesianLocale = new Locale("id", "ID");
        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(indonesianLocale);
        return currencyFormatter.format(coverageAmount).replace("Rp", "Rp").replace(",00", ",00");
    }
}
