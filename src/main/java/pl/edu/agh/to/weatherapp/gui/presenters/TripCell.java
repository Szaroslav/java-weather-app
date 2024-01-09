package pl.edu.agh.to.weatherapp.gui.presenters;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import pl.edu.agh.to.weatherapp.model.internal.Trip;

public class TripCell extends ListCell<Trip> {
    private static final String CITY_NAMES_SEPARATOR = " → ";
    HBox hbox = new HBox();
    Label label = new Label("");
    Pane pane = new Pane();
    Button button = new Button("Delete");

    public TripCell() {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(event ->
                pressDeleteButtonHandler()
        );
    }

    protected void pressDeleteButtonHandler() {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void updateItem(Trip item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            label.setText(item != null ? tripToString(item) : "<null>");
            setGraphic(hbox);
        }
    }

    private String tripToString(Trip trip) {
        return String.join(CITY_NAMES_SEPARATOR, trip.locationNames());
    }
}