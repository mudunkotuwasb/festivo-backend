package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.VendorCategory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface VendorCategoryRepository extends JpaRepository<VendorCategory, UUID> {

    boolean existsByVendorCategoryNameIgnoreCase(String vendorCategoryName);

}
