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
    private WeatherData weatherDataFromJsonObject(JsonObject json) {
        Date date = new Date(json.getAsJsonPrimitive("time_epoch").getAsLong());
        String conditionIconUrl = json
            .getAsJsonObject("condition")
            .getAsJsonPrimitive("icon")
            .getAsString();

        int temperatureC      = json.getAsJsonPrimitive("temp_c").getAsInt();
        float windKph         = json.getAsJsonPrimitive("wind_kph").getAsFloat();
        float precipitationMm = json.getAsJsonPrimitive("precip_mm").getAsFloat();
        boolean willRain      = json.getAsJsonPrimitive("will_it_rain").getAsBoolean();
        boolean willSnow      = json.getAsJsonPrimitive("will_it_snow").getAsBoolean();

        return new WeatherData(
            date,
            conditionIconUrl,
            temperatureC,
            windKph,
            precipitationMm,
            willRain,
            willSnow
        );
    }

    public ForecastWeatherData parseForecast(String content) {
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
            WeatherData weatherData = weatherDataFromJsonObject(object);
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
