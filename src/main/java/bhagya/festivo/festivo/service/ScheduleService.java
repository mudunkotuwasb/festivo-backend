package bhagya.festivo.festivo.service;

import bhagya.festivo.festivo.domain.Booking;
import bhagya.festivo.festivo.domain.Schedule;
import bhagya.festivo.festivo.domain.Vendor;
import bhagya.festivo.festivo.model.ScheduleDTO;
import bhagya.festivo.festivo.repos.BookingRepository;
import bhagya.festivo.festivo.repos.ScheduleRepository;
import bhagya.festivo.festivo.repos.VendorRepository;
import bhagya.festivo.festivo.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final VendorRepository vendorRepository;
    private final BookingRepository bookingRepository;

    public ScheduleService(final ScheduleRepository scheduleRepository,
            final VendorRepository vendorRepository, final BookingRepository bookingRepository) {
        this.scheduleRepository = scheduleRepository;
        this.vendorRepository = vendorRepository;
        this.bookingRepository = bookingRepository;
    }

    public List<ScheduleDTO> findAll() {
        final List<Schedule> schedules = scheduleRepository.findAll(Sort.by("scheduleId"));
        return schedules.stream()
                .map(schedule -> mapToDTO(schedule, new ScheduleDTO()))
                .toList();
    }

    public ScheduleDTO get(final UUID scheduleId) {
        return scheduleRepository.findById(scheduleId)
                .map(schedule -> mapToDTO(schedule, new ScheduleDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ScheduleDTO scheduleDTO) {
        final Schedule schedule = new Schedule();
        mapToEntity(scheduleDTO, schedule);
        return scheduleRepository.save(schedule).getScheduleId();
    }

    public void update(final UUID scheduleId, final ScheduleDTO scheduleDTO) {
        final Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(scheduleDTO, schedule);
        scheduleRepository.save(schedule);
    }

    public void delete(final UUID scheduleId) {
        scheduleRepository.deleteById(scheduleId);
    }

    private ScheduleDTO mapToDTO(final Schedule schedule, final ScheduleDTO scheduleDTO) {
        scheduleDTO.setScheduleId(schedule.getScheduleId());
        scheduleDTO.setSceduleDate(schedule.getSceduleDate());
        scheduleDTO.setScheduleTimeSlot(schedule.getScheduleTimeSlot());
        scheduleDTO.setScheduleStatus(schedule.getScheduleStatus());
        scheduleDTO.setVendorId(schedule.getVendorId() == null ? null : schedule.getVendorId().getVendorId());
        scheduleDTO.setBookingId(schedule.getBookingId() == null ? null : schedule.getBookingId().getBookingId());
        return scheduleDTO;
    }

    private Schedule mapToEntity(final ScheduleDTO scheduleDTO, final Schedule schedule) {
        schedule.setSceduleDate(scheduleDTO.getSceduleDate());
        schedule.setScheduleTimeSlot(scheduleDTO.getScheduleTimeSlot());
        schedule.setScheduleStatus(scheduleDTO.getScheduleStatus());
        final Vendor vendorId = scheduleDTO.getVendorId() == null ? null : vendorRepository.findById(scheduleDTO.getVendorId())
                .orElseThrow(() -> new NotFoundException("vendorId not found"));
        schedule.setVendorId(vendorId);
        final Booking bookingId = scheduleDTO.getBookingId() == null ? null : bookingRepository.findById(scheduleDTO.getBookingId())
                .orElseThrow(() -> new NotFoundException("bookingId not found"));
        schedule.setBookingId(bookingId);
        return schedule;
    }

    public boolean bookingIdExists(final UUID bookingId) {
        return scheduleRepository.existsByBookingIdBookingId(bookingId);
    }

}
