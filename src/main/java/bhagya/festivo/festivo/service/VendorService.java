package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Chat;
import bhagya.festivo.festivo.domain.Payment;
import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.Schedule;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.domain.VendorCategory;
import bhagya.festivo.festivo.model.VendorDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.ChatRepository;
import bhagya.festivo.festivo.repos.PaymentRepository;
import bhagya.festivo.festivo.repos.ReviewRepository;
import bhagya.festivo.festivo.repos.ScheduleRepository;
import bhagya.festivo.festivo.repos.ServiceRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.repos.VendorCategoryRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VendorService {

    private final VendorRepository vendorRepository;
    private final VendorCategoryRepository vendorCategoryRepository;
    private final UserRepository userRepository;
    private final ServiceRepository serviceRepository;
    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final PaymentRepository paymentRepository;
    private final ChatRepository chatRepository;
    private final ReviewRepository reviewRepository;

    public VendorService(final VendorRepository vendorRepository,
            final VendorCategoryRepository vendorCategoryRepository,
            final UserRepository userRepository, final ServiceRepository serviceRepository,
            final BookingRepository bookingRepository, final ScheduleRepository scheduleRepository,
            final PaymentRepository paymentRepository, final ChatRepository chatRepository,
            final ReviewRepository reviewRepository) {
        this.vendorRepository = vendorRepository;
        this.vendorCategoryRepository = vendorCategoryRepository;
        this.userRepository = userRepository;
        this.serviceRepository = serviceRepository;
        this.bookingRepository = bookingRepository;
        this.scheduleRepository = scheduleRepository;
        this.paymentRepository = paymentRepository;
        this.chatRepository = chatRepository;
        this.reviewRepository = reviewRepository;
    }

    public List<VendorDTO> findAll() {
        final List<Vendor> vendors = vendorRepository.findAll(Sort.by("vendorId"));
        return vendors.stream()
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .toList();
    }

    public VendorDTO get(final UUID vendorId) {
        return vendorRepository.findById(vendorId)
                .map(vendor -> mapToDTO(vendor, new VendorDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final VendorDTO vendorDTO) {
        final Vendor vendor = new Vendor();
        mapToEntity(vendorDTO, vendor);
        return vendorRepository.save(vendor).getVendorId();
    }

    public void update(final UUID vendorId, final VendorDTO vendorDTO) {
        final Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorDTO, vendor);
        vendorRepository.save(vendor);
    }

    public void delete(final UUID vendorId) {
        vendorRepository.deleteById(vendorId);
    }

    private VendorDTO mapToDTO(final Vendor vendor, final VendorDTO vendorDTO) {
        vendorDTO.setVendorId(vendor.getVendorId());
        vendorDTO.setRatings(vendor.getRatings());
        vendorDTO.setProfileDetails(vendor.getProfileDetails());
        vendorDTO.setCategoryId(vendor.getCategoryId() == null ? null : vendor.getCategoryId().getVendorCategoryId());
        return vendorDTO;
    }

    private Vendor mapToEntity(final VendorDTO vendorDTO, final Vendor vendor) {
        vendor.setRatings(vendorDTO.getRatings());
        vendor.setProfileDetails(vendorDTO.getProfileDetails());
        final VendorCategory categoryId = vendorDTO.getCategoryId() == null ? null : vendorCategoryRepository.findById(vendorDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("categoryId not found"));
        vendor.setCategoryId(categoryId);
        return vendor;
    }

    public ReferencedWarning getReferencedWarning(final UUID vendorId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(NotFoundException::new);
        final User vendorIdUser = userRepository.findFirstByVendorId(vendor);
        if (vendorIdUser != null) {
            referencedWarning.setKey("vendor.user.vendorId.referenced");
            referencedWarning.addParam(vendorIdUser.getUserId());
            return referencedWarning;
        }
        final bhagya.festivo.festivo.domain.Service vendorIdService = serviceRepository.findFirstByVendorId(vendor);
        if (vendorIdService != null) {
            referencedWarning.setKey("vendor.service.vendorId.referenced");
            referencedWarning.addParam(vendorIdService.getServiceId());
            return referencedWarning;
        }
        final Booking vendorIdBooking = bookingRepository.findFirstByVendorId(vendor);
        if (vendorIdBooking != null) {
            referencedWarning.setKey("vendor.booking.vendorId.referenced");
            referencedWarning.addParam(vendorIdBooking.getBookingId());
            return referencedWarning;
        }
        final Schedule vendorIdSchedule = scheduleRepository.findFirstByVendorId(vendor);
        if (vendorIdSchedule != null) {
            referencedWarning.setKey("vendor.schedule.vendorId.referenced");
            referencedWarning.addParam(vendorIdSchedule.getScheduleId());
            return referencedWarning;
        }
        final Payment vendorIdPayment = paymentRepository.findFirstByVendorId(vendor);
        if (vendorIdPayment != null) {
            referencedWarning.setKey("vendor.payment.vendorId.referenced");
            referencedWarning.addParam(vendorIdPayment.getPaymentId());
            return referencedWarning;
        }
        final Chat vendorIdChat = chatRepository.findFirstByVendorId(vendor);
        if (vendorIdChat != null) {
            referencedWarning.setKey("vendor.chat.vendorId.referenced");
            referencedWarning.addParam(vendorIdChat.getChatId());
            return referencedWarning;
        }
        final Review vendorIdReview = reviewRepository.findFirstByVendorId(vendor);
        if (vendorIdReview != null) {
            referencedWarning.setKey("vendor.review.vendorId.referenced");
            referencedWarning.addParam(vendorIdReview.getReviewId());
            return referencedWarning;
        }
        return null;
    }

}
