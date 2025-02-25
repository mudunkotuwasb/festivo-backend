package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Event;
import bhagya.festivo.festivo.domain.Payment;
import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.Schedule;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.BookingDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.EventRepository;
import bhagya.festivo.festivo.repos.PaymentRepository;
import bhagya.festivo.festivo.repos.ReviewRepository;
import bhagya.festivo.festivo.repos.ScheduleRepository;
import bhagya.festivo.festivo.repos.ServiceRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final EventRepository eventRepository;
    private final ServiceRepository serviceRepository;
    private final ReviewRepository reviewRepository;
    private final PaymentRepository paymentRepository;
    private final ScheduleRepository scheduleRepository;

    public BookingService(final BookingRepository bookingRepository,
            final UserRepository userRepository, final VendorRepository vendorRepository,
            final EventRepository eventRepository, final ServiceRepository serviceRepository,
            final ReviewRepository reviewRepository, final PaymentRepository paymentRepository,
            final ScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.eventRepository = eventRepository;
        this.serviceRepository = serviceRepository;
        this.reviewRepository = reviewRepository;
        this.paymentRepository = paymentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public List<BookingDTO> findAll() {
        final List<Booking> bookings = bookingRepository.findAll(Sort.by("bookingId"));
        return bookings.stream()
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .toList();
    }

    public BookingDTO get(final UUID bookingId) {
        return bookingRepository.findById(bookingId)
                .map(booking -> mapToDTO(booking, new BookingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final BookingDTO bookingDTO) {
        final Booking booking = new Booking();
        mapToEntity(bookingDTO, booking);
        return bookingRepository.save(booking).getBookingId();
    }

    public void update(final UUID bookingId, final BookingDTO bookingDTO) {
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(bookingDTO, booking);
        bookingRepository.save(booking);
    }

    public void delete(final UUID bookingId) {
        bookingRepository.deleteById(bookingId);
    }

    private BookingDTO mapToDTO(final Booking booking, final BookingDTO bookingDTO) {
        bookingDTO.setBookingId(booking.getBookingId());
        bookingDTO.setBookingStatus(booking.getBookingStatus());
        bookingDTO.setAdvancePayment(booking.getAdvancePayment());
        bookingDTO.setRemainingPayment(booking.getRemainingPayment());
        bookingDTO.setRefundStatus(booking.getRefundStatus());
        bookingDTO.setPaymentStatus(booking.getPaymentStatus());
        bookingDTO.setCreatedAT(booking.getCreatedAT());
        bookingDTO.setUserId(booking.getUserId() == null ? null : booking.getUserId().getUserId());
        bookingDTO.setVendorId(booking.getVendorId() == null ? null : booking.getVendorId().getVendorId());
        bookingDTO.setEventId(booking.getEventId() == null ? null : booking.getEventId().getEventId());
        bookingDTO.setServiceId(booking.getServiceId() == null ? null : booking.getServiceId().getServiceId());
        bookingDTO.setReviewId(booking.getReviewId() == null ? null : booking.getReviewId().getReviewId());
        return bookingDTO;
    }

    private Booking mapToEntity(final BookingDTO bookingDTO, final Booking booking) {
        booking.setBookingStatus(bookingDTO.getBookingStatus());
        booking.setAdvancePayment(bookingDTO.getAdvancePayment());
        booking.setRemainingPayment(bookingDTO.getRemainingPayment());
        booking.setRefundStatus(bookingDTO.getRefundStatus());
        booking.setPaymentStatus(bookingDTO.getPaymentStatus());
        booking.setCreatedAT(bookingDTO.getCreatedAT());
        final User userId = bookingDTO.getUserId() == null ? null : userRepository.findById(bookingDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        booking.setUserId(userId);
        final Vendor vendorId = bookingDTO.getVendorId() == null ? null : vendorRepository.findById(bookingDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        booking.setVendorId(vendorId);
        final Event eventId = bookingDTO.getEventId() == null ? null : eventRepository.findById(bookingDTO.getEventId())
                .orElseThrow(() -> new NotFoundException("eventId not found"));
        booking.setEventId(eventId);
        final bhagya.festivo.festivo.domain.Service serviceId = bookingDTO.getServiceId() == null ? null : serviceRepository.findById(bookingDTO.getServiceId())
                .orElseThrow(() -> new NotFoundException("serviceId not found"));
        booking.setServiceId(serviceId);
        final Review reviewId = bookingDTO.getReviewId() == null ? null : reviewRepository.findById(bookingDTO.getReviewId())
                .orElseThrow(() -> new NotFoundException("reviewId not found"));
        booking.setReviewId(reviewId);
        return booking;
    }

    public boolean reviewIdExists(final UUID reviewId) {
        return bookingRepository.existsByReviewIdReviewId(reviewId);
    }

    public ReferencedWarning getReferencedWarning(final UUID bookingId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(NotFoundException::new);
        final Payment bookingIdPayment = paymentRepository.findFirstByBookingId(booking);
        if (bookingIdPayment != null) {
            referencedWarning.setKey("booking.payment.bookingId.referenced");
            referencedWarning.addParam(bookingIdPayment.getPaymentId());
            return referencedWarning;
        }
        final Schedule bookingIdSchedule = scheduleRepository.findFirstByBookingId(booking);
        if (bookingIdSchedule != null) {
            referencedWarning.setKey("booking.schedule.bookingId.referenced");
            referencedWarning.addParam(bookingIdSchedule.getScheduleId());
            return referencedWarning;
        }
        return null;
    }

}
