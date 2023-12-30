package pl.edu.agh.to.weatherapp.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Accessors(chain = true)
public class Trip {
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private List<String> locationNames = new ArrayList<>();

    @Override
    public String toString() {
        StringBuilder nameString = new StringBuilder();
        for (int i = 0; i < locationNames.size(); i++) {
            if (i != 0) {
                nameString.append(CITY_NAMES_SEPARATOR);
            }
            nameString.append(locationNames.get(i));
        }
        return nameString.toString();
    }
}