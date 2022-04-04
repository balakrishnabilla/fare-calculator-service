package com.farecalculator.validator;

import com.farecalculator.exception.ApplicationException;
import com.farecalculator.constants.Constants;
import com.farecalculator.model.Zone;
import com.farecalculator.utils.MessageResourceUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DefaultValidator implements Validator<String> {
  static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Constants.DATE_FORMATTER);
  static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.TIME_FORMATTER);
  private String journey = null;

  @Override
  public void validate(String journey) throws ApplicationException {
    if (journey.isEmpty()) {
      return;
    }
    this.journey = journey;
    String[] data = journey.split(" ");
    if (data.length != 4) {
      throwAppException("DefaultValidator.validate.input.format.001");
    }
    validateDate(data[0]);
    validateTime(data[1]);
    validateFromZone(data[2]);
    validateToZone(data[3]);
  }

  private void validateFromZone(String zoneStr) throws ApplicationException {
    int zoneNUm = validateZoneNum(zoneStr, "DefaultValidator.validateFromZone.001");
    validateAcceptedZone(zoneStr, zoneNUm, "DefaultValidator.validateFromZone.002");
  }

  private void validateToZone(String zoneStr) throws ApplicationException {
    int zoneNUm = validateZoneNum(zoneStr, "DefaultValidator.validateToZone.001");
    validateAcceptedZone(zoneStr, zoneNUm, "DefaultValidator.validateToZone.002");
  }

  private void validateAcceptedZone(String zoneStr, int zoneNUm, String s)
      throws ApplicationException {
    try {
      Zone.of(zoneNUm);
    } catch (IllegalArgumentException exception) {
      throwAppException(s, zoneStr);
    }
  }

  private int validateZoneNum(String zoneStr, String s) throws ApplicationException {
    int zoneNUm = -1;
    try {
      zoneNUm = Integer.parseInt(zoneStr);
    } catch (NumberFormatException numberFormatException) {
      throwAppException(s);
    }
    return zoneNUm;
  }

  private void validateTime(String time) throws ApplicationException {
    try {
      LocalTime.parse(time, timeFormatter);
    } catch (RuntimeException ex) {
      throwAppException("DefaultValidator.validateTime.001", Constants.TIME_FORMATTER);
    }
  }

  private void validateDate(String date) throws ApplicationException {
    try {
      LocalDate.parse(date, dateFormatter);
    } catch (RuntimeException ex) {
      throwAppException("DefaultValidator.validateDate.001", Constants.DATE_FORMATTER);
    }
  }

  private void throwAppException(String messageKey) throws ApplicationException {
    throw new ApplicationException(
        MessageResourceUtil.getMessage(messageKey, new String[] {this.journey}));
  }

  private void throwAppException(String messageKey, String param) throws ApplicationException {
    throw new ApplicationException(
        MessageResourceUtil.getMessage(messageKey, new String[] {param, this.journey}));
  }
}
