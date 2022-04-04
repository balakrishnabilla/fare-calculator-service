package com.farecalculator.utils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.List;

public class FileUtil {
  public List<String> readFile(File file) throws IOException {
    return Files.readAllLines(file.toPath());
  }

  public File getFile(String fileName) {
    ClassLoader classLoader = getClass().getClassLoader();
    URL resource = classLoader.getResource(fileName);
    if (resource == null) {
      throw new IllegalArgumentException("file is not found!");
    } else {
      return new File(resource.getFile());
    }
  }
}
