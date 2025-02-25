package bhagya.festivo.festivo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Vendor {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID vendorId;

    @Column
    private Double ratings;

    @Column(columnDefinition = "longtext")
    private String profileDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id_id")
    private VendorCategory categoryId;

}
