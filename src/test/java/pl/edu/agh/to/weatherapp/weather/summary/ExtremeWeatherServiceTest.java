package pl.edu.agh.to.weatherapp.weather.summary;

import org.junit.jupiter.api.Test;
import pl.edu.agh.to.weatherapp.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.dto.WeatherApiDto;
import pl.edu.agh.to.weatherapp.model.InternalWeatherData;
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
