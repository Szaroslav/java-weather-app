package pl.edu.agh.to.weatherapp.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.WeatherData;
import pl.edu.agh.to.weatherapp.model.ForecastWeatherData;

import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JsonParser {
    public ForecastWeatherData parse(String content) {
        JsonObject json = new Gson()
            .fromJson(new String(
                content.getBytes(),
                StandardCharsets.UTF_8
            ), JsonObject.class);

        if (json.has("error")) {
            String errorMessage = json.getAsJsonObject("error")
                .getAsJsonPrimitive("message")
                .getAsString();
            throw new InvalidRequest(errorMessage);
        } else {
            return forecastWeatherFromJson(json);
        }
    }

//    private WeatherData weatherDataFromJson(JsonObject json) {
//        // Parse location details
//        JsonObject locationJson = json.getAsJsonObject("location");
//        String cityName = locationJson.getAsJsonPrimitive("name").getAsString();
//        String country = locationJson.getAsJsonPrimitive("country").getAsString();
//
//        // Parse current weather details
//        JsonObject currentJson = json.getAsJsonObject("current");
//        double tempC = currentJson.getAsJsonPrimitive("temp_c").getAsDouble();
//
//        // Parse current weather condition details
//        JsonObject conditionJson = currentJson.getAsJsonObject("condition");
//        String conditionIconUrl = conditionJson.getAsJsonPrimitive("icon").getAsString();
//
//        // initialise WeatherData model
//        WeatherData weatherData = new WeatherData();
//        weatherData.setLocationName(cityName + ", " + country);
//        weatherData.setTemperature((int) tempC);
//        weatherData.setConditionIconUrl("https:" + conditionIconUrl);
//
//        return weatherData;
//    }

    private ForecastWeatherData forecastWeatherFromJson(JsonObject json) {
        // Parse location details.
        JsonObject locationJson = json.getAsJsonObject("location");
        String locationName = locationJson.getAsJsonPrimitive("name").getAsString();
        String country = locationJson.getAsJsonPrimitive("country").getAsString();

        JsonArray hourlyWeatherJsonArray = hourlyWeatherFromJson(json);

        // Init forecast weather data.
        ForecastWeatherData forecastWeather = new ForecastWeatherData();
        forecastWeather.setLocationName(String.format("%s, %s", locationName, country));

        // Fill hourly weather list.
        for (JsonElement element : hourlyWeatherJsonArray) {
            JsonObject object = element.getAsJsonObject();

            Date date = new Date(object.getAsJsonPrimitive("time_epoch").getAsLong());
            String conditionIconUrl = object
                .getAsJsonObject("condition")
                .getAsJsonPrimitive("icon")
                .getAsString();

            int temperatureC = object.getAsJsonPrimitive("temp_c").getAsInt();
            float windKph = object.getAsJsonPrimitive("wind_kph").getAsFloat();
            float precipitationMm = object.getAsJsonPrimitive("precip_mm").getAsFloat();
            boolean willRain = object.getAsJsonPrimitive("will_it_rain").getAsBoolean();
            boolean willSnow = object.getAsJsonPrimitive("will_it_snow").getAsBoolean();

            WeatherData weatherData = new WeatherData(
                date,
                conditionIconUrl,
                temperatureC,
                windKph,
                precipitationMm,
                willRain,
                willSnow
            );

            forecastWeather.getHourlyWeatherForecasts().add(weatherData);
        }

        return forecastWeather;
    }

    private JsonArray hourlyWeatherFromJson(JsonObject json) {
        JsonArray dailyWeatherJsonArray = json
            .getAsJsonObject("forecast")
            .getAsJsonArray("forecastday");
        if (dailyWeatherJsonArray.isEmpty()) {
            throw new InvalidRequest("Cannot fetch a forecast.");
        }

        JsonArray hourlyWeatherJsonArray = dailyWeatherJsonArray.get(0)
            .getAsJsonObject()
            .getAsJsonArray("hour");
        if (hourlyWeatherJsonArray.isEmpty()) {
            throw new InvalidRequest("Cannot fetch a forecast.");
        }

        return hourlyWeatherJsonArray;
    }
}
