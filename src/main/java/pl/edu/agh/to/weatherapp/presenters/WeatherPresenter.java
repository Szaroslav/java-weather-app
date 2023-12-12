package pl.edu.agh.to.weatherapp.presenters;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.WindIntensity;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.util.List;

@Component
public class WeatherPresenter {
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
    private Label precipitationLabel;
    @FXML
    private SVGPath snowSVGPath;
    @FXML
    private SVGPath snowRainSVGPath;
    @FXML
    private Line noPrecipitationBackLine;
    @FXML
    private Line noPrecipitationLine;
    @FXML
    private TextField timeStartTextField;
    @FXML
    private TextField timeEndTextField;

    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TIME_INVALID = "Invalid time range";
    private static final String TEMP_SUFFIX = "°C";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final String WIND_SUFFIX = "m/s";
    private static final String PRECIPITATION_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";

    public WeatherPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void initialize() {
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
        timeEndTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        timeStartTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        hideWeatherInfo();
        clearErrorLabel();
        locationLabel.setText("");
        temperatureLabel.setText("");
        precipitationLabel.setText("");
        windLabel.setText("");
    }

    public void handleSearchAction() {
        if (searchTextField.getText().isEmpty()) {
            errorLabel.setText(FIELD_CANNOT_BE_EMPTY);
            return;
        }
        if (!isTimeValid(timeStartTextField.getText(), timeEndTextField.getText())) {
            errorLabel.setText(TIME_INVALID);
            return;
        }
        toggleSearchButtonVisibility();
        Task<InternalWeatherData> executeAppTask = new Task<>() {
            @Override
            protected InternalWeatherData call() {
                int startTime = timeStartTextField.getText().isEmpty() ? 0 : Integer.parseInt(timeStartTextField.getText());
                int endTime = timeEndTextField.getText().isEmpty() ? 24 : Integer.parseInt(timeEndTextField.getText());

                if (searchDestinationTextField.getText().isEmpty()) {
                    return weatherService.getWeatherData(searchTextField.getText(), startTime, endTime);
                } else {
                    return weatherService.getSummaryWeatherData(searchTextField.getText(), searchDestinationTextField.getText(), startTime, endTime);
                }
            }
        };
        executeAppTask.setOnSucceeded(e -> {
            InternalWeatherData weatherData = executeAppTask.getValue();
            clearErrorLabel();
            showLocation(weatherData.getLocationNames());
            showTemperature(String.valueOf(weatherData.getApparentTemperature()), weatherData.getTemperatureLevel());
            showPrecipitation(String.valueOf(weatherData.getPrecipitationInMm()), weatherData.getPrecipitationIntensity());
            showPrecipitationType(weatherData.getPrecipitationType());
            showWind(String.valueOf(weatherData.getWindInMps()), weatherData.getWindIntensity());
            showWeatherInfo();
            toggleSearchButtonVisibility();
        });
        executeAppTask.setOnFailed(e -> {
            if (executeAppTask.getException().getCause() != null) {
                errorLabel.setText(executeAppTask.getException().getCause().getMessage());
            } else {
                errorLabel.setText(executeAppTask.getException().getMessage());
            }
            toggleSearchButtonVisibility();
        });
        executeAppTask.setOnCancelled(e -> toggleSearchButtonVisibility());
        new Thread(executeAppTask).start();
    }

    private boolean isTimeValid(String timeStart, String timeEnd) {
        if (timeStart.isEmpty() ^ timeEnd.isEmpty()) {
            return false;
        }
        if (timeEnd.isEmpty()) {
            return true;
        }
        try {
            int startTime = Integer.parseInt(timeStart);
            int endTime = Integer.parseInt(timeEnd);
            return startTime <= endTime;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void showLocation(List<String> locationNames) {
        locationLabel.setText(locationNames.size() == 1 ? locationNames.get(0) : locationNames.get(0) + CITY_NAMES_SEPARATOR + locationNames.get(1));
    }

    private void showTemperature(String temperature, TemperatureLevel temperatureLevel) {
        String backgroundColorClass = switch (temperatureLevel) {
            case HOT -> "green";
            case WARM -> "orange";
            case COLD -> "red";
        };
        temperatureBox.getStyleClass().clear();
        temperatureBox.getStyleClass().add(backgroundColorClass);
        temperatureUnitLabel.setText(TEMP_SUFFIX);
        temperatureLabel.setText(temperature);
    }

    private void showWind(String windInMps, WindIntensity windIntensity) {
        String backgroundColor = switch (windIntensity) {
            case BREEZE -> COLOR_GREEN;
            case WINDY -> COLOR_ORANGE;
            case STORM -> COLOR_RED;
        };
        windSVGPath.setStroke(Color.web(backgroundColor));
        windLabel.setText(windInMps + WIND_SUFFIX);
        windLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
    }

    private void showPrecipitation(String precipitationInMm, PrecipitationIntensity precipitationIntensity) {
        String backgroundColor = switch (precipitationIntensity) {
            case WEAK -> COLOR_GREEN;
            case MEDIUM -> COLOR_ORANGE;
            case STRONG -> COLOR_RED;
        };
        precipitationLabel.setText(precipitationInMm + PRECIPITATION_SUFFIX);
        precipitationLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
    }

    private void showPrecipitationType(PrecipitationType precipitationType) {
        resetPrecipitationType();
        switch (precipitationType) {
            case SNOW -> {
                snowSVGPath.setVisible(true);
                snowSVGPath.setFill(Color.web(COLOR_RED));
            }
            case RAIN -> {
                rainSVGPath.setVisible(true);
                rainSVGPath.setFill(Color.web(COLOR_RED));
            }
            case BOTH -> {
                snowRainSVGPath.setVisible(true);
                snowRainSVGPath.setFill(Color.web(COLOR_RED));
            }
            case NONE -> {
                snowRainSVGPath.setVisible(true);
                snowRainSVGPath.setFill(Color.web(COLOR_GREEN));
                noPrecipitationBackLine.setVisible(true);
                noPrecipitationLine.setVisible(true);
                noPrecipitationLine.setFill(Color.web(COLOR_GREEN));
            }
        }
    }

    private void resetPrecipitationType() {
        snowSVGPath.setVisible(false);
        rainSVGPath.setVisible(false);
        snowRainSVGPath.setVisible(false);
        noPrecipitationBackLine.setVisible(false);
        noPrecipitationLine.setVisible(false);
    }

    private void hideWeatherInfo() {
        weatherInfoVBox.setVisible(false);
    }

    private void showWeatherInfo() {
        weatherInfoVBox.setVisible(true);
    }

    private void clearErrorLabel() {
        errorLabel.setText("");
    }

    private void toggleSearchButtonVisibility() {
        searchButton.setDisable(!searchButton.isDisabled());
    }
}
