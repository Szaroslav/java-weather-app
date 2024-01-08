package pl.edu.agh.to.weatherapp.gui.presenters.weatherpresenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import pl.edu.agh.to.weatherapp.gui.presenters.FavouriteTrips;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.io.IOException;
import java.util.List;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceWithoutDelayTestIT {
    private static final String LOCATION_START = "Tarnów";
    private static final String LOCATION_END = "Kraków";
    private static final String LOCATION_MIDDLE = "Ryglice";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final int WIND = 1;
    private static final int RAIN = 2;
    private static final int TEMP = 3;
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;
    private static final String VALID_EARLY_TIME = "12";
    private static final String VALID_LATE_TIME = "15";

    private static final String FIRST_TEXT_FIELD_ID = "#searchStartTextField";
    private static final String SECOND_TEXT_FIELD_ID = "#searchMiddleTextField";
    private static final String THIRD_TEXT_FIELD_ID = "#searchDestinationTextField";

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);
        ObservableList<Trip> trips = FXCollections.observableArrayList();

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        FavouriteTrips favouriteTripsMock = Mockito.mock(FavouriteTrips.class);
        Mockito.when(favouriteTripsMock.getTrips()).thenAnswer((Answer<ObservableList<Trip>>) invocation -> trips);

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather()
                    .setTemperatureLevel(TemperatureLevel.COLD)
                    .setWindIntensity(WindIntensity.WINDY)
                    .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                    .setPrecipitationType(PrecipitationType.BOTH)
                    .setApparentTemperature(TEMP)
                    .setWindInMps(WIND)
                    .setPrecipitationInMm(RAIN);
                    weatherData.getLocationNames().add(LOCATION_START);
                    return weatherData;
                });

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_MIDDLE), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather()
                    .setTemperatureLevel(TemperatureLevel.COLD)
                    .setWindIntensity(WindIntensity.WINDY)
                    .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                    .setPrecipitationType(PrecipitationType.BOTH)
                    .setApparentTemperature(TEMP)
                    .setWindInMps(WIND)
                    .setPrecipitationInMm(RAIN);
                    weatherData.getLocationNames().addAll(List.of(LOCATION_START, LOCATION_MIDDLE));
                    return weatherData;
                });

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_END), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather()
                    .setTemperatureLevel(TemperatureLevel.COLD)
                    .setWindIntensity(WindIntensity.WINDY)
                    .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                    .setPrecipitationType(PrecipitationType.BOTH)
                    .setApparentTemperature(TEMP)
                    .setWindInMps(WIND)
                    .setPrecipitationInMm(RAIN);
                    weatherData.getLocationNames().addAll(List.of(LOCATION_START, LOCATION_END));
                    return weatherData;
                });

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_MIDDLE, LOCATION_END), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather()
                    .setTemperatureLevel(TemperatureLevel.COLD)
                    .setWindIntensity(WindIntensity.WINDY)
                    .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                    .setPrecipitationType(PrecipitationType.BOTH)
                    .setApparentTemperature(TEMP)
                    .setWindInMps(WIND)
                    .setPrecipitationInMm(RAIN);
                    weatherData.getLocationNames().addAll(List.of(LOCATION_START, LOCATION_MIDDLE, LOCATION_END));
                    return weatherData;
                });

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_END), Integer.parseInt(VALID_EARLY_TIME), Integer.parseInt(VALID_LATE_TIME))).thenAnswer(
                (Answer<Weather>) invocation -> {
                    Weather weatherData = new Weather()
                    .setTemperatureLevel(TemperatureLevel.COLD)
                    .setWindIntensity(WindIntensity.WINDY)
                    .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                    .setPrecipitationType(PrecipitationType.BOTH)
                    .setApparentTemperature(TEMP)
                    .setWindInMps(WIND)
                    .setPrecipitationInMm(RAIN);
                    weatherData.getLocationNames().addAll(List.of(LOCATION_START, LOCATION_END));
                    return weatherData;
                });

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        loader.setControllerFactory(c ->
                new WeatherPresenter(weatherServiceMock, favouriteTripsMock));
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
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();

        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.write(LOCATION_MIDDLE);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_MIDDLE);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();

        robot.clickOn(THIRD_TEXT_FIELD_ID);
        robot.write(LOCATION_END);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_MIDDLE + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();

        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.type(KeyCode.BACK_SPACE, LOCATION_MIDDLE.length());
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void shouldDisplayWeatherOnClick(FxRobot robot) {
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
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
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
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
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
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

    @Test
    void noWeatherInformationOnFirstTextFieldEmpty(FxRobot robot) {
        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.write(LOCATION_MIDDLE);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class))
                .hasText("Search field cannot be empty");
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isFalse();

        robot.clickOn("#searchDestinationTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class))
                .hasText("Search field cannot be empty");
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isFalse();

        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.type(KeyCode.BACK_SPACE, LOCATION_MIDDLE.length());
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
