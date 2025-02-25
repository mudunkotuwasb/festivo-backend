package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Payment;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Payment findFirstByUserId(User user);

    Payment findFirstByVendorId(Vendor vendor);

    Payment findFirstByBookingId(Booking booking);

}
