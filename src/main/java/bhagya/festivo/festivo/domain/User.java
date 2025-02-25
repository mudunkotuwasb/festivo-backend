package bhagya.festivo.festivo.domain;

import bhagya.festivo.festivo.model.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class User {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID userId;

    @Column(nullable = false, length = 30)
    private String userName;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, unique = true, length = 10)
    private String phoneNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role userRole;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id_id", unique = true)
    private Vendor vendorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id_id", unique = true)
    private Customer customerId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id_id", unique = true)
    private Admin adminId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id_id", unique = true)
    private MessageEntity senderId;

}
