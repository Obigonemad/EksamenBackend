package dat.dtos;

import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.utils.FetchData;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Setter
public class TripDTO {
    private Long id;
    private LocalDateTime starttime;
    private LocalDateTime endtime;
    private double longitude;
    private double latitude;
    private String name;
    private double price;
    @Enumerated
    private TripCategory category;
    private GuideDTO guide;
    private List<PackingItemsDTO> packingItems;

    public TripDTO(Trip trip) {
        this.id = trip.getId();
        this.starttime = trip.getStarttime();
        this.endtime = trip.getEndtime();
        this.longitude = trip.getLongitude();
        this.latitude = trip.getLatitude();
        this.name = trip.getName();
        this.price = trip.getPrice();
        this.category = trip.getCategory();
        this.guide = new GuideDTO(trip.getGuide());
        this.packingItems = (List<PackingItemsDTO>) fetchPackingItems(trip.getCategory().name());
    }

    private List<? extends Object> fetchPackingItems(String category) {
        // Call your PackingService to fetch packing items for the given category
        PackingItemsDTO packingItemsDTO = FetchData.fetch(category);
        return packingItemsDTO != null ? packingItemsDTO.getItems() : new ArrayList<>();
    }

    // Getter for packingItems
    public List<PackingItemsDTO> getPackingItems() {
        return packingItems;
    }
}
