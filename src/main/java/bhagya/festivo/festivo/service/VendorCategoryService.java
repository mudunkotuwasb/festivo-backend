package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.domain.VendorCategory;
import bhagya.festivo.festivo.model.VendorCategoryDTO;
import bhagya.festivo.festivo.repos.VendorCategoryRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class VendorCategoryService {

    private final VendorCategoryRepository vendorCategoryRepository;
    private final VendorRepository vendorRepository;

    public VendorCategoryService(final VendorCategoryRepository vendorCategoryRepository,
            final VendorRepository vendorRepository) {
        this.vendorCategoryRepository = vendorCategoryRepository;
        this.vendorRepository = vendorRepository;
    }

    public List<VendorCategoryDTO> findAll() {
        final List<VendorCategory> vendorCategories = vendorCategoryRepository.findAll(Sort.by("vendorCategoryId"));
        return vendorCategories.stream()
                .map(vendorCategory -> mapToDTO(vendorCategory, new VendorCategoryDTO()))
                .toList();
    }

    public VendorCategoryDTO get(final UUID vendorCategoryId) {
        return vendorCategoryRepository.findById(vendorCategoryId)
                .map(vendorCategory -> mapToDTO(vendorCategory, new VendorCategoryDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final VendorCategoryDTO vendorCategoryDTO) {
        final VendorCategory vendorCategory = new VendorCategory();
        mapToEntity(vendorCategoryDTO, vendorCategory);
        return vendorCategoryRepository.save(vendorCategory).getVendorCategoryId();
    }

    public void update(final UUID vendorCategoryId, final VendorCategoryDTO vendorCategoryDTO) {
        final VendorCategory vendorCategory = vendorCategoryRepository.findById(vendorCategoryId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(vendorCategoryDTO, vendorCategory);
        vendorCategoryRepository.save(vendorCategory);
    }

    public void delete(final UUID vendorCategoryId) {
        vendorCategoryRepository.deleteById(vendorCategoryId);
    }

    private VendorCategoryDTO mapToDTO(final VendorCategory vendorCategory,
            final VendorCategoryDTO vendorCategoryDTO) {
        vendorCategoryDTO.setVendorCategoryId(vendorCategory.getVendorCategoryId());
        vendorCategoryDTO.setVendorCategoryName(vendorCategory.getVendorCategoryName());
        return vendorCategoryDTO;
    }

    private VendorCategory mapToEntity(final VendorCategoryDTO vendorCategoryDTO,
            final VendorCategory vendorCategory) {
        vendorCategory.setVendorCategoryName(vendorCategoryDTO.getVendorCategoryName());
        return vendorCategory;
    }

    public boolean vendorCategoryNameExists(final String vendorCategoryName) {
        return vendorCategoryRepository.existsByVendorCategoryNameIgnoreCase(vendorCategoryName);
    }

    public ReferencedWarning getReferencedWarning(final UUID vendorCategoryId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final VendorCategory vendorCategory = vendorCategoryRepository.findById(vendorCategoryId)
                .orElseThrow(NotFoundException::new);
        final Vendor categoryIdVendor = vendorRepository.findFirstByCategoryId(vendorCategory);
        if (categoryIdVendor != null) {
            referencedWarning.setKey("vendorCategory.vendor.categoryId.referenced");
            referencedWarning.addParam(categoryIdVendor.getVendorId());
            return referencedWarning;
        }
        return null;
    }

}
