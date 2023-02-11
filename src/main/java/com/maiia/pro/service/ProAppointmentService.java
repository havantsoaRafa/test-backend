package com.maiia.pro.service;

import com.maiia.pro.entity.Appointment;
import com.maiia.pro.entity.Availability;
import com.maiia.pro.repository.AppointmentRepository;
import com.maiia.pro.repository.AvailabilityRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProAppointmentService {
    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AvailabilityRepository availabilityRepository;

    public Appointment find(String appointmentId) {
        return appointmentRepository.findById(appointmentId).orElseThrow();
    }

    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    public List<Appointment> findByPractitionerId(Integer practitionerId) {
        return appointmentRepository.findByPractitionerId(practitionerId);
    }
    public Appointment add(Appointment appointment) {
        Appointment appointmentResult = appointmentRepository.save(appointment);
        Availability availability = availabilityRepository
            .findByPractitionerIdAndStartDateAndEndDate(appointmentResult.getPractitionerId(),
                appointment.getStartDate(),appointment.getEndDate());
        availabilityRepository.delete(availability);
        return appointmentResult;
    }
}
