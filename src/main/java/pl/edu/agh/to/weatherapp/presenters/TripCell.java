package pl.edu.agh.to.weatherapp.presenters;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import pl.edu.agh.to.weatherapp.model.Trip;
import pl.edu.agh.to.weatherapp.model.TripMemory;

public class TripCell extends ListCell<Trip> {
    HBox hbox = new HBox();
    Label label = new Label("(empty)");
    Pane pane = new Pane();
    Button button = new Button("Delete");
    Trip lastItem;

    public TripCell(TripMemory trips) {
        super();
        hbox.getChildren().addAll(label, pane, button);
        HBox.setHgrow(pane, Priority.ALWAYS);
        button.setOnAction(event ->{
            if (lastItem != null) {
                trips.deleteTrip(lastItem);
            }
        });
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
            label.setText(item != null ? item.toString() : "<null>");
            setGraphic(hbox);
        }
    }
}