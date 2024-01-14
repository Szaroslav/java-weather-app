package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import pl.edu.agh.to.weatherapp.gui.customcontrols.LocationLabel;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;

import java.util.List;

public class WeatherInfoPresenter {
    @FXML
    private VBox weatherInfoVBox;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label temperatureUnitLabel;
    @FXML
    private LocationLabel locationLabel;
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
    private SVGPath mudSVGPath;
    @FXML
    private Line noMudBackLine;
    @FXML
    private Line noMudLine;
    @FXML
    private SVGPath starSVGPath;
    private WeatherPresenter mainController;
    private static final String TEMP_SUFFIX = "°C";
    private static final String WIND_SUFFIX = "m/s";
    private static final String PRECIPITATION_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_YELLOW = "#FFD500";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";

    public WeatherInfoPresenter() {
        // Empty constructor for javafx
    }

    public void injectMainController(WeatherPresenter mainController) {
        this.mainController = mainController;
        starSVGPath.fillProperty().bind(mainController.getPaintProperty());
    }

    public void initialize() {
        locationLabel.setText("");
        temperatureLabel.setText("");
        precipitationLabel.setText("");
        windLabel.setText("");
        hideWeatherInfo();
    }

    @FXML
    private void onClickStarSVGPath() {
        mainController.onClickStarSVGPath();
    }

    void updateWeatherData(Weather weatherData) {
        showLocation(weatherData.getLocationNames());
        showTemperature(String.valueOf(weatherData.getApparentTemperature()), weatherData.getTemperatureLevel());
        showPrecipitation(weatherData.getPrecipitationType(),
                String.valueOf(weatherData.getPrecipitationInMm()),
                weatherData.getPrecipitationIntensity());
        showWind(String.valueOf(weatherData.getWindInMps()), weatherData.getWindIntensity());
        showMud(weatherData.isMud());
        showWeatherInfo();
    }

    private void showLocation(List<String> locationNames) {
        locationLabel.setNames(locationNames);
    }

    private void showTemperature(String temperature, TemperatureLevel temperatureLevel) {
        String backgroundColorClass = switch (temperatureLevel) {
            case HOT -> "green";
            case WARM -> "yellow";
            case COLD -> "orange";
            case FREEZING -> "red";
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

    private void showMud(boolean isMudPresent) {
        if (isMudPresent) {
            mudSVGPath.setFill(Color.web(COLOR_RED));
            noMudBackLine.setVisible(false);
            noMudLine.setVisible(false);
        } else {
            mudSVGPath.setFill(Color.web(COLOR_GREEN));
            noMudBackLine.setVisible(true);
            noMudLine.setVisible(true);
        }
    }

    private void showPrecipitation(PrecipitationType precipitationType, String precipitationInMm, PrecipitationIntensity precipitationIntensity) {
        resetPrecipitationType();
        String backgroundColor = switch (precipitationIntensity) {
            case WEAK -> COLOR_YELLOW;
            case MEDIUM -> COLOR_ORANGE;
            case STRONG -> COLOR_RED;
        };
        switch (precipitationType) {
            case SNOW -> {
                snowSVGPath.setVisible(true);
                snowSVGPath.setFill(Color.web(backgroundColor));
                precipitationLabel.setText(precipitationInMm + PRECIPITATION_SUFFIX);
                precipitationLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
            }
            case RAIN -> {
                rainSVGPath.setVisible(true);
                rainSVGPath.setFill(Color.web(backgroundColor));
                precipitationLabel.setText(precipitationInMm + PRECIPITATION_SUFFIX);
                precipitationLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
            }
            case BOTH -> {
                snowRainSVGPath.setVisible(true);
                snowRainSVGPath.setFill(Color.web(backgroundColor));
                precipitationLabel.setText(precipitationInMm + PRECIPITATION_SUFFIX);
                precipitationLabel.textFillProperty().setValue(Paint.valueOf(backgroundColor));
            }
            case NONE -> {
                snowRainSVGPath.setVisible(true);
                snowRainSVGPath.setFill(Color.web(COLOR_GREEN));
                noPrecipitationBackLine.setVisible(true);
                noPrecipitationLine.setVisible(true);
                noPrecipitationLine.setFill(Color.web(COLOR_GREEN));
                precipitationLabel.setText(precipitationInMm + PRECIPITATION_SUFFIX);
                precipitationLabel.textFillProperty().setValue(Paint.valueOf(COLOR_GREEN));
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

    private void showWeatherInfo() {
        weatherInfoVBox.setVisible(true);
    }

    private void hideWeatherInfo() {
        weatherInfoVBox.setVisible(false);
    }

    LocationLabel getLocationLabel() {
        return locationLabel;
    }
}
