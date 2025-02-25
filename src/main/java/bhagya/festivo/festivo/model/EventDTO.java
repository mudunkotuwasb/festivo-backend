package bhagya.festivo.festivo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EventDTO {

    private UUID eventId;

    @NotNull
    @Size(max = 100)
    private String eventName;

    @NotNull
    private LocalDate eventDate;

    @NotNull
    @Size(max = 50)
    private String eventLocation;

    private UUID userId;

}
