package pl.edu.agh.to.weatherapp.weather;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.edu.agh.to.weatherapp.api.IWeatherFetcher;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.parser.IParser;

@Service
public class WeatherService implements IWeatherService {

    private final IWeatherFetcher weatherFetcher;
    private final IParser responseParser;

    @Autowired
    public WeatherService(IWeatherFetcher weatherFetcher, IParser responseParser) {
        this.weatherFetcher = weatherFetcher;
        this.responseParser = responseParser;
    }

    @Override
    @SneakyThrows
    public WeatherData getWeatherData(String location) {
        return weatherFetcher.fetchCurrent(location)
                .thenApply(responseParser::parse)
                .join();
    }
}
