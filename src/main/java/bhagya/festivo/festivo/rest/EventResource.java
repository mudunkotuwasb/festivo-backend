package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.EventDTO;
import bhagya.festivo.festivo.service.EventService;
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
@RequestMapping(value = "/api/events", produces = MediaType.APPLICATION_JSON_VALUE)
public class EventResource {

    private final EventService eventService;

    public EventResource(final EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAll());
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable(name = "eventId") final UUID eventId) {
        return ResponseEntity.ok(eventService.get(eventId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createEvent(@RequestBody @Valid final EventDTO eventDTO) {
        final UUID createdEventId = eventService.create(eventDTO);
        return new ResponseEntity<>(createdEventId, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<UUID> updateEvent(@PathVariable(name = "eventId") final UUID eventId,
            @RequestBody @Valid final EventDTO eventDTO) {
        eventService.update(eventId, eventDTO);
        return ResponseEntity.ok(eventId);
    }

    @DeleteMapping("/{eventId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteEvent(@PathVariable(name = "eventId") final UUID eventId) {
        final ReferencedWarning referencedWarning = eventService.getReferencedWarning(eventId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        eventService.delete(eventId);
        return ResponseEntity.noContent().build();
    }

}
