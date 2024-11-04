package dat.routes;

import dat.config.ApplicationConfig;
import dat.config.HibernateConfig;
import dat.daos.impl.DoctorDAO;
import dat.daos.impl.TripDAO;
import dat.exceptions.ApiException;
import dat.security.controllers.SecurityController;
import dat.security.daos.SecurityDAO;
import dat.security.exceptions.ValidationException;
import dk.bugelhartmann.UserDTO;
import io.javalin.Javalin;

import io.restassured.RestAssured;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.AbstractList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@Disabled
class RouteTest  {
    private static ApplicationConfig appConfig;
    private static Javalin app;
    private static EntityManagerFactory emf = HibernateConfig.getEntityManagerFactoryForTest();
    private final static SecurityController securityController = SecurityController.getInstance();
    private final static SecurityDAO securityDAO = new SecurityDAO(emf);
    private static ObjectMapper jsonMapper = new ObjectMapper();
    private static TripDAO dao;
    private static String userToken, adminToken;
    private static UserDTO userDTO, adminDTO;

    @BeforeAll
    static void setUpBA() {
        RestAssured.baseURI = "http://localhost:7070/api/";
        HibernateConfig.setTest(true);
        app = ApplicationConfig.startServer(7070);
        dao = TripDAO.getInstance(emf);
    }

    @BeforeEach
    void setUp() throws ApiException {


        dao.populateGuidesAndTrips();
        UserDTO[] users = dao.populateUsers();
        userDTO = users[0];
        adminDTO = users[1];
        try {
            UserDTO verifiedUser = securityDAO.getVerifiedUser(userDTO.getUsername(), userDTO.getPassword());
            UserDTO verifiedAdmin = securityDAO.getVerifiedUser(adminDTO.getUsername(), adminDTO.getPassword());
            userToken = "Bearer " + securityController.createToken(verifiedUser);
            adminToken = "Bearer " + securityController.createToken(verifiedAdmin);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown() {
        try (EntityManager em = emf.createEntityManager()){
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Role ").executeUpdate();
            em.createQuery("DELETE FROM User").executeUpdate();
            em.createQuery("DELETE FROM Trip ").executeUpdate();
            em.createQuery("DELETE FROM Guide ").executeUpdate();
            em.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @AfterAll
    static void tearDownAA() {
        ApplicationConfig.stopServer(app);
    }

    @Test
    void getTrips() {
        System.out.println("usertoken: " + userToken);
        System.out.println("admintoken: " + adminToken);
        given()
                .when()
                .header("Authorization", userToken)
                .get("/trips")
                .then()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void getTripsById() {
        given()
                .when()
                .get("/trips/1")
                .then()
                .statusCode(200)
                .body("name", equalTo("Beach Adventure"));
    }

    @Test
    void createTrips() {

        String json = "{\"starttime\":\"2024-12-05T09:30:00\",\"endtime\":\"2024-12-05T17:00:00\",\"longitude\":9.5788,\"latitude\":56.1629,\"name\":\"Forest Adventure\",\"price\":150.00,\"category\":\"forest\",\"guide\":{\"id\":2,\"firstname\":\"Emily\",\"lastname\":\"Smith\",\"email\":\"emily.smith@example.com\",\"phone\":\"87654321\",\"yearsOfExperience\":8}}";

        given()
                .contentType("application/json")
                .body(json)
                .when()
                .post("/trips")
                .then()
                .statusCode(201)
                .body("category", equalTo("forest"));
    }

    @Test
    void updateTrips() {
        String json = "{\"id\":1,\"starttime\":\"2024-11-04T08:30:00\",\"endtime\":\"2024-11-04T10:30:00\",\"longitude\":12.345678,\"latitude\":98.765432,\"name\":\"Scenic Mountain Tour\",\"price\":150.00,\"category\":\"CITY\",\"guide\":{\"id\":1,\"name\":\"John Doe\",\"experience\":5}}";



        given()
                .contentType("application/json")
                .body(json)
                .when()
                .put("/trips/1")
                .then()
                .log().all() // Logs request and response
                .statusCode(200)
                .body("name", equalTo("Scenic Mountain Tour"));
    }

    @Test
    void deleteTrips() {
        given()
                .contentType("application/json")
                .when()
                .delete("/trips/1")
                .then()
                .statusCode(204);

    }

}
//