package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Schedule;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ScheduleRepository extends JpaRepository<Schedule, UUID> {

    Schedule findFirstByVendorId(Vendor vendor);

    Schedule findFirstByBookingId(Booking booking);

    boolean existsByBookingIdBookingId(UUID bookingId);

}
