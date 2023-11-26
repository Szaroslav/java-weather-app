package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.weatherapp.Config;
import pl.edu.agh.to.weatherapp.api.IWeatherFetcher;
import pl.edu.agh.to.weatherapp.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherServiceMock;

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
    public WeatherPresenter weatherPresenter() {
        return new WeatherPresenter(new WeatherServiceMock());
    }

    @Bean
    public IWeatherFetcher weatherFetcher() {
        return new WeatherApiFetcher(config.getWeatherApiKey());
    }
}
