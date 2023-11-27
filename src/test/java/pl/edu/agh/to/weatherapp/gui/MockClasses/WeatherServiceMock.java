package pl.edu.agh.to.weatherapp.gui.MockClasses;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

@Service
public class WeatherServiceMock implements WeatherService {
    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        WeatherData weatherData = new WeatherData();
        weatherData.setTemp(42);
        weatherData.setLocationName("Tarnów");
        weatherData.setConditionIconUrl("https://www.save9.com/wp-content/uploads/2015/01/Cloud-Computing-from-Save9.png");
        return weatherData;
    }
}
