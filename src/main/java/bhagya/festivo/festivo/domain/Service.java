package bhagya.festivo.festivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Service {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID serviceId;

    @Column(nullable = false, length = 100)
    private String serviceName;

    @Column(columnDefinition = "longtext")
    private String serviceDescription;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal servicePrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id_id")
    private Vendor vendorId;

}
