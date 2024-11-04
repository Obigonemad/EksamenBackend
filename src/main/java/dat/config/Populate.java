package dat.config;

import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;

import java.time.LocalDateTime;

public class Populate {
    public static void main(String[] args) {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();

        Populate populate = new Populate(emf);
        try {
            populate.populateGuidesAndTrips();
        } catch (ApiException e) {
            System.out.println(e.getMessage());
        }
    }
    private final EntityManagerFactory emf;

    public Populate(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public void populateGuidesAndTrips() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create and add guides
            Guide guide1 = new Guide();
            guide1.setFirstname("John");
            guide1.setLastname("Doe");
            guide1.setEmail("john.doe@example.com");
            guide1.setPhone("123-456-7890");
            guide1.setYearsOfExperience(5);
            em.persist(guide1);

            Guide guide2 = new Guide();
            guide2.setFirstname("Jane");
            guide2.setLastname("Smith");
            guide2.setEmail("jane.smith@example.com");
            guide2.setPhone("987-654-3210");
            guide2.setYearsOfExperience(8);
            em.persist(guide2);

            // Create and add trips for guide1
            Trip trip1 = new Trip();
            trip1.setStarttime(LocalDateTime.now().plusDays(1));
            trip1.setEndtime(LocalDateTime.now().plusDays(2));
            trip1.setLongitude(34.0522);
            trip1.setLatitude(-118.2437);
            trip1.setName("Beach Adventure");
            trip1.setPrice(299.99);
            trip1.setCategory(TripCategory.BEACH);
            trip1.setGuide(guide1); // Associate with guide1
            em.persist(trip1);

            Trip trip2 = new Trip();
            trip2.setStarttime(LocalDateTime.now().plusDays(3));
            trip2.setEndtime(LocalDateTime.now().plusDays(4));
            trip2.setLongitude(40.7128);
            trip2.setLatitude(-74.0060);
            trip2.setName("City Explorer");
            trip2.setPrice(399.99);
            trip2.setCategory(TripCategory.CITY);
            trip2.setGuide(guide1); // Associate with guide1
            em.persist(trip2);

            // Create and add trips for guide2
            Trip trip3 = new Trip();
            trip3.setStarttime(LocalDateTime.now().plusDays(5));
            trip3.setEndtime(LocalDateTime.now().plusDays(6));
            trip3.setLongitude(47.6062);
            trip3.setLatitude(-122.3321);
            trip3.setName("Forest Hike");
            trip3.setPrice(199.99);
            trip3.setCategory(TripCategory.FOREST);
            trip3.setGuide(guide2); // Associate with guide2
            em.persist(trip3);

            Trip trip4 = new Trip();
            trip4.setStarttime(LocalDateTime.now().plusDays(7));
            trip4.setEndtime(LocalDateTime.now().plusDays(8));
            trip4.setLongitude(37.7749);
            trip4.setLatitude(-122.4194);
            trip4.setName("Mountain Expedition");
            trip4.setPrice(499.99);
            trip4.setCategory(TripCategory.SNOW);
            trip4.setGuide(guide2); // Associate with guide2
            em.persist(trip4);

            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error populating guides and trips");
        }
    }
}