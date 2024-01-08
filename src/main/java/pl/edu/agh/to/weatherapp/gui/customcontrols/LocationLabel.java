package pl.edu.agh.to.weatherapp.gui.customcontrols;

import javafx.scene.control.Label;

import java.util.List;

public class LocationLabel extends Label {
    private List<String> locationNames;
    private static final String CITY_NAMES_SEPARATOR = " → ";

    public void setNames(List<String> locationNames){
        this.locationNames = locationNames;
        this.setText(String.join(CITY_NAMES_SEPARATOR, this.locationNames));
    }

    public List<String> getNames(){
        return locationNames;
    }
}
