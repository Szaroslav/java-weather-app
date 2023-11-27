package pl.edu.agh.to.weatherapp.presenters;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.controllers.AppController;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.weather.IWeatherService;

@Component
public class WeatherPresenter  {
    private IWeatherService weatherService;
    private AppController appController;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private ImageView conditionIconImageView;

    public WeatherPresenter(IWeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void handleSearchAction() {
        Task<WeatherData> executeAppTask = new Task<WeatherData>() {
            @Override
            protected WeatherData call() throws Exception {
                return weatherService.getWeatherData(searchTextField.getText());
            }
        };
        executeAppTask.setOnSucceeded(e -> {
            locationLabel.setText(executeAppTask.getValue().getLocationName());
            temperatureLabel.setText(String.valueOf(executeAppTask.getValue().getTemp()) + "°C");
            conditionIconImageView.setImage(new Image(executeAppTask.getValue().getConditionIconUrl()));
        });
        executeAppTask.setOnFailed(e -> {
            //https://stackoverflow.com/questions/44398611/running-a-process-in-a-separate-thread-so-rest-of-java-fx-application-is-usable
            //TODO: Add label for errors
        });
        new Thread(executeAppTask).start();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
