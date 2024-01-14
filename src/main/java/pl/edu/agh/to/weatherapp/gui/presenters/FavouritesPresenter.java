package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableObjectValue;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.paint.Color;
import pl.edu.agh.to.weatherapp.gui.customcontrols.TripCell;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

import java.util.Objects;

public class FavouritesPresenter {
    private static final String COLOR_ORANGE = "#FFB347";
    private final SimpleBooleanProperty isCurrentTripInFavourites = new SimpleBooleanProperty(false);
    private final ObservableObjectValue<Color> paintProperty = Bindings.when(isCurrentTripInFavourites)
            .then(Color.web(COLOR_ORANGE))
            .otherwise(Color.web(COLOR_ORANGE, 0));
    private final FavouriteTrips trips;
    @FXML
    private ListView<Trip> favouritesListView;
    private WeatherPresenter mainController;

    public FavouritesPresenter(FavouriteTrips trips) {
        this.trips = trips;
    }

    public void injectMainController(WeatherPresenter mainController) {
        this.mainController = mainController;
    }

    public void initialize() {
        favouritesListView.setCellFactory(param -> new TripCell() {
            @Override
            protected void pressDeleteButtonHandler() {
                if (getItem() != null) {
                    Trip item = getItem();
                    if (!Objects.equals(mainController.getLocationLabel().getText(), "") && item.equals(new Trip(mainController.getLocationLabel().getNames()))) {
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
                mainController.insertTripToSearchField(newValue);
                mainController.handleSearchAction();
            }
        });
        favouritesListView.setItems(trips.getTrips());
    }

    void onClickStarSVGPath() {
        if (isCurrentTripInFavourites.get()) {
            trips.deleteTrip(new Trip(mainController.getLocationLabel().getNames()));
        } else {
            trips.addTrip(new Trip(mainController.getLocationLabel().getNames()));
        }
        isCurrentTripInFavourites.set(!isCurrentTripInFavourites.get());
    }

    void checkTripInFavourites(Trip trip) {
        isCurrentTripInFavourites.set(trips.getTrips().contains(trip));
    }

    public ObservableObjectValue<Color> getPaintProperty() {
        return paintProperty;
    }
}
