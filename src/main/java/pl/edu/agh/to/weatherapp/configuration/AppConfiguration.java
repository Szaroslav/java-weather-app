package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.parser.Parser;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;
import pl.edu.agh.to.weatherapp.weather.WeatherApiService;

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
    public WeatherPresenter weatherPresenter(WeatherService weatherService) {
        return new WeatherPresenter(weatherService);
    }

    @Bean
    public WeatherService weatherService(WeatherFetcher weatherFetcher, Parser responseParser) {
        return new WeatherApiService(weatherFetcher, responseParser);
    }

    @Bean
    public Parser weatherParser() {
        return new JsonParser();
    }

    @Bean
    public WeatherFetcher weatherFetcher() {
        return new WeatherApiFetcher(config.getWeatherApiKey());
    }
}
