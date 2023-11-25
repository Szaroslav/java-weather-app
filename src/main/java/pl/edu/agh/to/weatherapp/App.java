package pl.edu.agh.to.weatherapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
  private Config config;

  @Override
  public void init() {
      try {
        config = new Config();
        config.init();
      }
      catch (IOException e) {
        System.out.println(e);
      }
  }

  @Override
  public void start(Stage primaryStage) {
    Scene scene = new Scene(new Label("test"));
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
