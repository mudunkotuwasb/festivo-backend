package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.domain.VendorCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendorRepository extends JpaRepository<Vendor, UUID> {

    Vendor findFirstByCategoryId(VendorCategory vendorCategory);

}
