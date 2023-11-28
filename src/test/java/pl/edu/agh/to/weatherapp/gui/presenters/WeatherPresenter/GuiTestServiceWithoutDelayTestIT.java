package pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;

import static org.testfx.assertions.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceWithoutDelayTestIT {
        private static final String LOCATION = "Tarnów";
        private static final int TEMPERATURE = 40;
        private static final String TEMPERATURE_SUFFIX = "°C";
        private static final String ICON_URL = "https://cdn.weatherapi.com/weather/64x64/night/116.png";

        @Start
        private void start(Stage stage) throws IOException {
                stage.setTitle("Potezna wichura");
                stage.setMinWidth(400);
                stage.setMinHeight(400);

                WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
                Mockito.when(weatherServiceMock.getWeatherData(LOCATION)).thenAnswer(
                        (Answer<WeatherData>) invocation -> {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setLocationName(LOCATION);
                                weatherData.setTemp(TEMPERATURE);
                                weatherData.setConditionIconUrl(ICON_URL);
                                return weatherData;
                        });

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
                loader.setControllerFactory(c -> new WeatherPresenter(weatherServiceMock));
                AnchorPane rootLayout = loader.load();

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
                robot.write(LOCATION);
                robot.type(KeyCode.ENTER);
                assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                        .hasText(LOCATION);
                assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                        .hasText(TEMPERATURE + TEMPERATURE_SUFFIX);
        }

        @Test
        void shouldDisplayWeatherOnClick(FxRobot robot) {
                robot.clickOn("#searchTextField");
                robot.write(LOCATION);
                robot.clickOn("#searchButton");
                assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                        .hasText(LOCATION);
                assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                        .hasText(TEMPERATURE + TEMPERATURE_SUFFIX);
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

        }
}
