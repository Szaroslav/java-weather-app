package pl.edu.agh.to.weatherapp.service.parser;

import lombok.SneakyThrows;
import org.assertj.core.data.Percentage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.dto.DailyWeatherApiDto;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonParserTest {
    private static final String VALID_JSON_FILENAME = "weather_forecast_krakow.json";
    private static final String ERROR_JSON_FILENAME = "error_response.json";
    private static String validJsonContent;
    private static String errorJsonContent;
    private final JsonParser jsonParser = new JsonParser();

    @SneakyThrows
    @BeforeAll
    public static void init() {
        final ClassLoader classLoader = JsonParserTest.class.getClassLoader();
        validJsonContent = new String(
            Files.readAllBytes(
                Paths.get(Objects.requireNonNull(
                    classLoader.getResource(VALID_JSON_FILENAME)).toURI()
                )
            )
        );
        errorJsonContent = new String(
            Files.readAllBytes(
                Paths.get(Objects.requireNonNull(
                    classLoader.getResource(ERROR_JSON_FILENAME)).toURI()
                )
            )
        );
    }

    @Test
    void parseValidResponse() {
        DailyWeatherApiDto forecast = jsonParser.parseForecast(validJsonContent);
        assertThat(forecast.getLocationName()).isEqualTo("Cracow, Poland");
        assertThat(forecast.getHourlyWeatherForecasts()).hasSize(24);
        var weatherData = forecast.getHourlyWeatherForecasts().get(8);
        assertThat(weatherData.getDate().getHourOfDay()).isEqualTo(8);
        assertThat(weatherData.getWindKph()).isCloseTo(5.8F, Percentage.withPercentage(1));
        assertThat(weatherData.getPrecipitationMm()).isCloseTo(0.24F, Percentage.withPercentage(1));
        assertThat(weatherData.getTemperatureC()).isEqualTo(-5);
        assertThat(weatherData.getConditionIconUrl()).isEqualTo("//cdn.weatherapi.com/weather/64x64/day/329.png");
        assertThat(weatherData.isWillRain()).isFalse();
        assertThat(weatherData.isWillSnow()).isTrue();
    }

    @Test
    void parseErrorResponse() {
        assertThrows(InvalidRequest.class, () -> jsonParser.parseForecast(errorJsonContent));
    }
}
