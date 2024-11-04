package dat.dtos;

import dat.entities.Doctor;
import dat.enums.Speciality;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Setter
public class DoctorDTO {
    private int id;
    private String name;
    private LocalDate dateOfBirth;
    private int yearOfGraduation;
    private String nameOfClinic;
    private Speciality speciality;

    public DoctorDTO(int id, String name, LocalDate dateOfBirth, int yearOfGraduation, String nameOfClinic, Speciality speciality) {
        this.id = id;
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.yearOfGraduation = yearOfGraduation;
        this.nameOfClinic = nameOfClinic;
        this.speciality = speciality;
    }

    public DoctorDTO (Doctor doctor) {
        this.id = doctor.getId();
        this.name = doctor.getName();
        this.dateOfBirth = doctor.getDateOfBirth();
        this.yearOfGraduation = doctor.getYearOfGraduation();
        this.nameOfClinic = doctor.getNameOfClinic();
        this.speciality = doctor.getSpeciality();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorDTO doctorDTO = (DoctorDTO) o;
        return id == doctorDTO.id && yearOfGraduation == doctorDTO.yearOfGraduation && Objects.equals(name, doctorDTO.name) && Objects.equals(dateOfBirth, doctorDTO.dateOfBirth) && Objects.equals(nameOfClinic, doctorDTO.nameOfClinic) && speciality == doctorDTO.speciality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, yearOfGraduation, nameOfClinic, speciality);
    }
}
