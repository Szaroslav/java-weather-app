package pl.edu.agh.to.weatherapp.gui.presenters.weatherpresenter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.SVGPath;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import java.time.Duration;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.mockito.Mockito.doNothing;
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestFavouritesTestIT {

    private static final String LOCATION_START = "Ryglice";
    private static final String LOCATION_END = "Kraków";
    private static final String LOCATION_MIDDLE = "Tarnów";
    private static final String CITY_NAMES_SEPARATOR = " → ";
    private static final int WIND = 1;
    private static final int RAIN = 2;
    private static final int TEMP = 3;
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;

    private static final String FIRST_TEXT_FIELD_ID = "#searchStartTextField";
    private static final String SECOND_TEXT_FIELD_ID = "#searchMiddleTextField";

    private static final String COLOR_ORANGE = "#FFB347";

    Trip trip2 = new Trip(List.of(LOCATION_START, LOCATION_END));
    Trip trip3 = new Trip(List.of(LOCATION_START, LOCATION_MIDDLE));
    ArgumentCaptor<Trip> valueCapture = ArgumentCaptor.forClass(Trip.class);
    ArgumentCaptor<Trip> valueCaptureDelete = ArgumentCaptor.forClass(Trip.class);
    ObservableList<Trip> trips = FXCollections.observableArrayList();

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        FavouriteTrips favouriteTripsMock = Mockito.mock(FavouriteTrips.class);
        Mockito.when(favouriteTripsMock.getTrips()).thenAnswer((Answer<ObservableList<Trip>>) invocation -> trips);

        doNothing().when(favouriteTripsMock).addTrip(valueCapture.capture());
        doNothing().when(favouriteTripsMock).deleteTrip(valueCaptureDelete.capture());

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_MIDDLE), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.COLD)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.BOTH)
                        .setApparentTemperature(TEMP)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setLocationNames(List.of(LOCATION_START, LOCATION_MIDDLE)));

        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(LOCATION_START, LOCATION_END), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.COLD)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.BOTH)
                        .setApparentTemperature(TEMP)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setLocationNames(List.of(LOCATION_START, LOCATION_END)));


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
    void addFavouriteTripSimple(FxRobot robot) {
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.write(LOCATION_END);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#starSVGPath");

        assertThat(robot.lookup("#starSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));

        Assertions.assertEquals(trip2, valueCapture.getValue());
        trips.add(valueCapture.getValue());

        Assertions.assertTrue(robot.lookup("#favouritesListView").queryAs(ListView.class).getItems().contains(valueCapture.getValue()));
    }

    @Test
    void deleteFavouriteTripSimple(FxRobot robot) {
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.write(LOCATION_END);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#starSVGPath");

        assertThat(robot.lookup("#starSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));

        Assertions.assertEquals(trip2, valueCapture.getValue());
        trips.add(valueCapture.getValue());

        Assertions.assertTrue(robot.lookup("#favouritesListView").queryAs(ListView.class).getItems().contains(valueCapture.getValue()));

        robot.clickOn("#starSVGPath");
        assertThat(robot.lookup("#starSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE, 0));
        Assertions.assertEquals(trip2, valueCaptureDelete.getValue());

        Platform.runLater(() ->
                trips.remove(valueCaptureDelete.getValue())
        );

    }

    @Test
    void deleteFavouriteTripSidePanel(FxRobot robot) {
        robot.clickOn(FIRST_TEXT_FIELD_ID);
        robot.write(LOCATION_START);
        robot.clickOn(SECOND_TEXT_FIELD_ID);
        robot.write(LOCATION_END);
        robot.type(KeyCode.ENTER);
        robot.clickOn("#starSVGPath");

        assertThat(robot.lookup("#starSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));

        Assertions.assertEquals(trip2, valueCapture.getValue());
        trips.add(valueCapture.getValue());

        Assertions.assertTrue(robot.lookup("#favouritesListView").queryAs(ListView.class).getItems().contains(valueCapture.getValue()));

        await()
                .pollDelay(Duration.ofMillis(300))
                .until(() -> true);

        robot.clickOn("Delete");
        Assertions.assertEquals(trip2, valueCaptureDelete.getValue());
        Platform.runLater(() ->
                trips.remove(valueCaptureDelete.getValue())
        );

        assertThat(robot.lookup("#starSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE, 0));
        Assertions.assertFalse(robot.lookup("#favouritesListView").queryAs(ListView.class).getItems().contains(valueCaptureDelete.getValue()));
    }

    @Test
    void showWeatherDataOnClick(FxRobot robot) {
        trips.add(trip3);
        await()
                .pollDelay(Duration.ofMillis(300))
                .until(() -> true);

        robot.clickOn(String.join(CITY_NAMES_SEPARATOR, trip3.getLocationNames()));

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(LOCATION_START + CITY_NAMES_SEPARATOR + LOCATION_MIDDLE);
    }

}
