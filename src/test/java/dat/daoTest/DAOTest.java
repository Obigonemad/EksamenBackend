package dat.daoTest;

import dat.config.HibernateConfig;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    private static EntityManagerFactory emf;
    private static DoctorDAO dao;


    @BeforeAll
    static void setUpBA() {
        emf = HibernateConfig.getEntityManagerFactoryForTest();
        dao = DoctorDAO.getInstance(emf);
    }

    @BeforeEach
    void setUp() throws ApiException {


        dao.populateDoctorsAndAppointments();// Pre-populate database with test data
    }

    @AfterEach
    void tearDown() {
        try (var em = emf.createEntityManager()) {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM Appointment").executeUpdate();
            em.createQuery("DELETE FROM Doctor").executeUpdate();
            em.createNativeQuery("ALTER sequence doctors_id_seq RESTART WITH 1").executeUpdate();
            em.getTransaction().commit();
        }
    }

    @AfterAll
    static void tearDownAA() {
        emf.close();
    }

    @Test
    void testCreate() throws ApiException {
        DoctorDTO newDoctor = new DoctorDTO();
        newDoctor.setName("Dr. John Doe");
        newDoctor.setDateOfBirth(LocalDate.of(1985, 6, 15));
        newDoctor.setYearOfGraduation(2010);
        newDoctor.setNameOfClinic("Health Center");
        newDoctor.setSpeciality(Speciality.SURGERY);

        DoctorDTO createdDoctor = dao.create(newDoctor);
        assertNotNull(createdDoctor);
        assertEquals("Dr. John Doe", createdDoctor.getName());
    }

    @Test
    void testRead() throws ApiException {
        DoctorDTO doctor = dao.read(1);
        assertNotNull(doctor);
        assertEquals("Dr. Alice Smith", doctor.getName());
    }

    @Test
    void testReadAll() throws ApiException {
        List<DoctorDTO> doctors = dao.readAll();
        assertNotNull(doctors);
        assertEquals(2, doctors.size()); // Based on initial data population
    }

    @Test
    void testUpdate() throws ApiException {
        // First, read the doctor to check its current state
        DoctorDTO existingDoctor = dao.read(1);
        assertNotNull(existingDoctor); // Ensure the doctor exists before updating

        // Update doctor details
        DoctorDTO updatedDoctor = new DoctorDTO();
        updatedDoctor.setId(existingDoctor.getId()); // Use the existing ID
        updatedDoctor.setName("Dr. Alice Smith Updated");
        updatedDoctor.setDateOfBirth(LocalDate.of(1975, 4, 12));
        updatedDoctor.setYearOfGraduation(2000);
        updatedDoctor.setNameOfClinic("Updated Health Clinic");
        updatedDoctor.setSpeciality(Speciality.FAMILY_MEDICINE);

        DoctorDTO result = dao.update(existingDoctor.getId(), updatedDoctor);
        assertNotNull(result);

        // Verify that the update was successful
        DoctorDTO updatedResult = dao.read(existingDoctor.getId());
        assertEquals("Dr. Alice Smith Updated", updatedResult.getName());
    }


    @Test
    void testDelete() throws ApiException {
        // Step 1: Read the doctor with ID 1 to ensure it exists
        DoctorDTO doctor = dao.read(1);
        assertNotNull(doctor, "The doctor should exist before deletion");

        // Step 2: Call delete method
        dao.delete(1);

        // Step 3: Verify that attempting to read the deleted doctor throws an ApiException
        assertThrows(ApiException.class, () -> dao.read(1), "Reading a deleted doctor should throw an ApiException");
    }



    @Test
    void testDoctorBySpeciality() throws ApiException {
        List<DoctorDTO> surgeons = dao.doctorBySpeciality(Speciality.SURGERY);

        assertEquals(1, surgeons.size()); // Assuming there's one surgeon in the data
        assertEquals("Dr. Bob Johnson", surgeons.get(0).getName());
    }

    @Test
    void testDoctorByBirthdateRange() throws ApiException {
        LocalDate from = LocalDate.of(1970, 1, 1);
        LocalDate to = LocalDate.of(1980, 12, 31);
        List<DoctorDTO> doctors = dao.doctorByBirthdateRange(from, to);

        assertEquals(2, doctors.size()); // Assuming one doctor falls within this range
        assertEquals("Dr. Alice Smith", doctors.get(0).getName());
    }
}
