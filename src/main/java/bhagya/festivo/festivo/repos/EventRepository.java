package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Event;
import bhagya.festivo.festivo.domain.User;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EventRepository extends JpaRepository<Event, UUID> {

    Event findFirstByUserId(User user);

}
