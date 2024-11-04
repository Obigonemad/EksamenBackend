package dat.controllers.impl;

import dat.config.HibernateConfig;
import dat.daos.impl.GuideDAO;
import dat.daos.impl.TripDAO;
import dat.dtos.GuideDTO;
import dat.dtos.PackingItemsDTO;
import dat.dtos.TripDTO;
import dat.entities.Guide;
import dat.entities.Trip;
import dat.enums.TripCategory;
import dat.exceptions.ApiException;
import dat.utils.FetchData;
import io.javalin.http.Context;
import jakarta.persistence.EntityManagerFactory;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TripController {

    private final TripDAO tripDAO;
    private final GuideDAO guideDAO;

    public TripController() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.tripDAO = TripDAO.getInstance(emf);
        this.guideDAO = GuideDAO.getInstance(emf);
    }

    public void getAllTrips(Context ctx) throws ApiException {
        List<TripDTO> trips = tripDAO.readAll();
        ctx.json(trips);
    }

    public void getTripById(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        TripDTO trip = tripDAO.read(id);

        if (trip != null) {
            ctx.json(trip);
        } else {
            throw new ApiException(404, "Trip not found");
        }
    }

    public void createTrip(Context ctx) throws ApiException {
        TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
        TripDTO createdTrip = tripDAO.create(tripDTO);
        ctx.status(201).json(createdTrip);
    }

    public void updateTrip(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            TripDTO tripDTO = ctx.bodyAsClass(TripDTO.class);
            TripDTO updatedTrip = tripDAO.update(id, tripDTO);
            ctx.status(200).json(updatedTrip);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid trip ID.");
        }
    }

    public void deleteTrip(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = tripDAO.validatePrimaryKey(id);
            if (!exists) {
                throw new ApiException(404, "Trip not found");
            }
            tripDAO.delete(id);
            ctx.status(204); // No Content
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid trip ID.");
        }
    }

    public void addGuideToTrip(Context ctx) throws ApiException {
        try {
            int tripId = Integer.parseInt(ctx.pathParam("tripId"));
            int guideId = Integer.parseInt(ctx.pathParam("guideId"));
            tripDAO.addGuideToTrip(tripId, guideId);
            ctx.status(200).result("Guide added to trip successfully.");
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid ID format for trip or guide.");
        } catch (Exception e) {
            throw new ApiException(500, "Failed to add guide to trip.");
        }
    }

    public void populateTripsAndGuides(Context ctx) throws ApiException {
        try {
            tripDAO.populateGuidesAndTrips();
            ctx.status(200).json("Database populated successfully with sample trips and guides.");
        } catch (Exception e) {
            throw new ApiException(500, "Failed to populate database.");
        }
    }

    public void getTripsByCategory(Context ctx) throws ApiException {
        String categoryParam = ctx.pathParam("category").toUpperCase();
        TripCategory category;

        try {
            category = TripCategory.valueOf(categoryParam); // Convert string to enum
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid category provided.");
        }

        List<TripDTO> trips = tripDAO.getAllTripsByCategory(category); // Call DAO method
        ctx.json(trips);
    }

    public List<GuideDTO> getTotalPricePerGuide(Context ctx) throws ApiException {
        try {
            List<Guide> guides = guideDAO.getAllGuidesWithTrips(); // Fetch all guides with their trips
            List<GuideDTO> guideDTOs = new ArrayList<>(); // Create a new list for GuideDTOs

            for (Guide guide : guides) {
                double totalPrice = 0.0; // Initialize total price for each guide
                // Loop through each trip to calculate the total price
                for (Trip trip : guide.getTrips()) {
                    totalPrice += trip.getPrice(); // Sum up the prices of trips
                }
                // Create a new GuideDTO with the calculated total price
                GuideDTO guideDTO = new GuideDTO(guide);
                guideDTO.setTotalPrice(totalPrice); // Assuming there's a setter for totalPrice
                guideDTOs.add(guideDTO); // Add to the DTO list
            }

            return guideDTOs; // Return the list of GuideDTOs
        } catch (Exception e) {
            throw new ApiException(500, "Error fetching total prices for guides");
        }
    }

    public void getPackingItemsByCategory(Context ctx) {
        String category = ctx.pathParam("category");
        PackingItemsDTO packingItems = FetchData.fetch(category);

        if (packingItems != null) {
            ctx.json(packingItems);
        } else {
            ctx.status(404).json("Packing items not found for category: " + category);
        }
    }

    public void getTotalWeightOfPackingItems(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        TripDTO trip = tripDAO.read(id); // Assuming this now returns a TripDTO with packing items

        if (trip != null) {
            double totalWeight = trip.getPackingItems().stream()
                    .mapToDouble(item -> item.getWeightInGrams() * item.getQuantity()) // Calculate total weight
                    .sum();
            ctx.json(totalWeight);
        } else {
            throw new ApiException(404, "Trip not found");
        }
    }

}

