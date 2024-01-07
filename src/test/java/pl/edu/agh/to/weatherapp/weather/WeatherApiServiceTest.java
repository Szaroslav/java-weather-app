package pl.edu.agh.to.weatherapp.weather;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.edu.agh.to.weatherapp.service.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.model.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.dto.WeatherApiDto;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.service.weather.WeatherApiService;
import pl.edu.agh.to.weatherapp.service.weather.summary.ExtremeWeatherService;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;

class WeatherApiServiceTest {
    private static final String LOCATION_1 = "Tarnów";
    private static final String LOCATION_2 = "Cracow";
    private static final String RESPONSE_1 = "response";
    private static final String RESPONSE_2 = "response2";
    private static final int DAYS = 1;
    private static final int START_TIME = 8;
    private static final int END_TIME = 20;
    @Captor
    private final ArgumentCaptor<List<ForecastWeatherApiDto>> captor = ArgumentCaptor.captor();
    @Mock
    private final WeatherApiFetcher weatherApiFetcher = Mockito.mock(WeatherApiFetcher.class);
    @Mock
    private final JsonParser jsonParser = Mockito.mock(JsonParser.class);
    @Mock
    private final ExtremeWeatherService extremeWeatherService = Mockito.mock(ExtremeWeatherService.class);
    @Mock
    private final WeatherApiService weatherApiService = new WeatherApiService(
            weatherApiFetcher,
            jsonParser,
            extremeWeatherService
    );

    @Test
    void testProperGetWeatherData() {
        //given
        Weather expectedWeather = new Weather();
        expectedWeather.getLocationNames().add(LOCATION_1);
        ForecastWeatherApiDto forecastWeatherDto = prepareMockForecast(LOCATION_1);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherDto);
        Mockito.when(extremeWeatherService.getSummary(Collections.singletonList(forecastWeatherDto)))
                .thenReturn(expectedWeather);
        Weather result = weatherApiService.getWeatherData(LOCATION_1);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(24);
    }

    @Test
    void testProperGetWeatherData_withTime() {
        //given
        Weather expectedWeather = new Weather();
        expectedWeather.getLocationNames().add(LOCATION_1);
        ForecastWeatherApiDto forecastWeatherDto = prepareMockForecast(LOCATION_1);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherDto);
        Mockito.when(extremeWeatherService.getSummary(Collections.singletonList(forecastWeatherDto)))
                .thenReturn(expectedWeather);
        Weather result = weatherApiService.getWeatherData(LOCATION_1, START_TIME, END_TIME);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
    }

    @Test
    void testProperGetSummaryWeatherData() {
        //given
        Weather expectedWeather = new Weather();
        expectedWeather.getLocationNames().add(LOCATION_1);
        expectedWeather.getLocationNames().add(LOCATION_2);
        ForecastWeatherApiDto forecastWeatherDto1 = prepareMockForecast(LOCATION_1);
        ForecastWeatherApiDto forecastWeatherDto2 = prepareMockForecast(LOCATION_2);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherDto1);
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_2, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_2));
        Mockito.when(jsonParser.parseForecast(RESPONSE_2))
                .thenReturn(forecastWeatherDto2);
        Mockito.when(extremeWeatherService.getSummary(Mockito.anyList()))
                .thenReturn(expectedWeather);
        Weather result = weatherApiService.getSummaryWeatherData(LOCATION_1, LOCATION_2);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue())
                .containsExactly(forecastWeatherDto1, forecastWeatherDto2);
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(24);
        assertThat(captor.getValue().get(1).getHourlyWeatherForecasts())
                .hasSize(24);
    }

    @Test
    void testProperGetSummaryWeatherData_withTime() {
        //given
        Weather expectedWeather = new Weather();
        expectedWeather.getLocationNames().add(LOCATION_1);
        expectedWeather.getLocationNames().add(LOCATION_2);
        ForecastWeatherApiDto forecastWeatherDto1 = prepareMockForecast(LOCATION_1);
        ForecastWeatherApiDto forecastWeatherDto2 = prepareMockForecast(LOCATION_2);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherDto1);
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_2, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_2));
        Mockito.when(jsonParser.parseForecast(RESPONSE_2))
                .thenReturn(forecastWeatherDto2);
        Mockito.when(extremeWeatherService.getSummary(Mockito.anyList()))
                .thenReturn(expectedWeather);
        Weather result =
                weatherApiService.getSummaryWeatherData(LOCATION_1, LOCATION_2, START_TIME, END_TIME);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue())
                .containsExactly(forecastWeatherDto1, forecastWeatherDto2);
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
        assertThat(captor.getValue().get(1).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
    }

    private ForecastWeatherApiDto prepareMockForecast(String location) {
        ForecastWeatherApiDto forecastWeatherDto = new ForecastWeatherApiDto()
                .setLocationName(location);
        for (int i = 0; i < 24; i++) {
            var weatherData = Mockito.mock(WeatherApiDto.class);
            Mockito.when(weatherData.getDate()).thenReturn(new DateTime().plusHours(i));
            forecastWeatherDto.getHourlyWeatherForecasts().add(weatherData);
        }
        return forecastWeatherDto;
    }
}
