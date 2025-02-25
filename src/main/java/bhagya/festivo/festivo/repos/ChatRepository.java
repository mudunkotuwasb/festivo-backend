package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.Customer;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRepository extends JpaRepository<Chat, UUID> {

    Chat findFirstByVendorId(Vendor vendor);

    Chat findFirstByCustomerId(Customer customer);

}
