package pl.edu.agh.to.weatherapp.presenters;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import pl.edu.agh.to.weatherapp.model.Trip;

import java.util.List;

public class TripCell extends ListCell<Trip> {
    private static final String CITY_NAMES_SEPARATOR = " → ";
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("Delete");
    Trip lastItem;

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
        setText(null);
        if (empty) {
            lastItem = null;
            setGraphic(null);
        } else {
            lastItem = item;
            label.setText(item != null ? tripToString(item) : "<null>");
            setGraphic(hbox);
        }
    }

    private String tripToString(Trip trip) {
        StringBuilder nameString = new StringBuilder();
        List<String> locationNames = trip.getLocationNames();
        for (int i = 0; i < locationNames.size(); i++) {
            if (i != 0) {
                nameString.append(CITY_NAMES_SEPARATOR);
            }
            nameString.append(locationNames.get(i));
        }
        return nameString.toString();
    }
}