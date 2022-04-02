package com.farecalulator.validator;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateValidator implements Validator<String> {
  static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");

  @Override
  public void validate(String dateStr) {

  }
}
