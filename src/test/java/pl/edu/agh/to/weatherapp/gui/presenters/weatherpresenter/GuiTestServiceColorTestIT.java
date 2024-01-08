package pl.edu.agh.to.weatherapp.gui.presenters.weatherpresenter;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.SVGPath;
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
class GuiTestServiceColorTestIT {
    private static final String WIND_SUFFIX = "m/s";
    private static final String RAIN_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_YELLOW = "#FFD500";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";
    private static final String COLOR_BACKGROUND = "#f4f4f4";
    private static final String COLOR_CLASS_RED = "red";
    private static final String COLOR_CLASS_YELLOW = "yellow";
    private static final String COLOR_CLASS_GREEN = "green";
    private static final String COLOR_CLASS_ORANGE = "orange";
    private static final String HOT_BREEZE_WEAK_NONE_MUD = "H_B_W_N";
    private static final String WARM_WINDY_MEDIUM_RAIN_NO_MUD = "W_W_M_R";
    private static final String COLD_STORM_STRONG_SNOW = "C_S_S_S";
    private static final String FREEZING_WINDY_WEAK_BOTH = "C_W_W_B";
    private static final int WIND = 0;
    private static final int RAIN = 0;
    private static final int TEMP_FREEZING = -10;
    private static final int TEMP_COLD = 2;
    private static final int TEMP_WARM = 6;
    private static final int TEMP_HOT = 30;
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
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(HOT_BREEZE_WEAK_NONE_MUD), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.HOT)
                        .setWindIntensity(WindIntensity.BREEZE)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.NONE)
                        .setApparentTemperature(TEMP_HOT)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setMud(true)
                        .setLocationNames(List.of(HOT_BREEZE_WEAK_NONE_MUD)));
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(WARM_WINDY_MEDIUM_RAIN_NO_MUD), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.WARM)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.MEDIUM)
                        .setPrecipitationType(PrecipitationType.RAIN)
                        .setApparentTemperature(TEMP_WARM)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setMud(false)
                        .setLocationNames(List.of(WARM_WINDY_MEDIUM_RAIN_NO_MUD)));
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(COLD_STORM_STRONG_SNOW), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.COLD)
                        .setWindIntensity(WindIntensity.STORM)
                        .setPrecipitationIntensity(PrecipitationIntensity.STRONG)
                        .setPrecipitationType(PrecipitationType.SNOW)
                        .setApparentTemperature(TEMP_COLD)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setMud(true)
                        .setLocationNames(List.of(COLD_STORM_STRONG_SNOW)));
        Mockito.when(weatherServiceMock.getForecastSummaryWeatherData(List.of(FREEZING_WINDY_WEAK_BOTH), START_HOUR, END_HOUR)).thenAnswer(
                (Answer<Weather>) invocation -> new Weather()
                        .setTemperatureLevel(TemperatureLevel.FREEZING)
                        .setWindIntensity(WindIntensity.WINDY)
                        .setPrecipitationIntensity(PrecipitationIntensity.WEAK)
                        .setPrecipitationType(PrecipitationType.BOTH)
                        .setApparentTemperature(TEMP_FREEZING)
                        .setWindInMps(WIND)
                        .setPrecipitationInMm(RAIN)
                        .setMud(true)
                        .setLocationNames(List.of(FREEZING_WINDY_WEAK_BOTH)));

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        Mockito.when(favouriteTripsMock.getTrips()).thenAnswer((Answer<ObservableList<Trip>>) invocation -> trips);
        loader.setControllerFactory(c ->
                new WeatherPresenter(weatherServiceMock, favouriteTripsMock));
        GridPane rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void checkColorsWhen_hotBreezeWeakNoneMud(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(HOT_BREEZE_WEAK_NONE_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(HOT_BREEZE_WEAK_NONE_MUD);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP_HOT));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLOR_GREEN;
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(color));

        assertThat(robot.lookup("#mudSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void checkColorsWhen_warmWindyMediumRain(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(WARM_WINDY_MEDIUM_RAIN_NO_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(WARM_WINDY_MEDIUM_RAIN_NO_MUD);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP_WARM));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);

        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_YELLOW);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();

        assertThat(robot.lookup("#mudSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_GREEN));
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));
    }

    @Test
    void checkColorsWhen_COLDStormStrongSnow(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLD_STORM_STRONG_SNOW);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP_COLD));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);

        String color = COLOR_RED;
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void checkColorsWhen_FREEZINGWindyWeakBoth(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(FREEZING_WINDY_WEAK_BOTH);
        robot.type(KeyCode.ENTER);

        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(FREEZING_WINDY_WEAK_BOTH);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP_FREEZING));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);

        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_RED);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_YELLOW));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_YELLOW));
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void colorsChangeWithDataChangeMud(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(HOT_BREEZE_WEAK_NONE_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#mudSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).isVisible()).isFalse();

        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, HOT_BREEZE_WEAK_NONE_MUD.length());
        robot.write(WARM_WINDY_MEDIUM_RAIN_NO_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#mudSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_GREEN));
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));

        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, WARM_WINDY_MEDIUM_RAIN_NO_MUD.length());
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLD_STORM_STRONG_SNOW);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP_COLD));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        assertThat(robot.lookup("#mudSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#noMudBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noMudLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void colorsChangeWithDataChange(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(HOT_BREEZE_WEAK_NONE_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_GREEN));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_GREEN));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));

        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, HOT_BREEZE_WEAK_NONE_MUD.length());
        robot.write(WARM_WINDY_MEDIUM_RAIN_NO_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_YELLOW);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));

        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, WARM_WINDY_MEDIUM_RAIN_NO_MUD.length());
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_RED));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_RED));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }


    @Test
    void colorsChangeWithDataChangeReversed(FxRobot robot) {
        robot.clickOn("#searchStartTextField");
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_RED));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_RED));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, COLD_STORM_STRONG_SNOW.length());
        robot.write(WARM_WINDY_MEDIUM_RAIN_NO_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_YELLOW);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_ORANGE));

        robot.clickOn("#searchStartTextField");
        robot.type(KeyCode.BACK_SPACE, WARM_WINDY_MEDIUM_RAIN_NO_MUD.length());
        robot.write(HOT_BREEZE_WEAK_NONE_MUD);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_GREEN));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_GREEN));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isTrue();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_GREEN));
    }
}
