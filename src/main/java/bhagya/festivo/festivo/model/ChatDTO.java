package bhagya.festivo.festivo.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ChatDTO {

    private UUID chatId;

    @NotNull
    private LocalDateTime createdAt;

    private UUID vendorId;

    private UUID customerId;

}
