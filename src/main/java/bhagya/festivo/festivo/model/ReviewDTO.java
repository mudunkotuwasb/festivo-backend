package bhagya.festivo.festivo.model;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReviewDTO {

    private UUID reviewId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private UUID userId;
    private UUID vendorId;

}
