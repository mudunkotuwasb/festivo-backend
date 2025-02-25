package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Admin;
import bhagya.festivo.festivo.domain.User;
import bhagya.festivo.festivo.model.AdminDTO;
import bhagya.festivo.festivo.repos.AdminRepository;
import bhagya.festivo.festivo.repos.UserRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    public AdminService(final AdminRepository adminRepository,
            final UserRepository userRepository) {
        this.adminRepository = adminRepository;
        this.userRepository = userRepository;
    }

    public List<AdminDTO> findAll() {
        final List<Admin> admins = adminRepository.findAll(Sort.by("adminId"));
        return admins.stream()
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .toList();
    }

    public AdminDTO get(final UUID adminId) {
        return adminRepository.findById(adminId)
                .map(admin -> mapToDTO(admin, new AdminDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final AdminDTO adminDTO) {
        final Admin admin = new Admin();
        mapToEntity(adminDTO, admin);
        return adminRepository.save(admin).getAdminId();
    }

    public void update(final UUID adminId, final AdminDTO adminDTO) {
        final Admin admin = adminRepository.findById(adminId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(adminDTO, admin);
        adminRepository.save(admin);
    }

    public void delete(final UUID adminId) {
        adminRepository.deleteById(adminId);
    }

    private AdminDTO mapToDTO(final Admin admin, final AdminDTO adminDTO) {
        adminDTO.setAdminId(admin.getAdminId());
        return adminDTO;
    }

    private Admin mapToEntity(final AdminDTO adminDTO, final Admin admin) {
        return admin;
    }

    public ReferencedWarning getReferencedWarning(final UUID adminId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Admin admin = adminRepository.findById(adminId)
                .orElseThrow(NotFoundException::new);
        final User adminIdUser = userRepository.findFirstByAdminId(admin);
        if (adminIdUser != null) {
            referencedWarning.setKey("admin.user.adminId.referenced");
            referencedWarning.addParam(adminIdUser.getUserId());
            return referencedWarning;
        }
        return null;
    }

}
