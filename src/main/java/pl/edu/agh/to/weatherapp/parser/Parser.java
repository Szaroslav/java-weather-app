package pl.edu.agh.to.weatherapp.parser;

import pl.edu.agh.to.weatherapp.model.WeatherData;

public interface Parser {
    WeatherData parse(String content);
}
