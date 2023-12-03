package pl.edu.agh.to.weatherapp.presenters;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.controllers.AppController;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

@Component
public class WeatherPresenter  {
    private final WeatherService weatherService;
    private AppController appController;
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private ImageView conditionIconImageView;
    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TEMP_SUFFIX = "°C";

    public WeatherPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void initialize(){
        searchTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
    }

    public void handleSearchAction() {
        if(searchTextField.getText().isEmpty()){
            errorLabel.setText(FIELD_CANNOT_BE_EMPTY);
            return;
        }
        Task<WeatherData> executeAppTask = new Task<>() {
            @Override
            protected WeatherData call() {
                return weatherService.getWeatherData(searchTextField.getText());
            }
        };
        searchButton.setDisable(true);
        locationLabel.setText("");
        temperatureLabel.setText("");
        conditionIconImageView.setImage(null);
        executeAppTask.setOnSucceeded(e -> {
            errorLabel.setText("");
            locationLabel.setText(executeAppTask.getValue().getLocationName());
            temperatureLabel.setText(executeAppTask.getValue().getTemp() + TEMP_SUFFIX);
            conditionIconImageView.setImage(new Image(executeAppTask.getValue().getConditionIconUrl()));
            searchButton.setDisable(false);
        });
        executeAppTask.setOnFailed(e -> {
            errorLabel.setText(executeAppTask.getException().getCause().getMessage());
            searchButton.setDisable(false);
        });
        executeAppTask.setOnCancelled(e ->
            searchButton.setDisable(false)
        );
        new Thread(executeAppTask).start();
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }
}
