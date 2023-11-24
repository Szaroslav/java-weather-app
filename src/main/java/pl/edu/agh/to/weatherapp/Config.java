package pl.edu.agh.to.weatherapp;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Config {
  public Config() {
    try {
      FileReader configReader = new FileReader("config.json");
    }
    catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}
