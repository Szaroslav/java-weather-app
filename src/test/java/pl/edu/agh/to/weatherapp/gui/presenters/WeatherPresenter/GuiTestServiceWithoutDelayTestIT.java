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
import pl.edu.agh.to.weatherapp.model.internal.*;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;

import static org.testfx.assertions.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceWithoutDelayTestIT {
        private static final String LOCATION_START = "Tarnów";
        private static final String LOCATION_END = "Kraków";
        private static final String CITY_NAMES_SEPARATOR = " → ";
        private static final int WIND = 1;
        private static final int RAIN = 2;
        private static final int TEMP = 3;
        private static final String ICON_URL = "https://cdn.weatherapi.com/weather/64x64/night/116.png";

        @Start
        private void start(Stage stage) throws IOException {
                stage.setTitle("Potezna wichura");
                stage.setMinWidth(400);
                stage.setMinHeight(400);

                WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
                Mockito.when(weatherServiceMock.getWeatherData(LOCATION_START)).thenAnswer(
                        (Answer<InternalWeatherData>) invocation -> {
                                InternalWeatherData weatherData = new InternalWeatherData();
                                weatherData.getLocationNames().add(LOCATION_START);
                                weatherData.getLocationNames().add(LOCATION_END);
                                weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                                weatherData.setWindIntensity(WindIntensity.WINDY);
                                weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                                weatherData.setPrecipitationType(PrecipitationType.BOTH);
                                weatherData.setTemperature(TEMP);
                                weatherData.setWindInMps(WIND);
                                weatherData.setPrecipitationInMm(RAIN);
                                weatherData.setConditionIconUrl(ICON_URL);
                                return weatherData;
                        });

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
                loader.setControllerFactory(c -> new WeatherPresenter(weatherServiceMock));
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
