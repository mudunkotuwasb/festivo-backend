package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Event;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.model.EventDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.EventRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public EventService(final EventRepository eventRepository, final UserRepository userRepository,
            final BookingRepository bookingRepository) {
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<EventDTO> findAll() {
        final List<Event> events = eventRepository.findAll(Sort.by("eventId"));
        return events.stream()
                .map(event -> mapToDTO(event, new EventDTO()))
                .toList();
    }

    public EventDTO get(final UUID eventId) {
        return eventRepository.findById(eventId)
                .map(event -> mapToDTO(event, new EventDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final EventDTO eventDTO) {
        final Event event = new Event();
        mapToEntity(eventDTO, event);
        return eventRepository.save(event).getEventId();
    }

    public void update(final UUID eventId, final EventDTO eventDTO) {
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(eventDTO, event);
        eventRepository.save(event);
    }

    public void delete(final UUID eventId) {
        eventRepository.deleteById(eventId);
    }

    private EventDTO mapToDTO(final Event event, final EventDTO eventDTO) {
        eventDTO.setEventId(event.getEventId());
        eventDTO.setEventName(event.getEventName());
        eventDTO.setEventDate(event.getEventDate());
        eventDTO.setEventLocation(event.getEventLocation());
        eventDTO.setUserId(event.getUserId() == null ? null : event.getUserId().getUserId());
        return eventDTO;
    }

    private Event mapToEntity(final EventDTO eventDTO, final Event event) {
        event.setEventName(eventDTO.getEventName());
        event.setEventDate(eventDTO.getEventDate());
        event.setEventLocation(eventDTO.getEventLocation());
        final User userId = eventDTO.getUserId() == null ? null : userRepository.findById(eventDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        event.setUserId(userId);
        return event;
    }

    public ReferencedWarning getReferencedWarning(final UUID eventId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Event event = eventRepository.findById(eventId)
                .orElseThrow(NotFoundException::new);
        final Booking eventIdBooking = bookingRepository.findFirstByEventId(event);
        if (eventIdBooking != null) {
            referencedWarning.setKey("event.booking.eventId.referenced");
            referencedWarning.addParam(eventIdBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

}
