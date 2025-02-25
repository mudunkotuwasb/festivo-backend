package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Admin;
import bhagya.festivo.festivo.domain.Customer;
import bhagya.festivo.festivo.domain.MessageEntity;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, UUID> {

    User findFirstByVendorId(Vendor vendor);

    User findFirstByCustomerId(Customer customer);

    User findFirstByAdminId(Admin admin);

    User findFirstBySenderId(MessageEntity messageEntity);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumberIgnoreCase(String phoneNumber);

    boolean existsByVendorIdVendorId(UUID vendorId);

    boolean existsByCustomerIdCustomerId(UUID customerId);

    boolean existsByAdminIdAdminId(UUID adminId);

    boolean existsBySenderIdMessageId(UUID messageId);

}
