package pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;
import java.time.Duration;

import static org.awaitility.Awaitility.await;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceDelayTestIT {
        private static final String LOCATION = "Tarnów";
        private static final int TEMPERATURE = 40;
        private static final String TEMPERATURE_SUFFIX = "°C";
        private static final String BUTTON_TEXT_AFTER_CLICK = "clicked!";
        private static final String ICON_URL = "https://cdn.weatherapi.com/weather/64x64/night/116.png";

        @Start
        private void start(Stage stage) throws IOException {
                stage.setTitle("Potezna wichura");
                stage.setMinWidth(400);
                stage.setMinHeight(400);

                WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
                Mockito.when(weatherServiceMock.getWeatherData(LOCATION)).thenAnswer(
                    (Answer<InternalWeatherData>) invocation -> {
                            await()
                                .pollDelay(Duration.ofSeconds(2))
                                .until(()->true);
                            InternalWeatherData weatherData = new InternalWeatherData();
                            weatherData.getLocationNames().add(LOCATION);
                            return weatherData;
                    });

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
                loader.setControllerFactory(c -> new WeatherPresenter(weatherServiceMock));
                AnchorPane rootLayout = loader.load();

                Button button = new Button("click me!");
                button.setId("myButton");
                button.setOnAction(actionEvent -> button.setText(BUTTON_TEXT_AFTER_CLICK));
                rootLayout .getChildren().add(button);

                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
        }

        @Test
        void serviceNonBlocking(FxRobot robot) {
                robot.clickOn("#searchTextField");
                robot.write(LOCATION);
                robot.clickOn("#searchButton");
                Assertions.assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                    .hasText("");
                Assertions.assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                    .hasText("");
                robot.clickOn("#myButton");
                Assertions.assertThat(robot.lookup("#myButton").queryAs(Button.class))
                    .hasText(BUTTON_TEXT_AFTER_CLICK);
                await()
                    .pollDelay(Duration.ofSeconds(3))
                    .until(()->true);
                Assertions.assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                    .hasText(LOCATION);
                Assertions.assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                    .hasText(TEMPERATURE + TEMPERATURE_SUFFIX);
        }

        @Test
        void whenSearchButtonClick_thenButtonDisabled(FxRobot robot) {
                robot.write(LOCATION);
                robot.clickOn("#searchButton");

                Assertions.assertThat(robot.lookup("#searchButton").queryAs(Button.class).isDisabled()).isTrue();
                await()
                    .pollDelay(Duration.ofSeconds(3))
                    .until(()->true);
                Assertions.assertThat(robot.lookup("#searchButton").queryAs(Button.class).isDisabled()).isFalse();
        }
}
