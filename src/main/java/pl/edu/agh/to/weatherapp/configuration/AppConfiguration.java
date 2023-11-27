package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.weatherapp.api.IWeatherFetcher;
import pl.edu.agh.to.weatherapp.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.parser.IParser;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.IWeatherService;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;

@Configuration
public class AppConfiguration {
    private final Config config;

    public AppConfiguration() throws IOException {
        config = new Config();
        config.init();
    }

    @Bean
    public String apiKey() {
        return config.getWeatherApiKey();
    }

    @Bean
    public WeatherPresenter weatherPresenter(IWeatherService weatherService) {
        return new WeatherPresenter(weatherService);
    }

    @Bean
    public IWeatherService weatherService(IWeatherFetcher weatherFetcher, IParser responseParser) {
        return new WeatherService(weatherFetcher, responseParser);
    }

    @Bean
    public IParser weatherParser() {
        return new JsonParser();
    }

    @Bean
    public IWeatherFetcher weatherFetcher() {
        return new WeatherApiFetcher(config.getWeatherApiKey());
    }
}
