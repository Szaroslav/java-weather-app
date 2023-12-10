package pl.edu.agh.to.weatherapp.presenters;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.model.internal.*;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.util.List;

@Component
public class WeatherPresenter  {
    private final WeatherService weatherService;
    @FXML
    private VBox weatherInfoVBox;
    @FXML
    private TextField searchTextField;
    @FXML
    private TextField searchDestinationTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label temperatureUnitLabel;
    @FXML
    private Label errorLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private StackPane temperatureBox;
    @FXML
    private SVGPath windSVGPath;
    @FXML
    private Label windLabel;
    @FXML
    private SVGPath rainSVGPath;
    @FXML
    private Label rainLabel;
    @FXML
    private SVGPath snowSVGPath;
    @FXML
    private Line noSnowBackLine;
    @FXML
    private Line noSnowLine;
    @FXML
    private ImageView conditionIconImageView;
    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TEMP_SUFFIX = "°C";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final String WIND = "2";
    private static final String WIND_SUFFIX = "m/s";
    private static final String RAIN = "9";
    private static final String RAIN_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";
    private static final String COLOR_GRAY = "#525252";

    public WeatherPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void initialize(){
        searchTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        searchDestinationTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        hideWeatherInfo();
        clearErrorLabel();
    }

    public void handleSearchAction() {
        if(searchTextField.getText().isEmpty()){
            errorLabel.setText(FIELD_CANNOT_BE_EMPTY);
            return;
        }
        Task<InternalWeatherData> executeAppTask = new Task<>() {
            @Override
            protected InternalWeatherData call() {
                return weatherService.getWeatherData(searchTextField.getText());
            }
        };
        toggleSearchButtonVisibility();
        executeAppTask.setOnSucceeded(e -> {
            InternalWeatherData weatherData = executeAppTask.getValue();
            clearErrorLabel();
            showLocation(weatherData.getLocationNames());
            showTemperature(String.valueOf(weatherData.getTemperature()), TemperatureLevel.COLD);
            showRain(RAIN, PrecipitationIntensity.STRONG);
            showSnow(PrecipitationType.SNOW);
            showWind(WIND, WindIntensity.STORM);
            setConditionIconImage(weatherData.getConditionIconUrl());
            showWeatherInfo();
            toggleSearchButtonVisibility();
        });
        executeAppTask.setOnFailed(e -> {
            if(executeAppTask.getException().getCause() != null){
                errorLabel.setText(executeAppTask.getException().getMessage());
            }else{
                errorLabel.setText(executeAppTask.getException().getCause().getMessage());
            }
            toggleSearchButtonVisibility();
        });
        executeAppTask.setOnCancelled(e ->
                toggleSearchButtonVisibility()
        );
        new Thread(executeAppTask).start();
    }
    private void showLocation(List<String> locationNames){
        locationLabel.setText(locationNames.size() == 1 ? locationNames.get(0): locationNames.get(0) + CITY_NAMES_SEPARATOR + locationNames.get(1));
    }

    private void showTemperature(String temperature, TemperatureLevel temperatureLevel){
        String backgroundColorClass = switch (temperatureLevel){
            case HOT -> "green";
            case WARM -> "orange";
            case COLD -> "red";
        };
        temperatureBox.getStyleClass().add(backgroundColorClass);
        temperatureUnitLabel.setText(TEMP_SUFFIX);
        temperatureLabel.setText(temperature);
    }

    private void showWind(String windInMps, WindIntensity windIntensity){
        String backgroundColor = switch (windIntensity){
            case BREEZE -> COLOR_GREEN;
            case WINDY -> COLOR_ORANGE;
            case STORM -> COLOR_RED;
        };
        windSVGPath.setStroke(Color.web(backgroundColor));
        windLabel.setText(windInMps + WIND_SUFFIX);
        windLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
    }

    private void showRain(String precipitationInMm, PrecipitationIntensity precipitationIntensity){
        String backgroundColor = switch (precipitationIntensity){
            case WEAK -> COLOR_GREEN;
            case MEDIUM -> COLOR_ORANGE;
            case STRONG -> COLOR_RED;
        };
        rainSVGPath.setFill(Color.web(backgroundColor));
        rainLabel.setText(precipitationInMm + RAIN_SUFFIX);
        rainLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
    }

    private void showSnow(PrecipitationType precipitationType){
        String backgroundColor;
        switch (precipitationType) {
            case SNOW, BOTH -> {
                backgroundColor = COLOR_RED;
                noSnowBackLine.setVisible(false);
                noSnowLine.setVisible(false);
            }
            case NONE, RAIN -> {
                backgroundColor = COLOR_GREEN;
                noSnowBackLine.setVisible(true);
                noSnowLine.setVisible(true);
                noSnowLine.setStroke(Color.web(backgroundColor));
            }
            default -> backgroundColor = COLOR_GRAY;
        }
        snowSVGPath.setFill(Color.web(backgroundColor));
    }

    private void hideWeatherInfo(){
        weatherInfoVBox.setVisible(false);
    }
    private void showWeatherInfo(){
        weatherInfoVBox.setVisible(true);
    }

    private void clearErrorLabel(){
        errorLabel.setText("");
    }

    private void setConditionIconImage(String imageUrl){
        conditionIconImageView.setImage(new Image(imageUrl));
    }
    private void toggleSearchButtonVisibility(){
        searchButton.setDisable(!searchButton.isDisabled());
    }
}
