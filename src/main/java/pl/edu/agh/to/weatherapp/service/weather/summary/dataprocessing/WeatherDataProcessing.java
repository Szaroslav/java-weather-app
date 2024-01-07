package pl.edu.agh.to.weatherapp.service.weather.summary.dataprocessing;

public class WeatherDataProcessing {
    private WeatherDataProcessing() {}

    public static float kphToMps(float kph) {
        return kph * 1000 / 60 / 60;
    }

    public static float getApparentTemperature(float temperature, float windInKph) {
        // formula: https://pl.wikipedia.org/wiki/Temperatura_odczuwalna
        double windPow = Math.pow(windInKph, 0.16);
        return (float) (13.12 + 0.6215 * temperature - 11.37 * windPow + 0.3965 * temperature * windPow);
    }
}
