package dat.daos.impl;

import dat.daos.IDAO;
import dat.entities.Appointment;
import dat.entities.Doctor;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import dat.security.entities.Role;
import dat.security.entities.User;
import dk.bugelhartmann.UserDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import jakarta.persistence.TypedQuery;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DoctorDAO implements IDAO<DoctorDTO, Integer> {

    private static DoctorDAO instance;
    private static EntityManagerFactory emf;

    private DoctorDAO() {
        // private constructor to enforce singleton pattern
    }

    public static DoctorDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new DoctorDAO();
        }
        return instance;
    }

    @Override
    public DoctorDTO read(Integer id) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found");
            }
            return new DoctorDTO(doctor); // Return DTO or null
        }
    }

    @Override
    public List<DoctorDTO> readAll() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d", Doctor.class);
            List<Doctor> doctors = query.getResultList();
            return doctors.stream().map(DoctorDTO::new).toList(); // Using constructor from DTO
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error retrieving doctors");
        }

    }

    public List<DoctorDTO> doctorBySpeciality(Speciality speciality) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d WHERE d.speciality = :speciality", Doctor.class);
            query.setParameter("speciality", speciality);
            List<Doctor> doctors = query.getResultList();
            return doctors.stream().map(DoctorDTO::new).toList();
        } catch (PersistenceException e) {
            throw new ApiException(400, "error getting specialities");// Using constructor from DTO
        }
    }

    public List<DoctorDTO> doctorByBirthdateRange(LocalDate from, LocalDate to) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Doctor> query = em.createQuery("SELECT d FROM Doctor d WHERE d.dateOfBirth BETWEEN :from AND :to", Doctor.class);
            query.setParameter("from", from);
            query.setParameter("to", to);
            List<Doctor> doctors = query.getResultList();
            return doctors.stream().map(DoctorDTO::new).toList();
        } catch (PersistenceException e) {
            throw new ApiException(400, "error getting range");

        }
    }

    @Override
    public DoctorDTO create(DoctorDTO doctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = new Doctor(doctorDTO); // Using constructor from entity
            em.persist(doctor);
            em.getTransaction().commit();
            return new DoctorDTO(doctor);
        } catch (PersistenceException e) {
            throw new ApiException(400, "Doctor allready exists or something else went wrong");

        }
    }

    @Override
    public DoctorDTO update(Integer id, DoctorDTO doctorDTO) throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found");
            }
            doctor.setName(doctorDTO.getName());
            doctor.setDateOfBirth(doctorDTO.getDateOfBirth());
            doctor.setYearOfGraduation(doctorDTO.getYearOfGraduation());
            doctor.setNameOfClinic(doctorDTO.getNameOfClinic());
            doctor.setSpeciality(doctorDTO.getSpeciality());
            Doctor mergedDoctor = em.merge(doctor);
            em.getTransaction().commit();
            return mergedDoctor != null ? new DoctorDTO(mergedDoctor) : null; // Returning DTO or null
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error updating doctor");
        }
    }

    @Override
    public void delete(Integer id) throws ApiException {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            Doctor doctor = em.find(Doctor.class, id);
            if (doctor == null) {
                throw new ApiException(404, "Doctor not found");
            }
            em.remove(doctor);
            em.getTransaction().commit();
        } catch (Exception e) {

            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw new ApiException(500, "Error deleting doctor");
        } finally {
            em.close();

        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Doctor doctor = em.find(Doctor.class, id);
            return doctor != null; // Return true if doctor exists
        }
    }

    public void populateDoctorsAndAppointments() throws ApiException {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Create and add doctors
            Doctor doctor1 = new Doctor();
            doctor1.setName("Dr. Alice Smith");
            doctor1.setDateOfBirth(LocalDate.of(1975, 4, 12));
            doctor1.setYearOfGraduation(2000);
            doctor1.setNameOfClinic("City Health Clinic");
            doctor1.setSpeciality(Speciality.FAMILY_MEDICINE);
            em.persist(doctor1);

            Doctor doctor2 = new Doctor();
            doctor2.setName("Dr. Bob Johnson");
            doctor2.setDateOfBirth(LocalDate.of(1980, 8, 5));
            doctor2.setYearOfGraduation(2005);
            doctor2.setNameOfClinic("Downtown Medical Center");
            doctor2.setSpeciality(Speciality.SURGERY);
            em.persist(doctor2);

            // Create and add appointments for doctor1
            Appointment appointment1 = new Appointment();
            appointment1.setClientName("John Smith");
            appointment1.setDate(LocalDate.of(2023, 11, 24));
            appointment1.setTime(LocalTime.of(9, 45));
            appointment1.setComment("First visit");
            appointment1.setDoctor(doctor1);
            em.persist(appointment1);

            Appointment appointment2 = new Appointment();
            appointment2.setClientName("Alice Johnson");
            appointment2.setDate(LocalDate.of(2023, 11, 27));
            appointment2.setTime(LocalTime.of(10, 30));
            appointment2.setComment("Follow up");
            appointment2.setDoctor(doctor1);
            em.persist(appointment2);

            // Create and add appointments for doctor2
            Appointment appointment3 = new Appointment();
            appointment3.setClientName("Bob Anderson");
            appointment3.setDate(LocalDate.of(2023, 12, 12));
            appointment3.setTime(LocalTime.of(14, 0));
            appointment3.setComment("General check");
            appointment3.setDoctor(doctor2);
            em.persist(appointment3);

            Appointment appointment4 = new Appointment();
            appointment4.setClientName("Emily White");
            appointment4.setDate(LocalDate.of(2023, 12, 15));
            appointment4.setTime(LocalTime.of(11, 0));
            appointment4.setComment("Consultation");
            appointment4.setDoctor(doctor2);
            em.persist(appointment4);

            em.getTransaction().commit();
        } catch (PersistenceException e) {
            throw new ApiException(400, "Error populating doctors and appointments");
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
