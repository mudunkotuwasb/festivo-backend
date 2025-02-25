package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.MessageEntity;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.model.MessageEntityDTO;
import bhagya.festivo.festivo.repos.ChatRepository;
import bhagya.festivo.festivo.repos.MessageEntityRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MessageEntityService {

    private final MessageEntityRepository messageEntityRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    public MessageEntityService(final MessageEntityRepository messageEntityRepository,
            final ChatRepository chatRepository, final UserRepository userRepository) {
        this.messageEntityRepository = messageEntityRepository;
        this.chatRepository = chatRepository;
        this.userRepository = userRepository;
    }

    public List<MessageEntityDTO> findAll() {
        final List<MessageEntity> messageEntities = messageEntityRepository.findAll(Sort.by("messageId"));
        return messageEntities.stream()
                .map(messageEntity -> mapToDTO(messageEntity, new MessageEntityDTO()))
                .toList();
    }

    public MessageEntityDTO get(final UUID messageId) {
        return messageEntityRepository.findById(messageId)
                .map(messageEntity -> mapToDTO(messageEntity, new MessageEntityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final MessageEntityDTO messageEntityDTO) {
        final MessageEntity messageEntity = new MessageEntity();
        mapToEntity(messageEntityDTO, messageEntity);
        return messageEntityRepository.save(messageEntity).getMessageId();
    }

    public void update(final UUID messageId, final MessageEntityDTO messageEntityDTO) {
        final MessageEntity messageEntity = messageEntityRepository.findById(messageId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(messageEntityDTO, messageEntity);
        messageEntityRepository.save(messageEntity);
    }

    public void delete(final UUID messageId) {
        messageEntityRepository.deleteById(messageId);
    }

    private MessageEntityDTO mapToDTO(final MessageEntity messageEntity,
            final MessageEntityDTO messageEntityDTO) {
        messageEntityDTO.setMessageId(messageEntity.getMessageId());
        messageEntityDTO.setMessageText(messageEntity.getMessageText());
        messageEntityDTO.setSentAt(messageEntity.getSentAt());
        messageEntityDTO.setChatId(messageEntity.getChatId() == null ? null : messageEntity.getChatId().getChatId());
        return messageEntityDTO;
    }

    private MessageEntity mapToEntity(final MessageEntityDTO messageEntityDTO,
            final MessageEntity messageEntity) {
        messageEntity.setMessageText(messageEntityDTO.getMessageText());
        messageEntity.setSentAt(messageEntityDTO.getSentAt());
        final Chat chatId = messageEntityDTO.getChatId() == null ? null : chatRepository.findById(messageEntityDTO.getChatId())
                .orElseThrow(() -> new NotFoundException("chatId not found"));
        messageEntity.setChatId(chatId);
        return messageEntity;
    }

    public ReferencedWarning getReferencedWarning(final UUID messageId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final MessageEntity messageEntity = messageEntityRepository.findById(messageId)
                .orElseThrow(NotFoundException::new);
        final User senderIdUser = userRepository.findFirstBySenderId(messageEntity);
        if (senderIdUser != null) {
            referencedWarning.setKey("messageEntity.user.senderId.referenced");
            referencedWarning.addParam(senderIdUser.getUserId());
            return referencedWarning;
        }
        return null;
    }

}
