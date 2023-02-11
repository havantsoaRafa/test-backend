package com.maiia.pro.dtos.mappers;

import com.maiia.pro.dtos.AppointmentDTO;
import com.maiia.pro.entity.Appointment;
import org.modelmapper.PropertyMap;

public abstract class CustomMapper {

  public static PropertyMap<AppointmentDTO, Appointment> skipIDFieldsMap = new PropertyMap<>() {
    protected void configure() {
      skip().setId(null);
    }
  };
}
