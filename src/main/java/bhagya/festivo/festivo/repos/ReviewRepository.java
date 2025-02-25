package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Review findFirstByUserId(User user);

    Review findFirstByVendorId(Vendor vendor);

}
