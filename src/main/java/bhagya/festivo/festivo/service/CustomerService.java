package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.Customer;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.model.CustomerDTO;
import bhagya.festivo.festivo.repos.ChatRepository;
import bhagya.festivo.festivo.repos.CustomerRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    public CustomerService(final CustomerRepository customerRepository,
            final UserRepository userRepository, final ChatRepository chatRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
        this.chatRepository = chatRepository;
    }

    public List<CustomerDTO> findAll() {
        final List<Customer> customers = customerRepository.findAll(Sort.by("customerId"));
        return customers.stream()
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .toList();
    }

    public CustomerDTO get(final UUID customerId) {
        return customerRepository.findById(customerId)
                .map(customer -> mapToDTO(customer, new CustomerDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final CustomerDTO customerDTO) {
        final Customer customer = new Customer();
        mapToEntity(customerDTO, customer);
        return customerRepository.save(customer).getCustomerId();
    }

    public void update(final UUID customerId, final CustomerDTO customerDTO) {
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDTO, customer);
        customerRepository.save(customer);
    }

    public void delete(final UUID customerId) {
        customerRepository.deleteById(customerId);
    }

    private CustomerDTO mapToDTO(final Customer customer, final CustomerDTO customerDTO) {
        customerDTO.setCustomerId(customer.getCustomerId());
        return customerDTO;
    }

    private Customer mapToEntity(final CustomerDTO customerDTO, final Customer customer) {
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final UUID customerId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(customerId)
                .orElseThrow(NotFoundException::new);
        final User customerIdUser = userRepository.findFirstByCustomerId(customer);
        if (customerIdUser != null) {
            referencedWarning.setKey("customer.user.customerId.referenced");
            referencedWarning.addParam(customerIdUser.getUserId());
            return referencedWarning;
        }
        final Chat customerIdChat = chatRepository.findFirstByCustomerId(customer);
        if (customerIdChat != null) {
            referencedWarning.setKey("customer.chat.customerId.referenced");
            referencedWarning.addParam(customerIdChat.getChatId());
            return referencedWarning;
        }
        return null;
    }

}
