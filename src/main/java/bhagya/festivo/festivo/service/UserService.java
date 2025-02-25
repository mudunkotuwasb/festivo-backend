package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Admin;
import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Customer;
import bhagya.festivo.festivo.domain.Event;
import bhagya.festivo.festivo.domain.MessageEntity;
import bhagya.festivo.festivo.domain.Payment;
import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.UserDTO;
import bhagya.festivo.festivo.repos.AdminRepository;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.CustomerRepository;
import bhagya.festivo.festivo.repos.EventRepository;
import bhagya.festivo.festivo.repos.MessageEntityRepository;
import bhagya.festivo.festivo.repos.PaymentRepository;
import bhagya.festivo.festivo.repos.ReviewRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final CustomerRepository customerRepository;
    private final AdminRepository adminRepository;
    private final MessageEntityRepository messageEntityRepository;
    private final EventRepository eventRepository;
    private final BookingRepository bookingRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;

    public UserService(final UserRepository userRepository, final VendorRepository vendorRepository,
            final CustomerRepository customerRepository, final AdminRepository adminRepository,
            final MessageEntityRepository messageEntityRepository,
            final EventRepository eventRepository, final BookingRepository bookingRepository,
            final ReviewRepository reviewRepository, final PaymentRepository paymentRepository) {
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.customerRepository = customerRepository;
        this.adminRepository = adminRepository;
        this.messageEntityRepository = messageEntityRepository;
        this.eventRepository = eventRepository;
        this.bookingRepository = bookingRepository;
        this.reviewRepository = reviewRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final UUID userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUserName(user.getUserName());
        userDTO.setEmail(user.getEmail());
        userDTO.setPhoneNumber(user.getPhoneNumber());
        userDTO.setUserRole(user.getUserRole());
        userDTO.setVendorId(user.getVendorId() == null ? null : user.getVendorId().getVendorId());
        userDTO.setCustomerId(user.getCustomerId() == null ? null : user.getCustomerId().getCustomerId());
        userDTO.setAdminId(user.getAdminId() == null ? null : user.getAdminId().getAdminId());
        userDTO.setSenderId(user.getSenderId() == null ? null : user.getSenderId().getMessageId());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUserName(userDTO.getUserName());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setUserRole(userDTO.getUserRole());

        final Vendor vendorId = userDTO.getVendorId() == null ? null : vendorRepository.findById(userDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        user.setVendorId(vendorId);
        final Customer customerId = userDTO.getCustomerId() == null ? null : customerRepository.findById(userDTO.getCustomerId())
                .orElseThrow(() -> new NotFoundException("customerId not found"));
        user.setCustomerId(customerId);
        final Admin adminId = userDTO.getAdminId() == null ? null : adminRepository.findById(userDTO.getAdminId())
                .orElseThrow(() -> new NotFoundException("adminId not found"));
        user.setAdminId(adminId);
        final MessageEntity senderId = userDTO.getSenderId() == null ? null : messageEntityRepository.findById(userDTO.getSenderId())
                .orElseThrow(() -> new NotFoundException("senderId not found"));
        user.setSenderId(senderId);
        return user;
    }

    public boolean emailExists(final String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    public boolean phoneNumberExists(final String phoneNumber) {
        return userRepository.existsByPhoneNumberIgnoreCase(phoneNumber);
    }

    public boolean vendorIdExists(final UUID vendorId) {
        return userRepository.existsByVendorIdVendorId(vendorId);
    }

    public boolean customerIdExists(final UUID customerId) {
        return userRepository.existsByCustomerIdCustomerId(customerId);
    }

    public boolean adminIdExists(final UUID adminId) {
        return userRepository.existsByAdminIdAdminId(adminId);
    }

    public boolean senderIdExists(final UUID messageId) {
        return userRepository.existsBySenderIdMessageId(messageId);
    }

    public ReferencedWarning getReferencedWarning(final UUID userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        final Event userIdEvent = eventRepository.findFirstByUserId(user);
        if (userIdEvent != null) {
            referencedWarning.setKey("user.event.userId.referenced");
            referencedWarning.addParam(userIdEvent.getEventId());
            return referencedWarning;
        }
        final Booking userIdBooking = bookingRepository.findFirstByUserId(user);
        if (userIdBooking != null) {
            referencedWarning.setKey("user.booking.userId.referenced");
            referencedWarning.addParam(userIdBooking.getBookingId());
            return referencedWarning;
        }
        final Review userIdReview = reviewRepository.findFirstByUserId(user);
        if (userIdReview != null) {
            referencedWarning.setKey("user.review.userId.referenced");
            referencedWarning.addParam(userIdReview.getReviewId());
            return referencedWarning;
        }
        final Payment userIdPayment = paymentRepository.findFirstByUserId(user);
        if (userIdPayment != null) {
            referencedWarning.setKey("user.payment.userId.referenced");
            referencedWarning.addParam(userIdPayment.getPaymentId());
            return referencedWarning;
        }
        return null;
    }

}
