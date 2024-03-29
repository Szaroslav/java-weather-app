package pl.edu.agh.to.weatherapp.gui.presenters.weatherpresenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import pl.edu.agh.to.weatherapp.gui.presenters.FavouritesPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.SearchPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherInfoPresenter;
import pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.io.IOException;
import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceDelayTestIT {
    private static final String LOCATION_START = "Tarnów";
    private static final String LOCATION_END = "Kraków";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final int WIND = 1;
    private static final int RAIN = 2;
    private static final int TEMP = 3;
    private static final String BUTTON_TEXT_AFTER_CLICK = "clicked!";
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        FavouriteTrips favouriteTripsMock = Mockito.mock(FavouriteTrips.class);
        Mockito.when(favouriteTripsMock.getTrips()).thenAnswer((Answer<ObservableList<Trip>>) invocation -> trips);

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_END), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    await()
                            .pollDelay(Duration.ofSeconds(2))
                            .until(() -> true);
                    return new Weather()
                            .setTemperatureLevel(TemperatureLevel.COLD)
                            .setWindIntensity(WindIntensity.WINDY)
                            .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                            .setPrecipitationType(PrecipitationType.BOTH)
                            .setApparentTemperature(TEMP)
                            .setWindInMps(WIND)
                            .setPrecipitationInMm(RAIN)
                            .setLocationNames(List.of(LOCATION_START, LOCATION_END));
                });

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        loader.setControllerFactory(c -> {
            if (c == WeatherPresenter.class) {
                return new WeatherPresenter();
            }
            if (c == SearchPresenter.class) {
                return new SearchPresenter(weatherServiceMock);
            }
            if (c == FavouritesPresenter.class) {
                return new FavouritesPresenter(favouriteTripsMock);
            }
            return new WeatherInfoPresenter();
        });
        GridPane rootLayout = loader.load();

        Button button = new Button("click me!");
        button.setId("myButton");
        button.setOnAction(actionEvent -> button.setText(BUTTON_TEXT_AFTER_CLICK));
        rootLayout.getChildren().add(button);

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void serviceNonBlocking(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchMiddleTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText("");
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText("");

        robot.clickOn("#myButton");
        assertThat(robot.lookup("#myButton").queryAs(Button.class))
                .hasText(BUTTON_TEXT_AFTER_CLICK);
        await()
                .pollDelay(Duration.ofSeconds(3))
                .until(() -> true);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_END);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#weatherInfoVBox").queryAs(VBox.class).isVisible()).isTrue();
    }

    @Test
    void whenSearchButtonClick_thenButtonDisabled(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(LOCATION_START);
        robot.clickOn("#searchMiddleTextField");
        robot.write(LOCATION_END);
        robot.clickOn("#searchButton");

        assertThat(robot.lookup("#searchButton").queryAs(Button.class).isDisabled()).isTrue();
        await()
                .pollDelay(Duration.ofSeconds(3))
                .until(() -> true);
        assertThat(robot.lookup("#searchButton").queryAs(Button.class).isDisabled()).isFalse();
    }
}
