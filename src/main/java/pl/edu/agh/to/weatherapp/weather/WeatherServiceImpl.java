package pl.edu.agh.to.weatherapp.weather;

import pl.edu.agh.to.weatherapp.WeatherData;

public class WeatherServiceImpl implements WeatherService {
    @Override
    public WeatherData getWeatherData(String loc) {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemp(42.42);
        weatherData.setCityName("Tarnów");
        return new WeatherData();
    }
}
