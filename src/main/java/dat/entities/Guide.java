package dat.entities;

import dat.dtos.GuideDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "guides")

public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private int yearsOfExperience;

    @OneToMany(mappedBy = "guides", cascade = CascadeType.ALL)
    private Set<Trip> trips = new HashSet<>();

    public Guide (GuideDTO guideDTO) {
        this.firstname = guideDTO.getFirstname();
        this.lastname = guideDTO.getLastname();
        this.email = guideDTO.getEmail();
        this.phone = guideDTO.getPhone();
        this.yearsOfExperience = guideDTO.getYearsOfExperience();
    }

    public void addTrip(Trip trip) {
        if (trip != null) {
            this.trips.add(trip);
            trip.setGuide(this);  // Set the guide for the trip to establish the bi-directional relationship
        }
    }


}
