package dat.daos;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DoctorMockDAO implements IDAO<DoctorDTO, Integer> {

    private static List<DoctorDTO> doctors = new ArrayList<>();

    static {
        doctors.add(new DoctorDTO(1, "Dr. Alice Smith", LocalDate.of(1975, 4, 12), 2000, "City Health Clinic", Speciality.FAMILY_MEDICINE));
        doctors.add(new DoctorDTO(2, "Dr. Bob Johnson", LocalDate.of(1980, 8, 5), 2005, "Downtown Medical Center", Speciality.SURGERY));
        doctors.add(new DoctorDTO(3, "Dr. Clara Lee", LocalDate.of(1983, 7, 22), 2008, "Green Valley Hospital", Speciality.PEDIATRICS));
        doctors.add(new DoctorDTO(4, "Dr. David Park", LocalDate.of(1978, 11, 15), 2003, "Hillside Medical Practice", Speciality.PSYCHIATRY));
        doctors.add(new DoctorDTO(5, "Dr. Emily White", LocalDate.of(1982, 9, 30), 2007, "Metro Health Center", Speciality.GERIATRICS));
        doctors.add(new DoctorDTO(6, "Dr. Fiona Martinez", LocalDate.of(1985, 2, 17), 2010, "Riverside Wellness Clinic", Speciality.SURGERY));
        doctors.add(new DoctorDTO(7, "Dr. George Kim", LocalDate.of(1979, 5, 29), 2004, "Summit Health Institute", Speciality.FAMILY_MEDICINE));
    }

    @Override
    public List<DoctorDTO> readAll() {
        return new ArrayList<>(doctors);
    }

    @Override
    public DoctorDTO read(Integer id) {
        return doctors.stream().filter(d -> d.getId() == id).findFirst().orElse(null);
    }

    @Override
    public DoctorDTO create(DoctorDTO doctor) {
        doctors.add(doctor);
        return doctor;
    }

    @Override
    public DoctorDTO update(Integer id, DoctorDTO updatedDoctor) {
        Optional<DoctorDTO> existingDoctor = doctors.stream().filter(d -> d.getId() == id).findFirst();
        existingDoctor.ifPresent(d -> {
            d.setName(updatedDoctor.getName());
            d.setDateOfBirth(updatedDoctor.getDateOfBirth());
            d.setYearOfGraduation(updatedDoctor.getYearOfGraduation());
            d.setNameOfClinic(updatedDoctor.getNameOfClinic());
            d.setSpeciality(updatedDoctor.getSpeciality());
        });
        return existingDoctor.orElse(null);
    }

    @Override
    public void delete(Integer id) {
        doctors.removeIf(d -> d.getId() == id);
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        return doctors.stream().anyMatch(d -> d.getId() == id);
    }

    // Additional custom methods
    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) {
        return doctors.stream()
                .filter(d -> d.getSpeciality() == speciality)
                .collect(Collectors.toList());
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) {
        return doctors.stream()
                .filter(d -> !d.getDateOfBirth().isBefore(from) && !d.getDateOfBirth().isAfter(to))
                .collect(Collectors.toList());
    }
}
