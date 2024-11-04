package dat.controllers;

import dat.dtos.DoctorDTO;
import dat.daos.DoctorMockDAO;
import dat.enums.Speciality;
import dat.security.exceptions.ApiException;
import io.javalin.http.Context;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class DoctorMockController {
    private static DoctorMockDAO doctorDAO = new DoctorMockDAO();

    public static void getAllDoctors(Context ctx) {
        ctx.json(doctorDAO.readAll());
    }

    public static void getDoctorById(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        Optional<DoctorDTO> doctor = Optional.ofNullable(doctorDAO.read(id));

        if (doctor.isPresent()) {
            ctx.json(doctor.get());
        } else {
            throw new ApiException (404,"doctor not found");
        }
    }


    public static void getDoctorsBySpeciality(Context ctx) throws ApiException {
        try {
            Speciality speciality = Speciality.valueOf(ctx.pathParam("speciality").toUpperCase());
            List<DoctorDTO> doctors = doctorDAO.doctorBySpeciality(speciality);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found for speciality - /api/doctor/speciality/" + speciality);
            } else {
                ctx.json(doctors);
            }
        } catch (IllegalArgumentException e) {
            throw new ApiException(400, "Invalid speciality provided.");
        }
    }

    public static void getDoctorsByBirthdateRange(Context ctx) throws ApiException {
        try {
            LocalDate from = LocalDate.parse(ctx.queryParam("from"));
            LocalDate to = LocalDate.parse(ctx.queryParam("to"));
            List<DoctorDTO> doctors = doctorDAO.doctorByBirthdateRange(from, to);
            if (doctors.isEmpty()) {
                throw new ApiException(404, "No doctors found in the specified range.");
            } else {
                ctx.json(doctors);
            }
        } catch (Exception e) {
            throw new ApiException(400, "Invalid date range.");
        }
    }

    public static void createDoctor(Context ctx) throws ApiException {
        DoctorDTO doctor = ctx.bodyAsClass(DoctorDTO.class);
        if (doctor == null || doctor.getName() == null || doctor.getSpeciality() == null) {
            throw new ApiException(400, "Invalid doctor details provided.");
        }
        doctorDAO.create(doctor);
        ctx.status(201).json(doctor);
    }

    public static void updateDoctor(Context ctx) throws ApiException {
        int id = Integer.parseInt(ctx.pathParam("id"));
        DoctorDTO doctor = ctx.bodyAsClass(DoctorDTO.class);
        Optional<DoctorDTO> updatedDoctor = Optional.ofNullable(doctorDAO.update(id, doctor));

        if (updatedDoctor.isPresent()) {
            ctx.json(updatedDoctor.get());
        } else {
            throw new ApiException(404, "Doctor not found - /api/doctors/" + id);
        }
    }
}
