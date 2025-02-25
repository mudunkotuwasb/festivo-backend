package bhagya.festivo.festivo.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserDTO {

    private UUID userId;

    @NotNull
    @Size(max = 30)
    private String userName;

    @NotNull
    @Size(max = 50)
    @UserEmailUnique
    private String email;

    @NotNull
    @Size(max = 10)
    @UserPhoneNumberUnique
    private String phoneNumber;

    @NotNull
    private Role userRole;

    @UserVendorIdUnique
    private UUID vendorId;

    @UserCustomerIdUnique
    private UUID customerId;

    @UserAdminIdUnique
    private UUID adminId;

    @UserSenderIdUnique
    private UUID senderId;

}
