package bhagya.festivo.festivo.model;

import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendorDTO {

    private UUID vendorId;
    private Double ratings;
    private String profileDetails;
    private UUID categoryId;

}
