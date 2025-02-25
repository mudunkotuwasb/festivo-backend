package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.ServiceDTO;
import bhagya.festivo.festivo.service.ServiceService;
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
@RequestMapping(value = "/api/services", produces = MediaType.APPLICATION_JSON_VALUE)
public class ServiceResource {

    private final ServiceService serviceService;

    public ServiceResource(final ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return ResponseEntity.ok(serviceService.findAll());
    }

    @GetMapping("/{serviceId}")
    public ResponseEntity<ServiceDTO> getService(
            @PathVariable(name = "serviceId") final UUID serviceId) {
        return ResponseEntity.ok(serviceService.get(serviceId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createService(@RequestBody @Valid final ServiceDTO serviceDTO) {
        final UUID createdServiceId = serviceService.create(serviceDTO);
        return new ResponseEntity<>(createdServiceId, HttpStatus.CREATED);
    }

    @PutMapping("/{serviceId}")
    public ResponseEntity<UUID> updateService(
            @PathVariable(name = "serviceId") final UUID serviceId,
            @RequestBody @Valid final ServiceDTO serviceDTO) {
        serviceService.update(serviceId, serviceDTO);
        return ResponseEntity.ok(serviceId);
    }

    @DeleteMapping("/{serviceId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteService(
            @PathVariable(name = "serviceId") final UUID serviceId) {
        final ReferencedWarning referencedWarning = serviceService.getReferencedWarning(serviceId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        serviceService.delete(serviceId);
        return ResponseEntity.noContent().build();
    }

}
