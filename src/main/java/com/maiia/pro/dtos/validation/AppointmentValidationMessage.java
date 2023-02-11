package com.maiia.pro.dtos.validation;

public enum AppointmentValidationMessage {
  APPOINTMENT_IS_NULL("APT001", "Appointment Object is null"),
  PATIENT_ID_INVALID("APT002", "Patient Id is not valid"),
  PRACTITIONER_ID_INVALID("APT003", "Practitioner Id is not valid"),
  START_DATE_INVALID("APT004", "Start Date is not valid"),
  END_DATE_INVALID("APT005", "End Date is not valid"),
  START_END_DATE_INVALID("APT006", "End date must be after Start date");

  private final String code;
  private final String description;

  AppointmentValidationMessage(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }

  public String getMessage() {
    return getCode() + " : " + getDescription();
  }
}
