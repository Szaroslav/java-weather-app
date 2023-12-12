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
import org.assertj.core.api.Assertions;
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
import static org.testfx.assertions.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
class GuiTestServiceColorTestIT {
    private static final String WIND_SUFFIX = "m/s";
    private static final String RAIN_SUFFIX = "mm";
    private static final String COLOR_GREEN = "#77dd77";
    private static final String COLOR_ORANGE = "#FFB347";
    private static final String COLOR_RED = "#FF6961";
    private static final String COLOR_BACKGROUND = "#f4f4f4";
    private static final String COLOR_CLASS_RED = "red";
    private static final String COLOR_CLASS_GREEN = "green";
    private static final String COLOR_CLASS_ORANGE = "orange";
    private static final String HOT_BREEZE_WEAK_NONE = "H_B_W_N";
    private static final String WARM_WINDY_MEDIUM_RAIN = "W_W_M_R";
    private static final String COLD_STORM_STRONG_SNOW = "C_S_S_S";
    private static final String COLD_WINDY_WEAK_BOTH = "C_W_W_B";
    private static final int WIND = 0;
    private static final int RAIN = 0;
    private static final int TEMP = 0;
    private static final int START_HOUR = 0;
    private static final int END_HOUR = 24;

    @Start
    private void start(Stage stage) throws IOException {
        stage.setTitle("Potezna wichura");
        stage.setMinWidth(400);
        stage.setMinHeight(400);

        WeatherService weatherServiceMock = Mockito.mock((WeatherService.class));
        Mockito.when(weatherServiceMock.getWeatherData(HOT_BREEZE_WEAK_NONE, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(HOT_BREEZE_WEAK_NONE);
                    weatherData.setTemperatureLevel(TemperatureLevel.HOT);
                    weatherData.setWindIntensity(WindIntensity.BREEZE);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.WEAK);
                    weatherData.setPrecipitationType(PrecipitationType.NONE);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(WARM_WINDY_MEDIUM_RAIN, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(WARM_WINDY_MEDIUM_RAIN);
                    weatherData.setTemperatureLevel(TemperatureLevel.WARM);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.MEDIUM);
                    weatherData.setPrecipitationType(PrecipitationType.RAIN);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(COLD_STORM_STRONG_SNOW, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLD_STORM_STRONG_SNOW);
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.STORM);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.STRONG);
                    weatherData.setPrecipitationType(PrecipitationType.SNOW);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
                    return weatherData;
                });
        Mockito.when(weatherServiceMock.getWeatherData(COLD_WINDY_WEAK_BOTH, START_HOUR, END_HOUR)).thenAnswer(
                (Answer<InternalWeatherData>) invocation -> {
                    InternalWeatherData weatherData = new InternalWeatherData();
                    weatherData.getLocationNames().add(COLD_WINDY_WEAK_BOTH);
                    weatherData.setTemperatureLevel(TemperatureLevel.COLD);
                    weatherData.setWindIntensity(WindIntensity.WINDY);
                    weatherData.setPrecipitationIntensity(PrecipitationIntensity.MEDIUM);
                    weatherData.setPrecipitationType(PrecipitationType.BOTH);
                    weatherData.setTemperature(TEMP);
                    weatherData.setWindInMps(WIND);
                    weatherData.setPrecipitationInMm(RAIN);
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
    void checkColorsWhen_hotBreezeWeakNone(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(HOT_BREEZE_WEAK_NONE);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(HOT_BREEZE_WEAK_NONE);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLOR_GREEN;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(color));
    }

    @Test
    void checkColorsWhen_warmWindyMediumRain(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(WARM_WINDY_MEDIUM_RAIN);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(WARM_WINDY_MEDIUM_RAIN);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLOR_ORANGE;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void checkColorsWhen_coldStormStrongSnow(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLD_STORM_STRONG_SNOW);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        String color = COLOR_RED;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_RED);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void checkColorsWhen_coldWindyWeakBoth(FxRobot robot) {
        robot.clickOn("#searchTextField");
        robot.write(COLD_WINDY_WEAK_BOTH);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLD_WINDY_WEAK_BOTH);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_RED);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(COLOR_ORANGE));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(COLOR_ORANGE));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void colorsChangeWithDataChange(FxRobot robot){
        robot.clickOn("#searchTextField");
        robot.write(HOT_BREEZE_WEAK_NONE);
        robot.type(KeyCode.ENTER);
        String color = COLOR_GREEN;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(color));
        robot.clickOn("#searchTextField");
        robot.type(KeyCode.BACK_SPACE, HOT_BREEZE_WEAK_NONE.length());
        robot.write(WARM_WINDY_MEDIUM_RAIN);
        robot.type(KeyCode.ENTER);
        color = COLOR_ORANGE;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        robot.clickOn("#searchTextField");
        robot.type(KeyCode.BACK_SPACE, WARM_WINDY_MEDIUM_RAIN.length());
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(COLD_STORM_STRONG_SNOW);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        color = COLOR_RED;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_RED);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isFalse();
    }

    @Test
    void colorsChangeWithDataChangeReversed(FxRobot robot){
        String color;
        robot.clickOn("#searchTextField");
        robot.write(COLD_STORM_STRONG_SNOW);
        robot.type(KeyCode.ENTER);
        color = COLOR_RED;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_RED);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        robot.clickOn("#searchTextField");
        robot.type(KeyCode.BACK_SPACE, COLD_STORM_STRONG_SNOW.length());
        robot.write(WARM_WINDY_MEDIUM_RAIN);
        robot.type(KeyCode.ENTER);
        color = COLOR_ORANGE;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_ORANGE);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).getFill()).isEqualTo(Color.web(COLOR_RED));
        robot.clickOn("#searchTextField");
        robot.type(KeyCode.BACK_SPACE, WARM_WINDY_MEDIUM_RAIN.length());
        robot.write(HOT_BREEZE_WEAK_NONE);
        robot.type(KeyCode.ENTER);
        assertThat(robot.lookup("#locationLabel").queryAs(Label.class))
                .hasText(HOT_BREEZE_WEAK_NONE);
        assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class))
                .hasText(String.valueOf(TEMP));
        assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class))
                .hasText(RAIN + RAIN_SUFFIX);
        assertThat(robot.lookup("#windLabel").queryAs(Label.class))
                .hasText(WIND + WIND_SUFFIX);
        color = COLOR_GREEN;
        Assertions.assertThat(robot.lookup("#temperatureBox").queryAs(StackPane.class).getStyleClass()).contains(COLOR_CLASS_GREEN);
        Assertions.assertThat(robot.lookup("#precipitationLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windLabel").queryAs(Label.class).getTextFill()).isEqualTo(Paint.valueOf(color));
        Assertions.assertThat(robot.lookup("#windSVGPath").queryAs(SVGPath.class).getStroke()).isEqualTo(Color.web(color));
        Assertions.assertThat(robot.lookup("#rainSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowSVGPath").queryAs(SVGPath.class).isVisible()).isFalse();
        Assertions.assertThat(robot.lookup("#snowRainSVGPath").queryAs(SVGPath.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).isVisible()).isTrue();
        Assertions.assertThat(robot.lookup("#noPrecipitationBackLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(COLOR_BACKGROUND));
        Assertions.assertThat(robot.lookup("#noPrecipitationLine").queryAs(Line.class).getStroke()).isEqualTo(Color.web(color));
    }
}
