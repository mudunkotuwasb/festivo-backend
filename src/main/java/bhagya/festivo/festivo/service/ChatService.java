package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.Customer;
import bhagya.festivo.festivo.domain.MessageEntity;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.ChatDTO;
import bhagya.festivo.festivo.repos.ChatRepository;
import bhagya.festivo.festivo.repos.CustomerRepository;
import bhagya.festivo.festivo.repos.MessageEntityRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ChatService {

    private final ChatRepository chatRepository;
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;
    private final MessageEntityRepository messageEntityRepository;

    public ChatService(final ChatRepository chatRepository, final VendorRepository vendorRepository,
            final CustomerRepository customerRepository,
            final MessageEntityRepository messageEntityRepository) {
        this.chatRepository = chatRepository;
        this.vendorRepository = vendorRepository;
        this.customerRepository = customerRepository;
        this.messageEntityRepository = messageEntityRepository;
    }

    public List<ChatDTO> findAll() {
        final List<Chat> chats = chatRepository.findAll(Sort.by("chatId"));
        return chats.stream()
                .map(chat -> mapToDTO(chat, new ChatDTO()))
                .toList();
    }

    public ChatDTO get(final UUID chatId) {
        return chatRepository.findById(chatId)
                .map(chat -> mapToDTO(chat, new ChatDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ChatDTO chatDTO) {
        final Chat chat = new Chat();
        mapToEntity(chatDTO, chat);
        return chatRepository.save(chat).getChatId();
    }

    public void update(final UUID chatId, final ChatDTO chatDTO) {
        final Chat chat = chatRepository.findById(chatId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(chatDTO, chat);
        chatRepository.save(chat);
    }

    public void delete(final UUID chatId) {
        chatRepository.deleteById(chatId);
    }

    private ChatDTO mapToDTO(final Chat chat, final ChatDTO chatDTO) {
        chatDTO.setChatId(chat.getChatId());
        chatDTO.setCreatedAt(chat.getCreatedAt());
        chatDTO.setVendorId(chat.getVendorId() == null ? null : chat.getVendorId().getVendorId());
        chatDTO.setCustomerId(chat.getCustomerId() == null ? null : chat.getCustomerId().getCustomerId());
        return chatDTO;
    }

    private Chat mapToEntity(final ChatDTO chatDTO, final Chat chat) {
        chat.setCreatedAt(chatDTO.getCreatedAt());
        final Vendor vendorId = chatDTO.getVendorId() == null ? null : vendorRepository.findById(chatDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        chat.setVendorId(vendorId);
        final Customer customerId = chatDTO.getCustomerId() == null ? null : customerRepository.findById(chatDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("customerId not found"));
        chat.setCustomerId(customerId);
        return chat;
    }

    public ReferencedWarning getReferencedWarning(final UUID chatId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Chat chat = chatRepository.findById(chatId)
                .orElseThrow(NotFoundException::new);
        final MessageEntity chatIdMessageEntity = messageEntityRepository.findFirstByChatId(chat);
        if (chatIdMessageEntity != null) {
            referencedWarning.setKey("chat.messageEntity.chatId.referenced");
            referencedWarning.addParam(chatIdMessageEntity.getMessageId());
            return referencedWarning;
        }
        return null;
    }

}
