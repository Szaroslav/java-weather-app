package pl.edu.agh.to.weatherapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;


public class AppController {
    private WeatherData weatherData;
    private WeatherService weatherService;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label locationLabel;
    private Stage primaryStage;

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void initRootLayout() {
        try {
            this.primaryStage.setTitle("My second JavaFX app");

            // load layout from FXML file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
            AnchorPane rootLayout = loader.load();

            // set initial data into controller
            WeatherPresenter controller = loader.getController();
            controller.setAppController(this);
            controller.setData(new WeatherData());

            // add layout to a scene and show them all
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            // don't do this in common apps
            throw new RuntimeException(e);
        }

    }

}
