package bhagya.festivo.festivo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class BookingDTO {

    private UUID bookingId;

    @NotNull
    private BookingStatus bookingStatus;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "44.08")
    private BigDecimal advancePayment;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "28.08")
    private BigDecimal remainingPayment;

    private RefundStatus refundStatus;

    private PayementStatus paymentStatus;

    private LocalDateTime createdAT;

    private UUID userId;

    private UUID vendorId;

    private UUID eventId;

    private UUID serviceId;

    @BookingReviewIdUnique
    private UUID reviewId;

}
