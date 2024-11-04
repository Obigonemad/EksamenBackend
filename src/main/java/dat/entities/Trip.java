package dat.entities;

import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.enums.TripCategory;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Trip {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double longitude;
    private double latitude;
    private String name;
    private double price;

    @Enumerated(EnumType.STRING)
    private TripCategory category;

    @ManyToOne
    private Guide guides;

    public Trip (TripDTO tripDTO) {
        this.starttime = tripDTO.getStarttime();
        this.endtime = tripDTO.getEndtime();
        this.longitude = tripDTO.getLongitude();
        this.latitude = tripDTO.getLatitude();
        this.name = tripDTO.getName();
        this.price = tripDTO.getPrice();
        this.category = tripDTO.getCategory();
    }
    @ManyToOne
    @JoinColumn(name = "guide_id")
    private Guide guide;

    public void setGuide(Guide guide) {
        this.guide = guide;
    }
    public double getPrice() { // Ensure this method exists
        return price;
    }
}
