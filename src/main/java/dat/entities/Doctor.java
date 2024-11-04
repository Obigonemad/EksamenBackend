package dat.entities;

import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private LocalDate dateOfBirth;

    private int yearOfGraduation;

    private String nameOfClinic;

    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Appointment> appointments;

    public Doctor (DoctorDTO doctorDTO) {
        this.name = doctorDTO.getName();
        this.dateOfBirth = doctorDTO.getDateOfBirth();
        this.yearOfGraduation = doctorDTO.getYearOfGraduation();
        this.nameOfClinic = doctorDTO.getNameOfClinic();
        this.speciality = doctorDTO.getSpeciality();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id == doctor.id && yearOfGraduation == doctor.yearOfGraduation && Objects.equals(name, doctor.name) && Objects.equals(dateOfBirth, doctor.dateOfBirth) && Objects.equals(nameOfClinic, doctor.nameOfClinic) && speciality == doctor.speciality && Objects.equals(createdAt, doctor.createdAt) && Objects.equals(updatedAt, doctor.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateOfBirth, yearOfGraduation, nameOfClinic, speciality, createdAt, updatedAt);
    }
}
