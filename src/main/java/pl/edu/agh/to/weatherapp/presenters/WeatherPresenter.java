package pl.edu.agh.to.weatherapp.presenters;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import pl.edu.agh.to.weatherapp.AppController;
import pl.edu.agh.to.weatherapp.WeatherData;
import pl.edu.agh.to.weatherapp.weather.WeatherService;


public class WeatherPresenter  {
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
    private AppController appController;


    public void handleSearchAction(ActionEvent event) {
        WeatherData data = weatherService.getWeatherData(searchTextField.getText());
        locationLabel.setText(data.getCityName());
        temperatureLabel.setText(String.valueOf(data.getTemp()));
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public void setData(WeatherData weatherData) {
        this.weatherData = weatherData;
    }

}
