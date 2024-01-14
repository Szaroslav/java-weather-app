package pl.edu.agh.to.weatherapp.gui.presenters.weatherpresenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequestException;
import pl.edu.agh.to.weatherapp.gui.presenters.*;
import pl.edu.agh.to.weatherapp.model.internal.Trip;
import pl.edu.agh.to.weatherapp.model.internal.Weather;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.enums.WindIntensity;
import pl.edu.agh.to.weatherapp.service.weather.WeatherService;

import java.io.IOException;
import java.util.List;
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
        ObservableList<Trip> trips = FXCollections.observableArrayList();
        FavouriteTrips favouriteTripsMock = Mockito.mock(FavouriteTrips.class);
        Mockito.when(favouriteTripsMock.getTrips()).thenAnswer((Answer<ObservableList<Trip>>) invocation -> trips);
        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_INVALID), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> {
                    throw new CompletionException(new InvalidRequestException("No matching location found."));
                });
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_VALID), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.COLD)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.BOTH)
                        .setApparentTemperature(TEMP)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN).setLocationNames(List.of(LOCATION_VALID)));
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_VALID), Integer.parseInt(VALID_EARLY_TIME), Integer.parseInt(VALID_LATE_TIME))).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.COLD)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.BOTH)
                        .setApparentTemperature(TEMP)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setLocationNames(List.of(LOCATION_VALID)));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        loader.setControllerFactory(c ->{
            if(c == WeatherPresenter.class){
                return new WeatherPresenter();
            }
            if(c == SearchPresenter.class){
                return new SearchPresenter(weatherServiceMock);
            }
            if(c == FavouritesPresenter.class){
                return new FavouritesPresenter(favouriteTripsMock);
            }
            return new WeatherInfoPresenter();

        });
        GridPane rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void handlingErrorsFromService(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(LOCATION_INVALID);
        robot.clickOn("#searchButton");
        assertThat(robot.lookup("#errorLabel").queryAs(Label.class)).hasText("No matching location found.");

        robot.clickOn("#searchStartTextField");
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
        robot.clickOn("#searchStartTextField");
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
