package pl.edu.agh.to.weatherapp.dataprocessing;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class WeatherDataProcessingTest {
    @Test
    void kphToMpsTest() {
        assertThat(WeatherDataProcessing.kphToMps(72)).isEqualTo(20);
        assertThat(WeatherDataProcessing.kphToMps(36)).isEqualTo(10);
        assertThat(WeatherDataProcessing.kphToMps(18)).isEqualTo(5);
        assertThat(WeatherDataProcessing.kphToMps(0)).isZero();
    }

    @Test
    void getApparentTemperatureTest() {
        // based on apparent temperature table at https://pl.wikipedia.org/wiki/Temperatura_odczuwalna
        assertEquals(8.6, WeatherDataProcessing.getApparentTemperature(10, 10),0.1);
        assertEquals(2.7, WeatherDataProcessing.getApparentTemperature(5, 10),0.1);
        assertEquals(-1, WeatherDataProcessing.getApparentTemperature(5, 45),0.1);
        assertEquals(-17.9, WeatherDataProcessing.getApparentTemperature(-10, 20),0.1);
        assertEquals(-40, WeatherDataProcessing.getApparentTemperature(-25, 35),0.1);
    }
}
