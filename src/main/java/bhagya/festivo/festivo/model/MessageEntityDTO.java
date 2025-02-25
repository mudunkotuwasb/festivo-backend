package bhagya.festivo.festivo.model;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class MessageEntityDTO {

    private UUID messageId;

    @NotNull
    private String messageText;

    private LocalDateTime sentAt;

    private UUID chatId;

}
