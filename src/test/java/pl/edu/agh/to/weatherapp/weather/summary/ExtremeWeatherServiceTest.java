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

    @Test
    void oneLocationOneForecastTest() {
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData = new ForecastWeatherData();

        WeatherData weatherData = new WeatherData();
        weatherData.setTemperatureC(30);
        weatherData.setWindKph(72);
        weatherData.setPrecipitationMm(20);
        weatherData.setWillRain(true);
        weatherData.setWillSnow(true);

        forecastWeatherData.getHourlyWeatherForecasts().add(weatherData);
        weatherDataList.add(forecastWeatherData);

        ExtremeWeatherService extremeWeatherService = new ExtremeWeatherService();
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.HOT);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(30);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.STRONG);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.BOTH);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(20);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.STORM);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(20);
    }

    @Test
    void oneLocationMultipleForecastsTest() {
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData = new ForecastWeatherData();

        for (int i=0;i<=10;i++) {
            WeatherData weatherData = new WeatherData();
            weatherData.setTemperatureC(15+i);
            weatherData.setWindKph(26+i);
            weatherData.setPrecipitationMm(i);
            weatherData.setWillRain(true);
            weatherData.setWillSnow(false);
            forecastWeatherData.getHourlyWeatherForecasts().add(weatherData);
        }
        weatherDataList.add(forecastWeatherData);

        ExtremeWeatherService extremeWeatherService = new ExtremeWeatherService();
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(15);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.MEDIUM);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.RAIN);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(10);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.WINDY);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(10);
    }

    @Test
    void twoLocationsMultipleForecastsTest() {
        List<ForecastWeatherData> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherData forecastWeatherData1 = new ForecastWeatherData();
        ForecastWeatherData forecastWeatherData2 = new ForecastWeatherData();

        for (int i=0;i<=10;i++) {
            WeatherData weatherData = new WeatherData();
            WeatherData weatherData2 = new WeatherData();
            weatherData.setTemperatureC(15+i);
            weatherData2.setTemperatureC(15-i);
            weatherData.setWindKph(8+i);
            weatherData2.setWindKph(10-i);
            weatherData.setPrecipitationMm((float)i/2);
            weatherData2.setPrecipitationMm(0);
            weatherData.setWillRain(false);
            weatherData.setWillSnow(true);
            forecastWeatherData1.getHourlyWeatherForecasts().add(weatherData);
            forecastWeatherData2.getHourlyWeatherForecasts().add(weatherData2);
        }
        weatherDataList.add(forecastWeatherData1);
        weatherDataList.add(forecastWeatherData2);

        ExtremeWeatherService extremeWeatherService = new ExtremeWeatherService();
        InternalWeatherData internalWeatherData = extremeWeatherService.getSummary(weatherDataList);

        assertThat(internalWeatherData.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(internalWeatherData.getTemperature()).isEqualTo(5);
        assertThat(internalWeatherData.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.WEAK);
        assertThat(internalWeatherData.getPrecipitationType()).isEqualTo(PrecipitationType.SNOW);
        assertThat(internalWeatherData.getPrecipitationInMm()).isEqualTo(5);
        assertThat(internalWeatherData.getWindIntensity()).isEqualTo(WindIntensity.BREEZE);
        assertThat(internalWeatherData.getWindInMps()).isEqualTo(5);
    }
}
