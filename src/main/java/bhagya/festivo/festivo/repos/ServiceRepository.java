package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Service;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ServiceRepository extends JpaRepository<Service, UUID> {

    Service findFirstByVendorId(Vendor vendor);

}
