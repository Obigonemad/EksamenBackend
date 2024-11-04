package dat.controllers;

import dat.config.HibernateConfig;
import dat.daos.impl.DoctorDAO;
import dat.dtos.DoctorDTO;
import dat.enums.Speciality;
import dat.exceptions.ApiException;
import jakarta.persistence.EntityManagerFactory;
import io.javalin.http.Context;

import java.time.LocalDate;
import java.util.List;

public class DoctorControllerDB {

    private final DoctorDAO dao;

    public DoctorControllerDB() {
        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        this.dao = DoctorDAO.getInstance(emf);
    }

    public void getAllDoctors(Context ctx) throws ApiException {
        List<DoctorDTO> doctors = dao.readAll();
        ctx.json(doctors);
    }

    public void getDoctorById(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        DoctorDTO doctor = dao.read(id);

        if (doctor != null) {
            ctx.json(doctor);
        } else {
            throw new ApiException(404, "Doctor not found");
        }
    }

    public void getDoctorsBySpeciality(Context ctx) throws ApiException {
        try {
            Speciality speciality = Speciality.valueOf(ctx.pathParam("speciality").toUpperCase());
            List<DoctorDTO> doctors = dao.doctorBySpeciality(speciality);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found for speciality " + speciality);
            } else {
                ctx.json(doctors);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid speciality provided.");
        }
    }

    public void getDoctorsByBirthdateRange(Context ctx) throws ApiException {
        try {
            LocalDate from = LocalDate.parse(ctx.queryParam("from"));
            LocalDate to = LocalDate.parse(ctx.queryParam("to"));
            List<DoctorDTO> doctors = dao.doctorByBirthdateRange(from, to);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found in the specified range.");
            } else {
                ctx.json(doctors);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Invalid date range.");
        }
    }

    public void createDoctor(Context ctx) throws ApiException {

        DoctorDTO doctorDTO = ctx.bodyAsClass(DoctorDTO.class);
        DoctorDTO createdDoctor = dao.create(doctorDTO);
        ctx.status(201).json(createdDoctor);

    }

    public void updateDoctor(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            DoctorDTO doctorDTO = ctx.bodyAsClass(DoctorDTO.class);
            DoctorDTO updatedDoctor = dao.update(id, doctorDTO);
            ctx.status(200).json(updatedDoctor);
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid doctor ID.");
        }

    }

    public void deleteDoctor(Context ctx) throws ApiException {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            boolean exists = dao.validatePrimaryKey(id);
            if (!exists) {
                throw new ApiException(404, "Doctor not found");
            }
            dao.delete(id);
            ctx.status(204); // No Content
        } catch (NumberFormatException e) {
            throw new ApiException(400, "Invalid doctor ID.");
        }
    }


    public void populate(Context ctx) throws ApiException {
        try {
            dao.populateDoctorsAndAppointments();
            ctx.status(200).json("Database populated successfully.");
        } catch (Exception e) {
            throw new ApiException(500, "Failed to populate database.");
        }
    }
}
