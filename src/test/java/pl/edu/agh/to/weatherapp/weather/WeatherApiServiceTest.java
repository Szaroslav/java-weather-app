package pl.edu.agh.to.weatherapp.weather;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import pl.edu.agh.to.weatherapp.api.WeatherApiFetcher;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.parser.JsonParser;
import pl.edu.agh.to.weatherapp.weather.summary.ExtremeWeatherService;

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
    private final ArgumentCaptor<List<ForecastWeatherData>> captor = ArgumentCaptor.captor();
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
        InternalWeatherData expectedWeather = new InternalWeatherData();
        expectedWeather.getLocationNames().add(LOCATION_1);
        ForecastWeatherData forecastWeatherData = prepareMockForecast(LOCATION_1);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherData);
        Mockito.when(extremeWeatherService.getSummary(Collections.singletonList(forecastWeatherData)))
                .thenReturn(expectedWeather);
        InternalWeatherData result = weatherApiService.getWeatherData(LOCATION_1);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(24);
    }

    @Test
    void testProperGetWeatherData_withTime() {
        //given
        InternalWeatherData expectedWeather = new InternalWeatherData();
        expectedWeather.getLocationNames().add(LOCATION_1);
        ForecastWeatherData forecastWeatherData = prepareMockForecast(LOCATION_1);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherData);
        Mockito.when(extremeWeatherService.getSummary(Collections.singletonList(forecastWeatherData)))
                .thenReturn(expectedWeather);
        InternalWeatherData result = weatherApiService.getWeatherData(LOCATION_1, START_TIME, END_TIME);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
    }

    @Test
    void testProperGetSummaryWeatherData() {
        //given
        InternalWeatherData expectedWeather = new InternalWeatherData();
        expectedWeather.getLocationNames().add(LOCATION_1);
        expectedWeather.getLocationNames().add(LOCATION_2);
        ForecastWeatherData forecastWeatherData1 = prepareMockForecast(LOCATION_1);
        ForecastWeatherData forecastWeatherData2 = prepareMockForecast(LOCATION_2);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherData1);
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_2, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_2));
        Mockito.when(jsonParser.parseForecast(RESPONSE_2))
                .thenReturn(forecastWeatherData2);
        Mockito.when(extremeWeatherService.getSummary(Mockito.anyList()))
                .thenReturn(expectedWeather);
        InternalWeatherData result = weatherApiService.getSummaryWeatherData(LOCATION_1, LOCATION_2);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue())
                .containsExactly(forecastWeatherData1, forecastWeatherData2);
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(24);
        assertThat(captor.getValue().get(1).getHourlyWeatherForecasts())
                .hasSize(24);
    }

    @Test
    void testProperGetSummaryWeatherData_withTime() {
        //given
        InternalWeatherData expectedWeather = new InternalWeatherData();
        expectedWeather.getLocationNames().add(LOCATION_1);
        expectedWeather.getLocationNames().add(LOCATION_2);
        ForecastWeatherData forecastWeatherData1 = prepareMockForecast(LOCATION_1);
        ForecastWeatherData forecastWeatherData2 = prepareMockForecast(LOCATION_2);

        //when
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_1, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_1));
        Mockito.when(jsonParser.parseForecast(RESPONSE_1))
                .thenReturn(forecastWeatherData1);
        Mockito.when(weatherApiFetcher.fetchForecast(LOCATION_2, DAYS))
                .thenReturn(CompletableFuture.completedFuture(RESPONSE_2));
        Mockito.when(jsonParser.parseForecast(RESPONSE_2))
                .thenReturn(forecastWeatherData2);
        Mockito.when(extremeWeatherService.getSummary(Mockito.anyList()))
                .thenReturn(expectedWeather);
        InternalWeatherData result =
                weatherApiService.getSummaryWeatherData(LOCATION_1, LOCATION_2, START_TIME, END_TIME);

        //then
        assertThat(result).isSameAs(expectedWeather);
        Mockito.verify(extremeWeatherService).getSummary(captor.capture());
        assertThat(captor.getValue())
                .containsExactly(forecastWeatherData1, forecastWeatherData2);
        assertThat(captor.getValue().get(0).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
        assertThat(captor.getValue().get(1).getHourlyWeatherForecasts())
                .hasSize(END_TIME - START_TIME + 1);
    }

    private ForecastWeatherData prepareMockForecast(String location) {
        ForecastWeatherData forecastWeatherData = new ForecastWeatherData()
                .setLocationName(location);
        for (int i = 0; i < 24; i++) {
            var weatherData = Mockito.mock(WeatherData.class);
            Mockito.when(weatherData.getDate()).thenReturn(new DateTime().plusHours(i));
            forecastWeatherData.getHourlyWeatherForecasts().add(weatherData);
        }
        return forecastWeatherData;
    }
}
