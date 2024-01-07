package pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.Weather;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.presenters.TripMemory;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.service.TripPersistenceService;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;
import java.util.concurrent.CompletionException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceErrorTestIT {
    private static final String LOCATION_VALID = "Tarnów";
    private static final String LOCATION_INVALID = "Kraków";
    private static final int WIND = 1;
    private static final int RAIN = 2;
    private static final int TEMP = 3;
    private static final String INVALID_TIME = "intm";
    private static final String VALID_EARLY_TIME = "12";
    private static final String VALID_LATE_TIME = "15";
    private static final String TIME_INVALID_MESSAGE = "Invalid time range";
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        Mockito.when(weatherServiceMock.getWeatherData(LOCATION_INVALID, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    throw new CompletionException(new InvalidRequest("No matching location found."));
                });
        Mockito.when(weatherServiceMock.getWeatherData(LOCATION_VALID, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather();
                    weatherData.getLocationNames().add(LOCATION_VALID);
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                    weatherData.setPrecipitationType(PrecipitationType.BOTH);
                    weatherData.setApparentTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(LOCATION_VALID, Integer.parseInt(VALID_EARLY_TIME), Integer.parseInt(VALID_LATE_TIME))).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather();
                    weatherData.getLocationNames().add(LOCATION_VALID);
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                    weatherData.setPrecipitationType(PrecipitationType.BOTH);
                    weatherData.setApparentTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        loader.setControllerFactory(c ->
                //TODO: create mock for TripMemory
                new WeatherPresenter(weatherServiceMock, new TripMemory(new TripPersistenceService())));
        GridPane rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void handlingErrorsFromService(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_INVALID);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText("No matching location found.");

        robot.clickOn("#searchTextField");
        robot.type(KeyCode.BACK_SPACE, LOCATION_INVALID.length());
        robot.write(LOCATION_VALID);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText("");
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_VALID);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
    }

    @Test
    void handlingTimeErrors(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_VALID);
        robot.clickOn("#timeStartTextField");
        robot.write(INVALID_TIME);
        robot.clickOn("#timeEndTextField");
        robot.write(INVALID_TIME);
        robot.clickOn("#searchButton");
        robot.clickOn("#timeEndTextField");
        robot.type(KeyCode.BACK_SPACE, INVALID_TIME.length());
        robot.clickOn("#timeStartTextField");
        robot.type(KeyCode.BACK_SPACE, INVALID_TIME.length());
        robot.write(VALID_EARLY_TIME);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText(TIME_INVALID_MESSAGE);

        robot.clickOn("#timeStartTextField");
        robot.type(KeyCode.BACK_SPACE, VALID_EARLY_TIME.length());
        robot.clickOn("#timeEndTextField");
        robot.write(VALID_EARLY_TIME);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText(TIME_INVALID_MESSAGE);

        robot.clickOn("#timeStartTextField");
        robot.write(VALID_LATE_TIME);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText(TIME_INVALID_MESSAGE);

        robot.clickOn("#timeStartTextField");
        robot.type(KeyCode.BACK_SPACE, VALID_LATE_TIME.length());
        robot.write(VALID_EARLY_TIME);
        robot.clickOn("#timeEndTextField");
        robot.type(KeyCode.BACK_SPACE, VALID_EARLY_TIME.length());
        robot.write(VALID_LATE_TIME);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText("");
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_VALID);
    }

}
