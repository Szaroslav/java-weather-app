package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.edu.agh.to.weatherapp.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;
import pl.edu.agh.to.weatherapp.weather.WeatherApiService;

import java.net.http.HttpClient;

@Configuration
@PropertySource("classpath:properties/app.properties")
public class AppConfiguration {
    @Value("${weather.apiKey}")
    private String apiKey;

    @Bean
    public WeatherPresenter weatherPresenter(WeatherService weatherService) {
        return new WeatherPresenter(weatherService);
    }

    @Bean
    public WeatherService weatherService(WeatherFetcher weatherFetcher, JsonParser responseParser) {
        return new WeatherApiService(weatherFetcher, responseParser);
    }

    @Bean
    public JsonParser weatherParser() {
        return new JsonParser();
    }

    @Bean
    public WeatherFetcher weatherFetcher() {
        return new WeatherApiFetcher(apiKey, httpClient());
    }

    @Bean
    public HttpClient httpClient() {
        return HttpClient.newHttpClient();
    }
}
