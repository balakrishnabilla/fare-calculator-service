package com.farecalculator.exception;

public class ApplicationException extends Exception {
  public ApplicationException(String message) {
    super(message);
  }
  public ApplicationException(Throwable message) {
    super(message);
  }
}
