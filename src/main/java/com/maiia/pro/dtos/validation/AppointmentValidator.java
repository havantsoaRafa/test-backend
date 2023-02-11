package com.maiia.pro.dtos.validation;

import com.maiia.pro.dtos.AppointmentDTO;
import com.maiia.pro.exception.ValidationException;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class AppointmentValidator {


  public static void validate(AppointmentDTO appointmentDTO) {
    assertNotNull(appointmentDTO, AppointmentValidationMessage.APPOINTMENT_IS_NULL);
    assertNotNull(appointmentDTO.getPatientId(), AppointmentValidationMessage.PATIENT_ID_INVALID);
    assertNotNull(appointmentDTO.getPractitionerId(), AppointmentValidationMessage.PRACTITIONER_ID_INVALID);
    assertNotNull(appointmentDTO.getStartDate(), AppointmentValidationMessage.START_DATE_INVALID);
    assertNotNull(appointmentDTO.getEndDate(), AppointmentValidationMessage.END_DATE_INVALID);
    assertStartDateBeforeEndDate(appointmentDTO.getStartDate(), appointmentDTO.getEndDate());
  }



  private static void assertNotNull(Object object, AppointmentValidationMessage validationMessage) {
    if (Objects.isNull(object)) {
      throw new ValidationException(validationMessage.getMessage());
    }
  }

  private static void assertStartDateBeforeEndDate(LocalDateTime start, LocalDateTime end) {
    if (start.isAfter(end)) {
      throw new ValidationException(AppointmentValidationMessage.START_END_DATE_INVALID.getMessage());
    }
  }
}
