package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.entity.StartEndDate;
import com.maiia.pro.entity.TimeSlot;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import com.maiia.pro.repository.TimeSlotRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProAvailabilityService {

    @Autowired
    private AvailabilityRepository availabilityRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    public List<Availability> findByPractitionerId(Integer practitionerId) {
        return availabilityRepository.findByPractitionerId(practitionerId);
    }

    public List<Availability> generateAvailabilities(Integer practitionerId) {
        // TODO : implement this
        List<TimeSlot> practitionerTimeSlots = getPractitionerTimeSlots(practitionerId);

        practitionerTimeSlots.forEach(timeSlot -> {
            List<StartEndDate> unavailableTimeSlotList = getUnavailableTimeSlot(timeSlot);
            algoToAddAvailability(practitionerId, timeSlot, unavailableTimeSlotList);
        });

        return findByPractitionerId(practitionerId);
    }

    private List<StartEndDate> getUnavailableTimeSlot(TimeSlot timeSlot) {
        List<Availability> availabilityList =  availabilityRepository.getAllAvailabilityBetweenDates(
            timeSlot.getPractitionerId(), timeSlot.getStartDate(), timeSlot.getEndDate());
        List<Appointment> appointmentList = appointmentRepository.getAllAppointmentBetweenDates(
            timeSlot.getPractitionerId(), timeSlot.getStartDate(), timeSlot.getEndDate());

        return concatToSortedStartEndDates(
            availabilityList, appointmentList);
    }

    private List<StartEndDate> concatToSortedStartEndDates(List<Availability> availabilityList,
        List<Appointment> appointmentList) {
        List<StartEndDate> startEndDateList = availabilityList.stream().map(availability -> StartEndDate.builder()
                .startDate(availability.getStartDate())
                .endDate(availability.getEndDate())
                .build())
            .collect(Collectors.toList());

        List<StartEndDate> appointmentStartEndDateList = appointmentList.stream().map(appointment -> StartEndDate.builder()
                .startDate(appointment.getStartDate())
                .endDate(appointment.getEndDate())
                .build())
            .collect(Collectors.toList());

        startEndDateList.addAll(appointmentStartEndDateList);
        startEndDateList.sort(Comparator.comparing(StartEndDate::getStartDate));
        return startEndDateList;
    }

    private void algoToAddAvailability(Integer practitionerId, TimeSlot timeSlot,
        List<StartEndDate> unavailableTimeSlotList) {
        LocalDateTime startDate = timeSlot.getStartDate();
        for (StartEndDate unavailable : unavailableTimeSlotList) {
            LocalDateTime endDate = unavailable.getStartDate();

            if(startDate.isAfter(endDate)){
                startDate = unavailable.getEndDate();
            }else {
                addAvailability(startDate, endDate, practitionerId, timeSlot.getEndDate());
                startDate = unavailable.getEndDate();
            }
        }
        if(startDate.isBefore(timeSlot.getEndDate())){
            addAvailability(startDate, timeSlot.getEndDate(), practitionerId, timeSlot.getEndDate());
        }

    }

    private List<TimeSlot> getPractitionerTimeSlots(Integer practitionerId) {
        List<TimeSlot> timeSlots = timeSlotRepository.findByPractitionerId(practitionerId);
        return timeSlots
            .stream()
            .filter(timeSlot -> timeSlot.getPractitionerId().equals(practitionerId))
            .collect(Collectors.toList());
    }

    private void addAvailability(LocalDateTime startDate, LocalDateTime endDate, Integer practitionerId, LocalDateTime timeSlotEndDate){
        LocalDateTime endAvailabilityDate = startDate.plusMinutes(15);
        if(endAvailabilityDate.isBefore(endDate) || endAvailabilityDate.isEqual(endDate)){
            availabilityRepository.save(Availability
                .builder()
                .practitionerId(practitionerId)
                .startDate(startDate)
                .endDate(endAvailabilityDate)
                .build());
            addAvailability(endAvailabilityDate,endDate, practitionerId, timeSlotEndDate);
        } else {
            addOptimalAvailability(startDate, endDate, practitionerId, timeSlotEndDate);
        }
    }
    private void addOptimalAvailability(LocalDateTime startDate, LocalDateTime endDate, Integer practitionerId, LocalDateTime timeSlotEndDate){
        if(endDate.isEqual(timeSlotEndDate)){
            long minutes = ChronoUnit.MINUTES.between(startDate, endDate);
            if(minutes >= 10){
                availabilityRepository.save(Availability
                    .builder()
                    .practitionerId(practitionerId)
                    .startDate(startDate)
                    .endDate(startDate.plusMinutes(15))
                    .build());
            }
        }
    }
}
