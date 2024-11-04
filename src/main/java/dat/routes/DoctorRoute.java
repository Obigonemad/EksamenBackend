package dat.routes;

import dat.controllers.DoctorControllerDB;
import dat.security.enums.Role;
import io.javalin.apibuilder.EndpointGroup;
import static io.javalin.apibuilder.ApiBuilder.*;

import dat.controllers.DoctorMockController;

public class DoctorRoute {

//    private final DoctorMockController doctorMockController = new DoctorMockController();
    private final DoctorControllerDB doctorControllerDB = new DoctorControllerDB();
    public EndpointGroup getRoutes() {
        return () -> {
            post("/populate", doctorControllerDB::populate, Role.USER);
            post("/", doctorControllerDB::createDoctor); // Use the exact method name from the controller
            get("/", doctorControllerDB::getAllDoctors, Role.USER);
            get("/{id}", doctorControllerDB::getDoctorById);
            get("/speciality/{speciality}", doctorControllerDB::getDoctorsBySpeciality);
            get("/birthdate/range", doctorControllerDB::getDoctorsByBirthdateRange);
            put("/{id}", doctorControllerDB::updateDoctor);
        };
    }


}
