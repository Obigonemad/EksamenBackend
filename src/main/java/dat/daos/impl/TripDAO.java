package dat.daos.impl;

import dat.daos.IDAO;
import dat.daos.ITripGuideDAO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripDAO implements IDAO<TripDTO, Integer>, ITripGuideDAO {

    private static TripDAO instance;
    private static EntityManagerFactory emf;

    private TripDAO() {
    }

    public static TripDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TripDAO();
        }
        return instance;
    }

    @Override
    public TripDTO read(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }

            return new TripDTO(trip);
        }
    }

    @Override
    public List<TripDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t", Trip.class);
            List<Trip> trips = query.getResultList();
            return trips.stream().map(TripDTO::new).toList(); // Using constructor from DTO
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error retrieving trips");
        }
    }


    @Override
    public TripDTO create(TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = new Trip(tripDTO); // Using constructor from entity
            em.persist(trip);
            em.getTransaction().commit();
            return new TripDTO(trip);
        } catch (PersistenceException e) {
            throw new ApiException(400, "trip allready exists or something else went wrong");

        }
    }

    @Override
    public TripDTO update(Integer id, TripDTO tripDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "trip not found");
            }
            trip.setName(tripDTO.getName());
            trip.setStarttime(tripDTO.getStarttime());
            trip.setEndtime(tripDTO.getEndtime());
            trip.setLatitude(tripDTO.getLatitude());
            trip.setLongitude(tripDTO.getLongitude());
            trip.setCategory(tripDTO.getCategory());
            trip.setPrice(tripDTO.getPrice());

            Trip mergedTrip = em.merge(trip);
            em.getTransaction().commit();
            return mergedTrip != null ? new TripDTO(mergedTrip) : null; // Returning DTO or null
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error updating doctor");
        }
    }

    @Override
    public void delete(Integer id) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, id);
            if (trip == null) {
                throw new ApiException(404, "trip not found");
            }
            em.remove(trip);
            em.getTransaction().commit();
        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ApiException(500, "Error deleting trip");
        } finally {
            em.close();

        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Trip trip = em.find(Trip.class, id);
            return trip != null; // Return true if doctor exists
        }
    }

    @Override
    public void addGuideToTrip(int tripId, int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Trip trip = em.find(Trip.class, tripId);
            if (trip == null) {
                throw new ApiException(404, "Trip not found");
            }
            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            trip.setGuide(guide);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error adding guide to trip");
        }
    }

    @Override
    public Set<TripDTO> getTripsByGuide(int guideId) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Guide guide = em.find(Guide.class, guideId);
            if (guide == null) {
                throw new ApiException(404, "Guide not found");
            }
            return guide.getTrips().stream()
                    .map(TripDTO::new)
                    .collect(Collectors.toSet());  // Use Collectors.toSet() instead of toSet()
        }
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

    public List<TripDTO> getAllTripsByCategory(TripCategory category) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Trip> query = em.createQuery("SELECT t FROM Trip t WHERE t.category = :category", Trip.class);
            query.setParameter("category", category);
            List<Trip> trips = query.getResultList();
            return trips.stream().map(TripDTO::new).collect(Collectors.toList());
        }
    }
    public UserDTO[] populateUsers() throws ApiException {
        User user, admin;
        Role userRole, adminRole;

        user = new User("usertest", "user123");
        admin = new User("admintest", "admin123");
        userRole = new Role("USER");
        adminRole = new Role("ADMIN");
        user.addRole(userRole);
        admin.addRole(adminRole);

        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.persist(userRole);
            em.persist(adminRole);
            em.persist(user);
            em.persist(admin);
            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error populating users");
        }
        UserDTO userDTO = new UserDTO(user.getUsername(), "user123");
        UserDTO adminDTO = new UserDTO(admin.getUsername(), "admin123");
        return new UserDTO[]{userDTO, adminDTO};
    }

}
