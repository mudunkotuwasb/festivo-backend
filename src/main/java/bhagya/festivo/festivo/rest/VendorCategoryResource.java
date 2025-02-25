package bhagya.festivo.festivo.rest;

import bhagya.festivo.festivo.model.VendorCategoryDTO;
import bhagya.festivo.festivo.service.VendorCategoryService;
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
@RequestMapping(value = "/api/vendorCategories", produces = MediaType.APPLICATION_JSON_VALUE)
public class VendorCategoryResource {

    private final VendorCategoryService vendorCategoryService;

    public VendorCategoryResource(final VendorCategoryService vendorCategoryService) {
        this.vendorCategoryService = vendorCategoryService;
    }

    @GetMapping
    public ResponseEntity<List<VendorCategoryDTO>> getAllVendorCategories() {
        return ResponseEntity.ok(vendorCategoryService.findAll());
    }

    @GetMapping("/{vendorCategoryId}")
    public ResponseEntity<VendorCategoryDTO> getVendorCategory(
            @PathVariable(name = "vendorCategoryId") final UUID vendorCategoryId) {
        return ResponseEntity.ok(vendorCategoryService.get(vendorCategoryId));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createVendorCategory(
            @RequestBody @Valid final VendorCategoryDTO vendorCategoryDTO) {
        final UUID createdVendorCategoryId = vendorCategoryService.create(vendorCategoryDTO);
        return new ResponseEntity<>(createdVendorCategoryId, HttpStatus.CREATED);
    }

    @PutMapping("/{vendorCategoryId}")
    public ResponseEntity<UUID> updateVendorCategory(
            @PathVariable(name = "vendorCategoryId") final UUID vendorCategoryId,
            @RequestBody @Valid final VendorCategoryDTO vendorCategoryDTO) {
        vendorCategoryService.update(vendorCategoryId, vendorCategoryDTO);
        return ResponseEntity.ok(vendorCategoryId);
    }

    @DeleteMapping("/{vendorCategoryId}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteVendorCategory(
            @PathVariable(name = "vendorCategoryId") final UUID vendorCategoryId) {
        final ReferencedWarning referencedWarning = vendorCategoryService.getReferencedWarning(vendorCategoryId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        vendorCategoryService.delete(vendorCategoryId);
        return ResponseEntity.noContent().build();
    }

}
