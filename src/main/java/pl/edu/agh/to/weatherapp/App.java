package pl.edu.agh.to.weatherapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;

import java.io.IOException;

public class App extends Application  {
  private Stage primaryStage;
  private AppController appController;

  @Override
  public void start(Stage primaryStage) {

    this.primaryStage = primaryStage;
    this.primaryStage.setTitle("My first JavaFX app");

    this.appController = new AppController(primaryStage);
    this.appController.initRootLayout();

  }
}
