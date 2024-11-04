package dat.routes;

import dat.controllers.impl.TripController;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

public class TripRoute {
    private final TripController tripController = new TripController();

    public EndpointGroup getRoutes() {
        return () -> {
            get("/", tripController::getAllTrips, Role.USER);
            get("/{id}", tripController::getTripById, Role.ANYONE);
            post("/", tripController::createTrip, Role.ANYONE);
            put("/{id}", tripController::updateTrip, Role.ANYONE);
            delete("/{id}", tripController::deleteTrip, Role.ANYONE);
            put("/{tripId}/guides/{guideId}", tripController::addGuideToTrip, Role.ANYONE);
            post("/populate", tripController::populateTripsAndGuides, Role.ANYONE);
            get("/category/{category}", tripController::getTripsByCategory, Role.ANYONE);
            get("/guides/totalprice", tripController::getTotalPricePerGuide, Role.ANYONE);
            get("/trips/{id}/packing/weight", tripController::getTotalWeightOfPackingItems, Role.ANYONE);
        };
    }
}
