package pl.edu.agh.to.weatherapp.parser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.WeatherData;

import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class JsonParserTest {
    private static final String VALID_JSON_FILENAME = "valid_response.json";
    private static final String ERROR_JSON_FILENAME = "error_response.json";
    private static String validJsonContent;
    private static String errorJsonContent;
    private final JsonParser jsonParser = new JsonParser();

    @SneakyThrows
    @BeforeAll
    public static void init() {
        validJsonContent = new String(Files.readAllBytes(Paths.get(JsonParserTest.class.getClassLoader().getResource(VALID_JSON_FILENAME).toURI())));
        errorJsonContent = new String(Files.readAllBytes(Paths.get(JsonParserTest.class.getClassLoader().getResource(ERROR_JSON_FILENAME).toURI())));
    }

    @Test
    void parseValidResponse() {
        WeatherData weatherData = jsonParser.parse(validJsonContent);
        assertThat(weatherData.getTemp()).isEqualTo(-1);
        assertThat(weatherData.getLocationName()).isEqualTo("Tarnów, Pologne");
        assertThat(weatherData.getConditionIconUrl()).isEqualTo("https://cdn.weatherapi.com/weather/64x64/night/122.png");
    }

    @Test
    void parseErrorResponse() {
        assertThrows(InvalidRequest.class, () -> jsonParser.parse(errorJsonContent));
    }
}
