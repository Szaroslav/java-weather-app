package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.model.WeatherData;

@Service
public class WeatherServiceMock implements WeatherService {
    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        Thread.sleep(2000);
        WeatherData weatherData = new WeatherData();
        weatherData.setTemp(42);
        weatherData.setLocationName("Tarnów");
        return weatherData;
    }
}
