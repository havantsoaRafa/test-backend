package com.maiia.pro.repository;

import com.maiia.pro.entity.Availability;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvailabilityRepository extends CrudRepository<Availability, String> {
    List<Availability> findByPractitionerId(Integer id);
    Availability findByPractitionerIdAndStartDateAndEndDate(Integer id, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT a FROM Availability a WHERE  a.practitionerId =:practitionerId "
        + "AND ((a.startDate BETWEEN :startDate AND :endDate) "
        + "OR (a.endDate BETWEEN :startDate AND :endDate))")
    List<Availability> getAllAvailabilityBetweenDates(@Param("practitionerId") Integer practitionerId,
        @Param("startDate") LocalDateTime startDate,
        @Param("endDate")LocalDateTime endDate);
}
