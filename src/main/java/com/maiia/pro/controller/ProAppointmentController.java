package com.maiia.pro.controller;

import com.maiia.pro.dtos.AppointmentDTO;
import com.maiia.pro.dtos.validation.AppointmentValidator;
import com.maiia.pro.entity.Appointment;
import com.maiia.pro.service.ProAppointmentService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping(value = "/appointments", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProAppointmentController {
    private final ProAppointmentService proAppointmentService;

    private final ModelMapper modelMapper;

    public ProAppointmentController(ProAppointmentService proAppointmentService,
        ModelMapper modelMapper) {
        this.proAppointmentService = proAppointmentService;
        this.modelMapper = modelMapper;
    }

    @ApiOperation(value = "Get appointments by practitionerId")
    @GetMapping("/{practitionerId}")
    public List<Appointment> getAppointmentsByPractitioner(@PathVariable final Integer practitionerId) {
        return proAppointmentService.findByPractitionerId(practitionerId);
    }

    @ApiOperation(value = "Get all appointments")
    @GetMapping
    public List<Appointment> getAppointments() {
        return proAppointmentService.findAll();
    }

    @ApiOperation(value = "Add all appointments")
    @PostMapping
    public AppointmentDTO addAppointments(@RequestBody AppointmentDTO appointmentDTO) {
        AppointmentValidator.validate(appointmentDTO);
        Appointment appointment = modelMapper.map(appointmentDTO, Appointment.class);
        return modelMapper.map(proAppointmentService.add(appointment), AppointmentDTO.class);
    }
}
