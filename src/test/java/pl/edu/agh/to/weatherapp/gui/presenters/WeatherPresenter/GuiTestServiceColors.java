package pl.edu.agh.to.weatherapp.gui.presenters.WeatherPresenter;

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
import pl.edu.agh.to.weatherapp.model.internal.*;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;
import pl.edu.agh.to.weatherapp.weather.WeatherService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.testfx.assertions.api.Assertions.assertThat;


@ExtendWith(ApplicationExtension.class)
class GuiTestServiceColorsTestIT {
    private static final String WIND_SUFFIX = "m/s";
    private static final String RAIN_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";
    private static final String COLOR_BACKGROUND = "#f4f4f4";
    private static final List<String> COLORS = Arrays.asList(COLOR_GREEN, COLOR_ORANGE, COLOR_RED);
    private static final List<String> COLORS_CLASSES = Arrays.asList("green", "orange", "red");
    private static final List<String> COLORS_TEST_LOCATIONS = Arrays.asList("HOT_BREEZE_WEAK_NONE","WARM_WINDY_MEDIUM_RAIN", "COLD_STORM_STRONG_SNOW", "COLD_WINDY_WEAK_BOTH");
    private static final int WIND = 0;
    private static final int RAIN = 0;
    private static final int TEMP = 0;
    private static final String ICON_URL = "https://cdn.weatherapi.com/weather/64x64/night/116.png";

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        Mockito.when(weatherServiceMock.getWeatherData(COLORS_TEST_LOCATIONS.get(0))).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLORS_TEST_LOCATIONS.get(0));
                    weatherData.setTemperatureLevel(TemperatureLevel.HOT);
                    weatherData.setWindIntensity(WindIntensity.BREEZE);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                    weatherData.setPrecipitationType(PrecipitationType.NONE);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    weatherData.setConditionIconUrl(ICON_URL);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(COLORS_TEST_LOCATIONS.get(1))).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLORS_TEST_LOCATIONS.get(1));
                    weatherData.setTemperatureLevel(TemperatureLevel.WARM);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.MEDIUM);
                    weatherData.setPrecipitationType(PrecipitationType.RAIN);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    weatherData.setConditionIconUrl(ICON_URL);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(COLORS_TEST_LOCATIONS.get(2))).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLORS_TEST_LOCATIONS.get(2));
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.STORM);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.STRONG);
                    weatherData.setPrecipitationType(PrecipitationType.SNOW);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    weatherData.setConditionIconUrl(ICON_URL);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(COLORS_TEST_LOCATIONS.get(3))).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLORS_TEST_LOCATIONS.get(3));
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
    void HOT_BREEZE_WEAK_NONE(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLORS_TEST_LOCATIONS.get(0));
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLORS_TEST_LOCATIONS.get(0));
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLORS.get(0);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLORS_CLASSES.get(0));
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
    }

    @Test
    void WARM_WINDY_MEDIUM_RAIN(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLORS_TEST_LOCATIONS.get(1));
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLORS_TEST_LOCATIONS.get(1));
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLORS.get(1);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLORS_CLASSES.get(1));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void COLD_STORM_STRONG_SNOW(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLORS_TEST_LOCATIONS.get(2));
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLORS_TEST_LOCATIONS.get(2));
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLORS.get(2);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLORS_CLASSES.get(2));
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
    void COLD_WINDY_WEAK_BOTH(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLORS_TEST_LOCATIONS.get(3));
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLORS_TEST_LOCATIONS.get(3));
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLORS_CLASSES.get(2));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_GREEN));
        assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }
}
