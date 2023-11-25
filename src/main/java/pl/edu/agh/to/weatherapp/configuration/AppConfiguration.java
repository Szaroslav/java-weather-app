package pl.edu.agh.to.weatherapp.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherServiceMock;

@Configuration
public class AppConfiguration {
    @Bean
    public WeatherPresenter weatherPresenter() {
        return new WeatherPresenter(new WeatherServiceMock());
    }
}
