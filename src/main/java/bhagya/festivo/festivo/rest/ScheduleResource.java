package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.ScheduleDTO;
import bhagya.festivo.festivo.service.ScheduleService;
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
@RequestMapping(value = "/api/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScheduleResource {

    private final ScheduleService scheduleService;

    public ScheduleResource(final ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public ResponseEntity<List<ScheduleDTO>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.findAll());
    }

    @GetMapping("/{scheduleId}")
    public ResponseEntity<ScheduleDTO> getSchedule(
            @PathVariable(name = "scheduleId") final UUID scheduleId) {
        return ResponseEntity.ok(scheduleService.get(scheduleId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createSchedule(@RequestBody @Valid final ScheduleDTO scheduleDTO) {
        final UUID createdScheduleId = scheduleService.create(scheduleDTO);
        return new ResponseEntity<>(createdScheduleId, HttpStatus.CREATED);
    }

    @PutMapping("/{scheduleId}")
    public ResponseEntity<UUID> updateSchedule(
            @PathVariable(name = "scheduleId") final UUID scheduleId,
            @RequestBody @Valid final ScheduleDTO scheduleDTO) {
        scheduleService.update(scheduleId, scheduleDTO);
        return ResponseEntity.ok(scheduleId);
    }

    @DeleteMapping("/{scheduleId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteSchedule(
            @PathVariable(name = "scheduleId") final UUID scheduleId) {
        scheduleService.delete(scheduleId);
        return ResponseEntity.noContent().build();
    }

}
