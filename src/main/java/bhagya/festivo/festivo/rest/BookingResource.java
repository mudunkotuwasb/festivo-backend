package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.BookingDTO;
import bhagya.festivo.festivo.service.BookingService;
import bhagya.festivo.festivo.util.ReferencedException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/bookings", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookingResource {

    private final BookingService bookingService;

    public BookingResource(final BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAllBookings() {
        return ResponseEntity.ok(bookingService.findAll());
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDTO> getBooking(
            @PathVariable(name = "bookingId") final UUID bookingId) {
        return ResponseEntity.ok(bookingService.get(bookingId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createBooking(@RequestBody @Valid final BookingDTO bookingDTO) {
        final UUID createdBookingId = bookingService.create(bookingDTO);
        return new ResponseEntity<>(createdBookingId, HttpStatus.CREATED);
    }

    @PutMapping("/{bookingId}")
    public ResponseEntity<UUID> updateBooking(
            @PathVariable(name = "bookingId") final UUID bookingId,
            @RequestBody @Valid final BookingDTO bookingDTO) {
        bookingService.update(bookingId, bookingDTO);
        return ResponseEntity.ok(bookingId);
    }

    @DeleteMapping("/{bookingId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteBooking(
            @PathVariable(name = "bookingId") final UUID bookingId) {
        final ReferencedWarning referencedWarning = bookingService.getReferencedWarning(bookingId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        bookingService.delete(bookingId);
        return ResponseEntity.noContent().build();
    }

}
