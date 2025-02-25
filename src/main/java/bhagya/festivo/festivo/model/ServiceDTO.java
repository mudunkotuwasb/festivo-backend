package bhagya.festivo.festivo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ServiceDTO {

    private UUID serviceId;

    @NotNull
    @Size(max = 100)
    private String serviceName;

    private String serviceDescription;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "80.08")
    private BigDecimal servicePrice;

    private UUID vendorId;

}
