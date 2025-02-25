package bhagya.festivo.festivo.repos;

import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.MessageEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MessageEntityRepository extends JpaRepository<MessageEntity, UUID> {

    MessageEntity findFirstByChatId(Chat chat);

}
