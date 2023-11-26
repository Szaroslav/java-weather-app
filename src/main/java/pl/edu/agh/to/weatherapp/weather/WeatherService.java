package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.IWeatherFetcher;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.parser.IResponseParser;

@Service
public class WeatherService implements IWeatherService {

    private final IWeatherFetcher weatherFetcher;
    private final IResponseParser responseParser;

    @Autowired
    public WeatherService(IWeatherFetcher weatherFetcher, IResponseParser responseParser) {
        this.weatherFetcher = weatherFetcher;
        this.responseParser = responseParser;
    }

    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        WeatherData weatherData = new WeatherData();

        weatherFetcher.fetchCurrent(location)
                .thenAccept(System.out::println)
                .join();

        // TODO: now parse weatherFetcher output
        // responseParser.parseOrSomething();

        weatherData.setTemp(42);
        weatherData.setLocationName("Tarnów");
        return weatherData;
    }
}
