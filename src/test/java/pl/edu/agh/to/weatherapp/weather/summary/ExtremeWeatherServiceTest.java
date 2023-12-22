package pl.edu.agh.to.weatherapp.weather.summary;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.dto.WeatherApiDto;
import pl.edu.agh.to.weatherapp.model.Weather;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationIntensity;
import pl.edu.agh.to.weatherapp.model.enums.PrecipitationType;
import pl.edu.agh.to.weatherapp.model.enums.TemperatureLevel;
import pl.edu.agh.to.weatherapp.model.enums.WindIntensity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExtremeWeatherServiceTest {
    private final ExtremeWeatherService extremeWeatherService = new ExtremeWeatherService();

    @Test
    void oneLocationOneForecastTest() {
        //given
        List<ForecastWeatherApiDto> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherApiDto forecastWeatherDto = new ForecastWeatherApiDto();
        WeatherApiDto weatherDto = new WeatherApiDto()
                .setTemperatureC(30)
                .setWindKph(72)
                .setPrecipitationMm(20)
                .setWillRain(true)
                .setWillSnow(true);
        forecastWeatherDto.getHourlyWeatherForecasts().add(weatherDto);
        weatherDataList.add(forecastWeatherDto);

        //when
        Weather weather = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(weather.getTemperatureLevel()).isEqualTo(TemperatureLevel.HOT);
        assertThat(weather.getTemperature()).isEqualTo(30);
        assertThat(weather.getApparentTemperature()).isEqualTo(32);
        assertThat(weather.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.STRONG);
        assertThat(weather.getPrecipitationType()).isEqualTo(PrecipitationType.BOTH);
        assertThat(weather.getPrecipitationInMm()).isEqualTo(20);
        assertThat(weather.getWindIntensity()).isEqualTo(WindIntensity.STORM);
        assertThat(weather.getWindInMps()).isEqualTo(20);
    }

    @Test
    void oneLocationMultipleForecastsTest() {
        //given
        List<ForecastWeatherApiDto> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherApiDto forecastWeatherDto = new ForecastWeatherApiDto();
        for (int i = 0; i <= 10; i++) {
            WeatherApiDto weatherDto = new WeatherApiDto()
                    .setTemperatureC(15 + i)
                    .setWindKph(26 + i)
                    .setPrecipitationMm(i)
                    .setWillRain(true)
                    .setWillSnow(false);
            forecastWeatherDto.getHourlyWeatherForecasts().add(weatherDto);
        }
        weatherDataList.add(forecastWeatherDto);

        //when
        Weather weather = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(weather.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(weather.getTemperature()).isEqualTo(15);
        assertThat(weather.getApparentTemperature()).isEqualTo(13);
        assertThat(weather.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.MEDIUM);
        assertThat(weather.getPrecipitationType()).isEqualTo(PrecipitationType.RAIN);
        assertThat(weather.getPrecipitationInMm()).isEqualTo(10);
        assertThat(weather.getWindIntensity()).isEqualTo(WindIntensity.WINDY);
        assertThat(weather.getWindInMps()).isEqualTo(10);
    }

    @Test
    void twoLocationsMultipleForecastsTest() {
        //given
        List<ForecastWeatherApiDto> weatherDataList = new java.util.ArrayList<>();
        ForecastWeatherApiDto forecastWeatherDto1 = new ForecastWeatherApiDto();
        ForecastWeatherApiDto forecastWeatherDto2 = new ForecastWeatherApiDto();
        for (int i = 0; i <= 10; i++) {
            WeatherApiDto weatherDto = new WeatherApiDto()
                    .setTemperatureC(15 + i)
                    .setWindKph(8 + i)
                    .setPrecipitationMm((float) i / 2)
                    .setWillRain(false)
                    .setWillSnow(true);
            WeatherApiDto weatherDto2 = new WeatherApiDto()
                    .setPrecipitationMm(0)
                    .setWindKph(10 - i)
                    .setTemperatureC(15 - i)
                    .setWillRain(true)
                    .setWillSnow(false);
            forecastWeatherDto1.getHourlyWeatherForecasts().add(weatherDto);
            forecastWeatherDto2.getHourlyWeatherForecasts().add(weatherDto2);
        }
        weatherDataList.add(forecastWeatherDto1);
        weatherDataList.add(forecastWeatherDto2);

        //when
        Weather weather = extremeWeatherService.getSummary(weatherDataList);

        //then
        assertThat(weather.getTemperatureLevel()).isEqualTo(TemperatureLevel.WARM);
        assertThat(weather.getTemperature()).isEqualTo(5);
        assertThat(weather.getApparentTemperature()).isEqualTo(7);
        assertThat(weather.getPrecipitationIntensity()).isEqualTo(PrecipitationIntensity.WEAK);
        assertThat(weather.getPrecipitationType()).isEqualTo(PrecipitationType.BOTH);
        assertThat(weather.getPrecipitationInMm()).isEqualTo(5);
        assertThat(weather.getWindIntensity()).isEqualTo(WindIntensity.BREEZE);
        assertThat(weather.getWindInMps()).isEqualTo(5);
    }
}
