package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.ServiceDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.ServiceRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import bhagya.festivo.festivo.util.ReferencedWarning;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ServiceService {

    private final ServiceRepository serviceRepository;
    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;

    public ServiceService(final ServiceRepository serviceRepository,
            final VendorRepository vendorRepository, final BookingRepository bookingRepository) {
        this.serviceRepository = serviceRepository;
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ServiceDTO> findAll() {
        final List<bhagya.festivo.festivo.domain.Service> services = serviceRepository.findAll(Sort.by("serviceId"));
        return services.stream()
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .toList();
    }

    public ServiceDTO get(final UUID serviceId) {
        return serviceRepository.findById(serviceId)
                .map(service -> mapToDTO(service, new ServiceDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ServiceDTO serviceDTO) {
        final bhagya.festivo.festivo.domain.Service service = new bhagya.festivo.festivo.domain.Service();
        mapToEntity(serviceDTO, service);
        return serviceRepository.save(service).getServiceId();
    }

    public void update(final UUID serviceId, final ServiceDTO serviceDTO) {
        final bhagya.festivo.festivo.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(serviceDTO, service);
        serviceRepository.save(service);
    }

    public void delete(final UUID serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    private ServiceDTO mapToDTO(final bhagya.festivo.festivo.domain.Service service,
            final ServiceDTO serviceDTO) {
        serviceDTO.setServiceId(service.getServiceId());
        serviceDTO.setServiceName(service.getServiceName());
        serviceDTO.setServiceDescription(service.getServiceDescription());
        serviceDTO.setServicePrice(service.getServicePrice());
        serviceDTO.setVendorId(service.getVendorId() == null ? null : service.getVendorId().getVendorId());
        return serviceDTO;
    }

    private bhagya.festivo.festivo.domain.Service mapToEntity(final ServiceDTO serviceDTO,
            final bhagya.festivo.festivo.domain.Service service) {
        service.setServiceName(serviceDTO.getServiceName());
        service.setServiceDescription(serviceDTO.getServiceDescription());
        service.setServicePrice(serviceDTO.getServicePrice());
        final Vendor vendorId = serviceDTO.getVendorId() == null ? null : vendorRepository.findById(serviceDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        service.setVendorId(vendorId);
        return service;
    }

    public ReferencedWarning getReferencedWarning(final UUID serviceId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final bhagya.festivo.festivo.domain.Service service = serviceRepository.findById(serviceId)
                .orElseThrow(NotFoundException::new);
        final Booking serviceIdBooking = bookingRepository.findFirstByServiceId(service);
        if (serviceIdBooking != null) {
            referencedWarning.setKey("service.booking.serviceId.referenced");
            referencedWarning.addParam(serviceIdBooking.getBookingId());
            return referencedWarning;
        }
        return null;
    }

}
