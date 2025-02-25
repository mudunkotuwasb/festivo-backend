package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Customer;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CustomerRepository extends JpaRepository<Customer, UUID> {
}
