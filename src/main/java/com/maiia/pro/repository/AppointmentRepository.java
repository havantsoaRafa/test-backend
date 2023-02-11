package com.maiia.pro.repository;

import com.maiia.pro.entity.Appointment;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends CrudRepository<Appointment, String> {
    List<Appointment> findByPractitionerId(Integer practitionerId);
    List<Appointment> findAll();

    @Query("SELECT a FROM Appointment a WHERE  a.practitionerId =:practitionerId "
        + "AND ((a.startDate BETWEEN :startDate AND :endDate) "
        + "OR (a.endDate BETWEEN :startDate AND :endDate))")
    List<Appointment> getAllAppointmentBetweenDates(@Param("practitionerId") Integer practitionerId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate")LocalDateTime endDate);
}
