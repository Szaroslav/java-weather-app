package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import org.springframework.stereotype.Component;
import pl.edu.agh.to.weatherapp.gui.customcontrols.LocationLabel;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

@Component
public class WeatherPresenter {
    @FXML
    private FavouritesPresenter favouritesController;
    @FXML
    private SearchPresenter searchController;
    @FXML
    private WeatherInfoPresenter weatherInfoController;


    public WeatherPresenter(WeatherService weatherService, FavouriteTrips trips) {

    }

    public void initialize() {
        favouritesController.injectMainController(this);
        searchController.injectMainController(this);
        weatherInfoController.injectMainController(this);
    }

    void onClickStarSVGPath() {
        favouritesController.onClickStarSVGPath();
    }

    void handleSearchAction() {
        searchController.handleSearchAction();
    }

    void updateWeatherData(Weather weatherData) {
            favouritesController.checkTripInFavourites(new Trip(weatherData.getLocationNames()));
            weatherInfoController.updateWeatherData(weatherData);
    }

    void insertTripToSearchField(Trip trip) {
        searchController.insertTripToSearchField(trip);
    }

    LocationLabel getLocationLabel(){
        return weatherInfoController.getLocationLabel();
    }

    public ObservableObjectValue<Color> getPaintProperty() {
        return favouritesController.getPaintProperty();
    }

}
