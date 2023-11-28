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
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.concurrent.CompletionException;

import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceErrorTestIT {
        private static final String LOCATION_VALID = "Tarnów";
        private static final String LOCATION_INVALID = "Kraków";
        private static final int TEMPERATURE = 40;
        private static final String TEMPERATURE_SUFFIX = "°C";
        private static final String ICON_URL = "https://cdn.weatherapi.com/weather/64x64/night/116.png";

        @Start
        private void start(Stage stage) throws IOException {
                stage.setTitle("Potezna wichura");
                stage.setMinWidth(400);
                stage.setMinHeight(400);

                WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
                Mockito.when(weatherServiceMock.getWeatherData(LOCATION_INVALID)).thenAnswer(
                        (Answer<WeatherData>) invocation -> {
                                throw new CompletionException(new InvalidRequest("No matching location found."));
                        });
                Mockito.when(weatherServiceMock.getWeatherData(LOCATION_VALID)).thenAnswer(
                        (Answer<WeatherData>) invocation -> {
                                WeatherData weatherData = new WeatherData();
                                weatherData.setLocationName(LOCATION_VALID);
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
                Assertions.assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                        .hasText(LOCATION_VALID);
                Assertions.assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                        .hasText(TEMPERATURE + TEMPERATURE_SUFFIX);
        }

}
