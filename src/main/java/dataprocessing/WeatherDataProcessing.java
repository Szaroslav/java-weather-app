package dataprocessing;

public class WeatherDataProcessing {
    public static float kphToMps(float kph) {
        return kph * 1000 / 60 / 60;
    }
}
