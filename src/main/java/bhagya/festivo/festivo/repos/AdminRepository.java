package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Admin;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, UUID> {
}
