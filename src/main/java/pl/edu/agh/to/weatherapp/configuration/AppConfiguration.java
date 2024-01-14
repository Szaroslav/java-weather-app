package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import pl.edu.agh.to.weatherapp.gui.presenters.FavouriteTrips;
import pl.edu.agh.to.weatherapp.gui.presenters.FavouritesPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.SearchPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherInfoPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.service.api.WeatherFetcher;
import pl.edu.agh.to.weatherapp.service.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.service.parser.JsonParser;
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
    @Value("${weather.dbName}")
    private String dbName;
    @Value("${weather.noDaysToCheckPrecipitation}")
    private int noDaysToCheckPrecipitation;

    @Bean(name = "apiKey")
    public String apiKey() {
        return apiKey;
    }

    @Bean(name = "dbName")
    public String dbName() {
        return dbName;
    }

    @Bean(name = "noDaysToCheckPrecipitation")
    public int noDaysToCheckPrecipitation() {
        return noDaysToCheckPrecipitation;
    }

    @Bean
    public WeatherPresenter weatherPresenter() {
        return new WeatherPresenter();
    }

    @Bean
    public FavouritesPresenter favouritesPresenter(FavouriteTrips tripMemory) {
        return new FavouritesPresenter(tripMemory);
    }

    @Bean
    public SearchPresenter searchPresenterPresenter(WeatherService weatherService) {
        return new SearchPresenter(weatherService);
    }

    @Bean
    public WeatherInfoPresenter weatherInfoPresenter() {
        return new WeatherInfoPresenter();
    }

    @Bean
    public WeatherService weatherService(WeatherFetcher weatherFetcher,
                                         JsonParser responseParser,
                                         WeatherSummaryService weatherSummaryService) {
        return new WeatherApiService(
                noDaysToCheckPrecipitation(),
                weatherFetcher,
                responseParser,
                weatherSummaryService
        );
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
        return new SqlitePersistenceService(dbName());
    }

    @Bean
    public FavouriteTrips tripMemory() {
        return new FavouriteTrips(tripPersistenceService());
    }
}
