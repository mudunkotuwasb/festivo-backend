package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Event;
import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.Service;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookingRepository extends JpaRepository<Booking, UUID> {

    Booking findFirstByUserId(User user);

    Booking findFirstByVendorId(Vendor vendor);

    Booking findFirstByEventId(Event event);

    Booking findFirstByServiceId(Service service);

    Booking findFirstByReviewId(Review review);

    boolean existsByReviewIdReviewId(UUID reviewId);

}
