package com.farecalculator.utils;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class MessageResourceUtil {

  private MessageResourceUtil() {
    throw new IllegalStateException("Utility class");
  }

  public static String getMessage(String key, String[] paramValues) {
    String message = ResourceBundle.getBundle("application").getString(key);
    MessageFormat messageFormat = new MessageFormat(message);
    return messageFormat.format(paramValues);
  }

  public static String getMessage(String key) {
    return ResourceBundle.getBundle("application").getString(key);
  }
}
