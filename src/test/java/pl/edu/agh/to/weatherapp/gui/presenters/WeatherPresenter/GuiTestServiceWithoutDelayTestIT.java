package pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.presenters.FavouriteTrips;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.service.persistence.TripPersistenceService;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.io.IOException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceWithoutDelayTestIT {
    private static final String LOCATION_START = "Tarnów";
    private static final String LOCATION_END = "Kraków";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final int WIND = 1;
    private static final int RAIN = 2;
    private static final int TEMP = 3;
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;
    private static final String VALID_EARLY_TIME = "12";
    private static final String VALID_LATE_TIME = "15";

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        Mockito.when(weatherServiceMock.getSummaryWeatherData(LOCATION_START, LOCATION_END, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather();
                    weatherData.getLocationNames().add(LOCATION_START);
                    weatherData.getLocationNames().add(LOCATION_END);
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                    weatherData.setPrecipitationType(PrecipitationType.BOTH);
                    weatherData.setApparentTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });

        Mockito.when(weatherServiceMock.getSummaryWeatherData(LOCATION_START, LOCATION_END, Integer.parseInt(VALID_EARLY_TIME), Integer.parseInt(VALID_LATE_TIME))).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather();
                    weatherData.getLocationNames().add(LOCATION_START);
                    weatherData.getLocationNames().add(LOCATION_END);
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
                //TODO: create mock for FavouriteTrips
                new WeatherPresenter(weatherServiceMock, new FavouriteTrips(new TripPersistenceService())));
        GridPane rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void shouldContainButton(FxRobot robot) {
        assertThat(robot.lookup("#searchButton").queryAs(Button.class)).hasText("");
    }

    @Test
    void shouldDisplayWeatherOnEnter(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchDestinationTextField");
        robot.write(LOCATION_END);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void shouldDisplayWeatherOnClick(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchDestinationTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#searchButton");

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void shouldDisplayWeatherOnEnterEndTimeFocused(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchDestinationTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#timeStartTextField");
        robot.write(VALID_EARLY_TIME);
        robot.clickOn("#timeEndTextField");
        robot.write(VALID_LATE_TIME);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void shouldDisplayWeatherOnEnterStartTimeFocused(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchDestinationTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#timeEndTextField");
        robot.write(VALID_LATE_TIME);
        robot.clickOn("#timeStartTextField");
        robot.write(VALID_EARLY_TIME);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void noWeatherInformationOnEmptyPrompt(FxRobot robot) {
        robot.clickOn("#searchButton");

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class))
                .hasText("Search field cannot be empty");
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isFalse();
    }
}
