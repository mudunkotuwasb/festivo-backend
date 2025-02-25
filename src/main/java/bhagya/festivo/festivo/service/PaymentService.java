package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Payment;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.PaymentDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.PaymentRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;

    public PaymentService(final PaymentRepository paymentRepository,
            final UserRepository userRepository, final VendorRepository vendorRepository,
            final BookingRepository bookingRepository) {
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("paymentId"));
        return payments.stream()
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .toList();
    }

    public PaymentDTO get(final UUID paymentId) {
        return paymentRepository.findById(paymentId)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getPaymentId();
    }

    public void update(final UUID paymentId, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    public void delete(final UUID paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    private PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setPaymentId(payment.getPaymentId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setPayementStatus(payment.getPayementStatus());
        paymentDTO.setTarnsactionDate(payment.getTarnsactionDate());
        paymentDTO.setTransactionId(payment.getTransactionId());
        paymentDTO.setUserId(payment.getUserId() == null ? null : payment.getUserId().getUserId());
        paymentDTO.setVendorId(payment.getVendorId() == null ? null : payment.getVendorId().getVendorId());
        paymentDTO.setBookingId(payment.getBookingId() == null ? null : payment.getBookingId().getBookingId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setAmount(paymentDTO.getAmount());
        payment.setPayementStatus(paymentDTO.getPayementStatus());
        payment.setTarnsactionDate(paymentDTO.getTarnsactionDate());
        payment.setTransactionId(paymentDTO.getTransactionId());
        final User userId = paymentDTO.getUserId() == null ? null : userRepository.findById(paymentDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        payment.setUserId(userId);
        final Vendor vendorId = paymentDTO.getVendorId() == null ? null : vendorRepository.findById(paymentDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        payment.setVendorId(vendorId);
        final Booking bookingId = paymentDTO.getBookingId() == null ? null : bookingRepository.findById(paymentDTO.getBookingId())
                .orElseThrow(() -> new NotFoundException("bookingId not found"));
        payment.setBookingId(bookingId);
        return payment;
    }

}
