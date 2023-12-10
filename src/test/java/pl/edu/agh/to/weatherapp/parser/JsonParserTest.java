package pl.edu.agh.to.weatherapp.parser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;

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
        ForecastWeatherData forecast = jsonParser.parseForecast(validJsonContent);
        assertThat(forecast.getLocationName()).isEqualTo("Cracow, Poland");
        assertThat(forecast.getHourlyWeatherForecasts().size()).isEqualTo(24);
        // TODO: Verify a first forecast.
    }

    @Test
    void parseErrorResponse() {
        assertThrows(InvalidRequest.class, () -> jsonParser.parseForecast(errorJsonContent));
    }
}
