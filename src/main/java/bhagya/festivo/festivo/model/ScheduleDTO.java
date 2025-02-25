package bhagya.festivo.festivo.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ScheduleDTO {

    private UUID scheduleId;

    @NotNull
    private LocalDate sceduleDate;

    @NotNull
    @Schema(type = "string", example = "18:30")
    private LocalTime scheduleTimeSlot;

    @NotNull
    private Availability scheduleStatus;

    private UUID vendorId;

    @ScheduleBookingIdUnique
    private UUID bookingId;

}
