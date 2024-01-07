package pl.edu.agh.to.weatherapp.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.dto.ForecastWeatherApiDto;
import pl.edu.agh.to.weatherapp.model.dto.WeatherApiDto;

import java.nio.charset.StandardCharsets;

public class JsonParser {
    private WeatherApiDto weatherDataFromJsonObject(JsonObject json) {
        DateTime date = new DateTime(json.getAsJsonPrimitive("time_epoch").getAsLong() * 1000);
        String conditionIconUrl = json
                .getAsJsonObject("condition")
                .getAsJsonPrimitive("icon")
                .getAsString();
        int temperatureC = json.getAsJsonPrimitive("temp_c").getAsInt();
        float windKph = json.getAsJsonPrimitive("wind_kph").getAsFloat();
        float precipitationMm = json.getAsJsonPrimitive("precip_mm").getAsFloat();
        boolean willRain = Integer.parseInt(json.getAsJsonPrimitive("chance_of_rain").getAsString()) > 0;
        boolean willSnow = Integer.parseInt(json.getAsJsonPrimitive("chance_of_snow").getAsString()) > 0;
        return new WeatherApiDto(
                date,
                conditionIconUrl,
                temperatureC,
                windKph,
                precipitationMm,
                willRain,
                willSnow
        );
    }

    public ForecastWeatherApiDto parseForecast(String content) {
        JsonObject json = new Gson()
                .fromJson(new String(content.getBytes(), StandardCharsets.UTF_8), JsonObject.class);
        if (json.has("error")) {
            String errorMessage = json
                    .getAsJsonObject("error")
                    .getAsJsonPrimitive("message")
                    .getAsString();
            throw new InvalidRequest(errorMessage);
        } else {
            return forecastWeatherFromJson(json);
        }
    }

    private ForecastWeatherApiDto forecastWeatherFromJson(JsonObject json) {
        // Parse location details.
        JsonObject locationJson = json.getAsJsonObject("location");
        String locationName = locationJson.getAsJsonPrimitive("name").getAsString();
        String country = locationJson.getAsJsonPrimitive("country").getAsString();

        JsonArray hourlyWeatherJsonArray = hourlyWeatherFromJson(json);

        // Init forecast weather data.
        ForecastWeatherApiDto forecastWeather = new ForecastWeatherApiDto();
        forecastWeather.setLocationName(String.format("%s, %s", locationName, country));

        // Fill hourly weather list.
        for (JsonElement element : hourlyWeatherJsonArray) {
            JsonObject object = element.getAsJsonObject();
            WeatherApiDto weatherDto = weatherDataFromJsonObject(object);
            forecastWeather.getHourlyWeatherForecasts().add(weatherDto);
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
