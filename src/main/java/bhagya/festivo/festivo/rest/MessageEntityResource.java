package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.MessageEntityDTO;
import bhagya.festivo.festivo.service.MessageEntityService;
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
@RequestMapping(value = "/api/messageEntities", produces = MediaType.APPLICATION_JSON_VALUE)
public class MessageEntityResource {

    private final MessageEntityService messageEntityService;

    public MessageEntityResource(final MessageEntityService messageEntityService) {
        this.messageEntityService = messageEntityService;
    }

    @GetMapping
    public ResponseEntity<List<MessageEntityDTO>> getAllMessageEntities() {
        return ResponseEntity.ok(messageEntityService.findAll());
    }

    @GetMapping("/{messageId}")
    public ResponseEntity<MessageEntityDTO> getMessageEntity(
            @PathVariable(name = "messageId") final UUID messageId) {
        return ResponseEntity.ok(messageEntityService.get(messageId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createMessageEntity(
            @RequestBody @Valid final MessageEntityDTO messageEntityDTO) {
        final UUID createdMessageId = messageEntityService.create(messageEntityDTO);
        return new ResponseEntity<>(createdMessageId, HttpStatus.CREATED);
    }

    @PutMapping("/{messageId}")
    public ResponseEntity<UUID> updateMessageEntity(
            @PathVariable(name = "messageId") final UUID messageId,
            @RequestBody @Valid final MessageEntityDTO messageEntityDTO) {
        messageEntityService.update(messageId, messageEntityDTO);
        return ResponseEntity.ok(messageId);
    }

    @DeleteMapping("/{messageId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteMessageEntity(
            @PathVariable(name = "messageId") final UUID messageId) {
        final ReferencedWarning referencedWarning = messageEntityService.getReferencedWarning(messageId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        messageEntityService.delete(messageId);
        return ResponseEntity.noContent().build();
    }

}
