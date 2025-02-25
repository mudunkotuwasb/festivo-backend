package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Review;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.ReviewDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
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
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;

    public ReviewService(final ReviewRepository reviewRepository,
            final UserRepository userRepository, final VendorRepository vendorRepository,
            final BookingRepository bookingRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ReviewDTO> findAll() {
        final List<Review> reviews = reviewRepository.findAll(Sort.by("reviewId"));
        return reviews.stream()
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .toList();
    }

    public ReviewDTO get(final UUID reviewId) {
        return reviewRepository.findById(reviewId)
                .map(review -> mapToDTO(review, new ReviewDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ReviewDTO reviewDTO) {
        final Review review = new Review();
        mapToEntity(reviewDTO, review);
        return reviewRepository.save(review).getReviewId();
    }

    public void update(final UUID reviewId, final ReviewDTO reviewDTO) {
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(reviewDTO, review);
        reviewRepository.save(review);
    }

    public void delete(final UUID reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    private ReviewDTO mapToDTO(final Review review, final ReviewDTO reviewDTO) {
        reviewDTO.setReviewId(review.getReviewId());
        reviewDTO.setRating(review.getRating());
        reviewDTO.setComment(review.getComment());
        reviewDTO.setCreatedAt(review.getCreatedAt());
        reviewDTO.setUserId(review.getUserId() == null ? null : review.getUserId().getUserId());
        reviewDTO.setVendorId(review.getVendorId() == null ? null : review.getVendorId().getVendorId());
        return reviewDTO;
    }

    private Review mapToEntity(final ReviewDTO reviewDTO, final Review review) {
        review.setRating(reviewDTO.getRating());
        review.setComment(reviewDTO.getComment());
        review.setCreatedAt(reviewDTO.getCreatedAt());
        final User userId = reviewDTO.getUserId() == null ? null : userRepository.findById(reviewDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        review.setUserId(userId);
        final Vendor vendorId = reviewDTO.getVendorId() == null ? null : vendorRepository.findById(reviewDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        review.setVendorId(vendorId);
        return review;
    }

    public ReferencedWarning getReferencedWarning(final UUID reviewId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Review review = reviewRepository.findById(reviewId)
                .orElseThrow(NotFoundException::new);
        final Booking reviewIdBooking = bookingRepository.findFirstByReviewId(review);
        if (reviewIdBooking != null) {
            referencedWarning.setKey("review.booking.reviewId.referenced");
            referencedWarning.addParam(reviewIdBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

}
