package pl.edu.agh.to.weatherapp.weather.summary;

import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;

import java.util.List;

public class ExtremeWeatherService implements WeatherSummaryService{

    //TODO: implement
    @Override
    public InternalWeatherData getSummary(List<WeatherData> weatherDataList) {
        return new InternalWeatherData();
    }
}
