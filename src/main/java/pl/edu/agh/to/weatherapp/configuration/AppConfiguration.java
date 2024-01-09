package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.edu.agh.to.weatherapp.service.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.service.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.service.parser.JsonParser;
import pl.edu.agh.to.weatherapp.gui.presenters.FavouriteTrips;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.service.persistence.SqlitePersistenceService;
import pl.edu.agh.to.weatherapp.service.persistence.TripPersistenceService;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;
import pl.edu.agh.to.weatherapp.service.weather.WeatherApiService;
import pl.edu.agh.to.weatherapp.service.weather.summary.ExtremeWeatherService;
import pl.edu.agh.to.weatherapp.service.weather.summary.WeatherSummaryService;

import java.net.http.HttpClient;

@Configuration
@PropertySource("classpath:properties/app.properties")
public class AppConfiguration {
    @Value("${weather.apiKey}")
    private String apiKey;

    @Bean(name = "apiKey")
    public String apiKey() {
        return apiKey;
    }

    @Bean
    public WeatherPresenter weatherPresenter(WeatherService weatherService, FavouriteTrips tripMemory) {
        return new WeatherPresenter(weatherService, tripMemory);
    }

    @Bean
    public WeatherService weatherService(WeatherFetcher weatherFetcher,
                                         JsonParser responseParser,
                                         WeatherSummaryService weatherSummaryService) {
        return new WeatherApiService(weatherFetcher, responseParser, weatherSummaryService);
    }

    @Bean
    public WeatherSummaryService weatherSummaryService() {
        return new ExtremeWeatherService();
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

    @Bean
    public TripPersistenceService tripPersistenceService() {
        return new SqlitePersistenceService("trips.db");
    }

    @Bean
    public FavouriteTrips tripMemory() {
        return new FavouriteTrips(tripPersistenceService());
    }
}
