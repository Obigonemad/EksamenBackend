package dat.daos.impl;

import dat.dtos.GuideDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class GuideDAO {

    private static GuideDAO instance;
    private static EntityManagerFactory emf;

    private GuideDAO() {
    }

    public static GuideDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new GuideDAO();
        }
        return instance;
    }

    public List<Guide> getAllGuidesWithTrips() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Guide> query = em.createQuery("SELECT g FROM Guide g LEFT JOIN FETCH g.trips", Guide.class);
            return query.getResultList();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error retrieving guides with trips");
        }
    }
}
