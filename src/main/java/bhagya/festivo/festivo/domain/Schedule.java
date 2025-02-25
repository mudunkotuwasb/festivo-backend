package bhagya.festivo.festivo.domain;

import bhagya.festivo.festivo.model.Availability;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Schedule {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID scheduleId;

    @Column(nullable = false)
    private LocalDate sceduleDate;

    @Column(nullable = false)
    private LocalTime scheduleTimeSlot;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Availability scheduleStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id_id")
    private Vendor vendorId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id_id", unique = true)
    private Booking bookingId;

}
