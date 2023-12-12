package pl.edu.agh.to.weatherapp.weather.summary;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.internal.InternalWeatherData;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.internal.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.internal.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.internal.WindIntensity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtremeWeatherServiceTest {
    private final ExtremeWeatherService extremeWeatherService = new ExtremeWeatherService();

    @Test
    void oneLocationOneForecastTest() {
        //given
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData = new ForecastWeatherData();
        WeatherData weatherData = new WeatherData()
                .setTemperatureC(30)
                .setWindKph(72)
                .setPrecipitationMm(20)
                .setWillRain(true)
                .setWillSnow(true);
        forecastWeatherData.getHourlyWeatherForecasts().add(weatherData);
        weatherDataList.add(forecastWeatherData);

        //when
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.HOT);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(30);
        assertThat(internalWeatherData.getApparentTemperature()).isEqualTo(32);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.STRONG);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.BOTH);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(20);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.STORM);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(20);
    }

    @Test
    void oneLocationMultipleForecastsTest() {
        //given
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData = new ForecastWeatherData();
        for (int i = 0; i <= 10; i++) {
            WeatherData weatherData = new WeatherData()
                    .setTemperatureC(15 + i)
                    .setWindKph(26 + i)
                    .setPrecipitationMm(i)
                    .setWillRain(true)
                    .setWillSnow(false);
            forecastWeatherData.getHourlyWeatherForecasts().add(weatherData);
        }
        weatherDataList.add(forecastWeatherData);

        //when
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(15);
        assertThat(internalWeatherData.getApparentTemperature()).isEqualTo(13);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.MEDIUM);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.RAIN);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(10);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.WINDY);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(10);
    }

    @Test
    void twoLocationsMultipleForecastsTest() {
        //given
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData1 = new ForecastWeatherData();
        ForecastWeatherData forecastWeatherData2 = new ForecastWeatherData();
        for (int i = 0; i <= 10; i++) {
            WeatherData weatherData = new WeatherData()
                    .setTemperatureC(15 + i)
                    .setWindKph(8 + i)
                    .setPrecipitationMm((float) i / 2)
                    .setWillRain(false)
                    .setWillSnow(true);
            WeatherData weatherData2 = new WeatherData()
                    .setPrecipitationMm(0)
                    .setWindKph(10 - i)
                    .setTemperatureC(15 - i)
                    .setWillRain(true)
                    .setWillSnow(false);
            forecastWeatherData1.getHourlyWeatherForecasts().add(weatherData);
            forecastWeatherData2.getHourlyWeatherForecasts().add(weatherData2);
        }
        weatherDataList.add(forecastWeatherData1);
        weatherDataList.add(forecastWeatherData2);

        //when
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(5);
        assertThat(internalWeatherData.getApparentTemperature()).isEqualTo(7);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.WEAK);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.BOTH);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(5);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.BREEZE);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(5);
    }
}
