package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.gui.customcontrols.LocationLabel;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class WeatherPresenter {
    private final WeatherService weatherService;
    private final FavouriteTrips trips;
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

    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TIME_INVALID = "Invalid time range";
    private static final String TEMP_SUFFIX = "°C";
    private static final String WIND_SUFFIX = "m/s";
    private static final String PRECIPITATION_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_YELLOW = "#FFD500";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";

    private final SimpleBooleanProperty isCurrentTripInFavourites = new SimpleBooleanProperty(false);
    final ObservableObjectValue<Color> paintProperty = Bindings.when(isCurrentTripInFavourites)
            .then(Color.web(COLOR_ORANGE))
            .otherwise(Color.web(COLOR_ORANGE, 0));

    public WeatherPresenter(WeatherService weatherService, FavouriteTrips trips) {
        this.weatherService = weatherService;
        this.trips = trips;
    }

    public void initialize() {
        starSVGPath.fillProperty().bind(paintProperty);
        favouritesListView.setCellFactory(param -> new TripCell() {
            @Override
            protected void pressDeleteButtonHandler() {
                if (getItem() != null) {
                    Trip item = getItem();
                    if (!Objects.equals(locationLabel.getText(), "") && item.equals(new Trip(locationLabel.getNames()))) {
                        isCurrentTripInFavourites.set(false);
                    }
                    trips.deleteTrip(item);
                }
            }
        });
        favouritesListView.setOnMouseClicked(event ->
                favouritesListView.getSelectionModel().clearSelection()
        );
        favouritesListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                insertTripToSearchField(newValue);
                handleSearchAction();
            }
        });
        favouritesListView.setItems(trips.getTrips());
        hideWeatherInfo();
        clearErrorLabel();
        locationLabel.setText("");
        temperatureLabel.setText("");
        precipitationLabel.setText("");
        windLabel.setText("");
    }

    @FXML
    private void onClickStarSVGPath() {
        if (isCurrentTripInFavourites.get()) {
            trips.deleteTrip(new Trip(locationLabel.getNames()));
        } else {
            trips.addTrip(new Trip(locationLabel.getNames()));
        }
        isCurrentTripInFavourites.set(!isCurrentTripInFavourites.get());
    }

    @FXML
    private void onPressEnterSearch(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)) {
            handleSearchAction();
        }
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
                int startTime =
                        timeStartTextField.getText().isEmpty() ? 0 : Integer.parseInt(timeStartTextField.getText());
                int endTime = timeEndTextField.getText().isEmpty() ? 24 : Integer.parseInt(timeEndTextField.getText());

                List<String> locationsToSearch = new ArrayList<>();
                if (!searchStartTextField.getText().isEmpty()) {
                    locationsToSearch.add(searchStartTextField.getText());
                }
                if (!searchMiddleTextField.getText().isEmpty()) {
                    locationsToSearch.add(searchMiddleTextField.getText());
                }
                if (!searchDestinationTextField.getText().isEmpty()) {
                    locationsToSearch.add(searchDestinationTextField.getText());
                }
                return weatherService.getForecastSummaryWeatherData(locationsToSearch, startTime, endTime);
            }
        };
        executeAppTask.setOnSucceeded(e -> {
            Weather weatherData = executeAppTask.getValue();
            isCurrentTripInFavourites.set(trips.getTrips().contains(new Trip(weatherData.getLocationNames())));
            clearErrorLabel();
            showLocation(weatherData.getLocationNames());
            showTemperature(String.valueOf(weatherData.getApparentTemperature()), weatherData.getTemperatureLevel());
            showPrecipitation(weatherData.getPrecipitationType(),
                    String.valueOf(weatherData.getPrecipitationInMm()),
                    weatherData.getPrecipitationIntensity());
            showWind(String.valueOf(weatherData.getWindInMps()), weatherData.getWindIntensity());
            showMud(weatherData.isMud());
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

    private void insertTripToSearchField(Trip trip) {
        List<String> tripLocations = trip.locationNames();
        clearTextFields();
        if (!tripLocations.isEmpty()) {
            searchStartTextField.setText(tripLocations.get(0));
        }
        if (tripLocations.size() > 1) {
            searchMiddleTextField.setText(tripLocations.get(1));
        }
        if (tripLocations.size() > 2) {
            searchDestinationTextField.setText(tripLocations.get(2));
        }
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

    private void clearTextFields() {
        searchStartTextField.clear();
        searchMiddleTextField.clear();
        searchDestinationTextField.clear();
    }

}
