package bhagya.festivo.festivo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VendorCategoryDTO {

    private UUID vendorCategoryId;

    @NotNull
    @Size(max = 50)
    @VendorCategoryVendorCategoryNameUnique
    private String vendorCategoryName;

}
