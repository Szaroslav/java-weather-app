package pl.edu.agh.to.weatherapp.presenters;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.model.Trip;
import pl.edu.agh.to.weatherapp.model.TripMemory;
import pl.edu.agh.to.weatherapp.model.Weather;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.util.List;

@Component
public class WeatherPresenter {
    private final WeatherService weatherService;
    @FXML
    private VBox weatherInfoVBox;
    @FXML
    private TextField searchStartTextField;
    @FXML
    private TextField searchMiddleTextField;
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
    @FXML
    private SVGPath mudSVGPath;
    @FXML
    private Line noMudBackLine;
    @FXML
    private Line noMudLine;
    @FXML
    private ListView<Trip> favouritesListView;
    @FXML
    private SVGPath starSVGPath;
    private TripMemory trips;

    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TIME_INVALID = "Invalid time range";
    private static final String TEMP_SUFFIX = "°C";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final String WIND_SUFFIX = "m/s";
    private static final String PRECIPITATION_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_YELLOW = "#FFD500";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";

    private Trip lastSearch = null;
    //private boolean favourite = false;
    private BooleanProperty favourite = new SimpleBooleanProperty(false);

    public WeatherPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
        this.trips = new TripMemory();

        Trip sampleTrip = new Trip();
        sampleTrip.getLocationNames().add("Tarnów");
        sampleTrip.getLocationNames().add("Ryglice");
        sampleTrip.getLocationNames().add("Bistuszowa");
        this.trips.addTrip(sampleTrip);
        Trip sampleTrip2 = new Trip();
        sampleTrip2.getLocationNames().add("Tarnów");
        sampleTrip2.getLocationNames().add("Bistuszowa");
        this.trips.addTrip(sampleTrip2);
    }

    public void initialize() {
        favouritesListView.setCellFactory(param -> new TripCell(trips));
        favouritesListView.setOnMouseClicked(event -> favouritesListView.getSelectionModel().clearSelection());
        favouritesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null)
            {
                insertTripToSearchField(newValue);
            }
        });
        favourite.bindBidirectional(trips.getCurrentTripFavourite());
        favourite.addListener((observable, oldValue, newValue) -> {
            System.out.println("Changed");
            updateStarSVG();
        });
        favouritesListView.setItems(trips.getTrips());
        starSVGPath.setOnMouseClicked(event -> {
            if(favourite.get()){
                trips.deleteTrip(lastSearch);
            }else{
                trips.addTrip(lastSearch);
            }
        });
        searchStartTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        searchDestinationTextField.setOnKeyPressed(key -> {
            if (key.getCode().equals(KeyCode.ENTER)) {
                handleSearchAction();
            }
        });
        searchMiddleTextField.setOnKeyPressed(key -> {
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
        if (searchStartTextField.getText().isEmpty()) {
            errorLabel.setText(FIELD_CANNOT_BE_EMPTY);
            return;
        }
        if (!isTimeValid(timeStartTextField.getText(), timeEndTextField.getText())) {
            errorLabel.setText(TIME_INVALID);
            return;
        }
        toggleSearchButtonVisibility();
        Task<Weather> executeAppTask = new Task<>() {
            @Override
            protected Weather call() {
                int startTime = timeStartTextField.getText().isEmpty() ? 0 : Integer.parseInt(timeStartTextField.getText());
                int endTime = timeEndTextField.getText().isEmpty() ? 24 : Integer.parseInt(timeEndTextField.getText());

                if (searchMiddleTextField.getText().isEmpty()) {
                    return weatherService.getWeatherData(searchStartTextField.getText(), startTime, endTime);
                } else {
                    if(searchDestinationTextField.getText().isEmpty()){
                        return weatherService.getSummaryWeatherData(searchStartTextField.getText(), searchMiddleTextField.getText(), startTime, endTime);
                    }else{
                        //TODO: Add third destination
                        return weatherService.getSummaryWeatherData(searchStartTextField.getText(), searchMiddleTextField.getText(), startTime, endTime);
                    }
                }
            }
        };
        executeAppTask.setOnSucceeded(e -> {
            Weather weatherData = executeAppTask.getValue();
            Trip tmpLastSearch = new Trip().setLocationNames(weatherData.getLocationNames());
            Trip checkedTrip = trips.checkIfTripPresent(tmpLastSearch);
            if(checkedTrip != null){
                lastSearch = checkedTrip;
            }else{
                lastSearch = tmpLastSearch;
            }
            updateStarSVG();

            clearErrorLabel();
            showLocation(weatherData.getLocationNames());
            showTemperature(String.valueOf(weatherData.getApparentTemperature()), weatherData.getTemperatureLevel());
            showPrecipitation(weatherData.getPrecipitationType(),
                    String.valueOf(weatherData.getPrecipitationInMm()),
                    weatherData.getPrecipitationIntensity());
            showWind(String.valueOf(weatherData.getWindInMps()), weatherData.getWindIntensity());
            //TODO: Change to value from weatherData
            showMud(true);
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
    private void insertTripToSearchField(Trip trip){
        List<String> tripLocations = trip.getLocationNames();
        clearTextFields();
        if(!tripLocations.isEmpty()){
            searchStartTextField.setText(tripLocations.get(0));
        }
        if(tripLocations.size() > 1){
            searchMiddleTextField.setText(tripLocations.get(1));
        }
        if(tripLocations.size() > 2){
            searchDestinationTextField.setText(tripLocations.get(2));
        }
    }

    private void showLocation(List<String> locationNames) {
        locationLabel.setText(locationNames.size() == 1 ? locationNames.get(0) : locationNames.get(0) + CITY_NAMES_SEPARATOR + locationNames.get(1));
    }
    private void updateStarSVG(){
        if(favourite.get()){
            starSVGPath.setFill(Color.web(COLOR_ORANGE));
        }else{
            starSVGPath.setFill(Color.web(COLOR_ORANGE, 0));
        }
    }

    private void showTemperature(String temperature, TemperatureLevel temperatureLevel) {
        //TODO: add fourth color
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
    private void showMud(boolean isMudPresent){
        if(isMudPresent){
            mudSVGPath.setFill(Color.web(COLOR_RED));
            noMudBackLine.setVisible(false);
            noMudLine.setVisible(false);
        }else{
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

    private void clearTextFields(){
        searchStartTextField.clear();
        searchMiddleTextField.clear();
        searchDestinationTextField.clear();
    }
}
