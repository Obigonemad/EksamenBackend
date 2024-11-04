package dat.routes;

import io.javalin.apibuilder.EndpointGroup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class Routes {

    private final TripRoute tripRoute = new TripRoute();
    private final DoctorRoute doctorRoute = new DoctorRoute();
    public EndpointGroup getRoutes() {
        return () -> {
                path("/trips", tripRoute.getRoutes());
                path("/doctors", doctorRoute.getRoutes());
        };
    }
}
