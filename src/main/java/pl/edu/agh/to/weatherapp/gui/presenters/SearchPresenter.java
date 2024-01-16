package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.util.ArrayList;
import java.util.List;

public class SearchPresenter {
    private static final String FIELD_CANNOT_BE_EMPTY = "Search field cannot be empty";
    private static final String TIME_INVALID = "Invalid time range";
    private final WeatherService weatherService;
    @FXML
    private TextField searchStartTextField;
    @FXML
    private TextField searchMiddleTextField;
    @FXML
    private TextField searchDestinationTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label errorLabel;
    @FXML
    private TextField timeStartTextField;
    @FXML
    private TextField timeEndTextField;
    private WeatherPresenter mainController;

    public SearchPresenter(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    public void injectMainController(WeatherPresenter mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        clearErrorLabel();
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
            clearErrorLabel();
            mainController.updateWeatherData(weatherData);
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

    void insertTripToSearchField(Trip trip) {
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
