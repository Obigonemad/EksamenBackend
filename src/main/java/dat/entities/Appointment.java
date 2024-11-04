package dat.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import dat.enums.Speciality;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Getter
@NoArgsConstructor
@Setter
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String clientName;

    private LocalDate date;

    private LocalTime time;

    private String comment;

    @ManyToOne
//    @JsonManagedReference
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;


}
