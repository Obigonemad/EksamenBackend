package dat.dtos;



import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@NoArgsConstructor
public class AppointmentDTO {

    private int id;
    private String clientName;
    private LocalDate date;
    private LocalTime time;
    private String comment;

    // Getters and Setters
    // ...
}
