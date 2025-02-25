package bhagya.festivo.festivo.domain;

import bhagya.festivo.festivo.model.BookingStatus;
import bhagya.festivo.festivo.model.PayementStatus;
import bhagya.festivo.festivo.model.RefundStatus;
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
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;


@Entity
@Getter
@Setter
public class Booking {

    @Id
    @Column(nullable = false, updatable = false, columnDefinition = "char(36)")
    @GeneratedValue
    @UuidGenerator
    private UUID bookingId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;

    @Column(precision = 10, scale = 2)
    private BigDecimal advancePayment;

    @Column(precision = 10, scale = 2)
    private BigDecimal remainingPayment;

    @Column
    @Enumerated(EnumType.STRING)
    private RefundStatus refundStatus;

    @Column
    @Enumerated(EnumType.STRING)
    private PayementStatus paymentStatus;

    @Column
    private LocalDateTime createdAT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_id")
    private User userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vendor_id_id")
    private Vendor vendorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id_id")
    private Event eventId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id_id")
    private Service serviceId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id_id", unique = true)
    private Review reviewId;

}
