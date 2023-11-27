package pl.edu.agh.to.weatherapp.parser;

import pl.edu.agh.to.weatherapp.model.WeatherData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class JsonParser implements IParser {
    @Override
    public WeatherData parse(String content) {
        JsonObject json = new Gson().fromJson(content, JsonObject.class);

        if (json.has("error")) {
            return handleErrorResponse(json.getAsJsonObject("error"));
        } else {
            return parseWeatherData(json);
        }
    }

    private WeatherData handleErrorResponse(JsonObject errorJson) {

        int errorCode = errorJson.getAsJsonPrimitive("code").getAsInt();
        String errorMessage = errorJson.getAsJsonPrimitive("message").getAsString();

        /*
          TODO:
            Handle somehow error response.
            eg. "code":1006,"message":"No matching location found."
        */

        WeatherData weatherData = new WeatherData();
        weatherData.setLocationName("Error " + errorCode + ": " + errorMessage);
        weatherData.setTemp(-1);

        return weatherData;
    }

    private  WeatherData parseWeatherData(JsonObject json) {

        // Parse location details
        JsonObject locationJson = json.getAsJsonObject("location");
        String cityName = locationJson.getAsJsonPrimitive("name").getAsString();
        String region = locationJson.getAsJsonPrimitive("region").getAsString();
        String country = locationJson.getAsJsonPrimitive("country").getAsString();
        double latitude = locationJson.getAsJsonPrimitive("lat").getAsDouble();
        double longitude = locationJson.getAsJsonPrimitive("lon").getAsDouble();
        String timezoneId = locationJson.getAsJsonPrimitive("tz_id").getAsString();
        long localtimeEpoch = locationJson.getAsJsonPrimitive("localtime_epoch").getAsLong();
        String localtime = locationJson.getAsJsonPrimitive("localtime").getAsString();

        // Parse current weather details
        JsonObject currentJson = json.getAsJsonObject("current");
        long lastUpdatedEpoch = currentJson.getAsJsonPrimitive("last_updated_epoch").getAsLong();
        String lastUpdated = currentJson.getAsJsonPrimitive("last_updated").getAsString();
        double tempC = currentJson.getAsJsonPrimitive("temp_c").getAsDouble();
        double tempF = currentJson.getAsJsonPrimitive("temp_f").getAsDouble();
        boolean isDay = currentJson.getAsJsonPrimitive("is_day").getAsInt() == 1;

        // Parse current weather condition details
        JsonObject conditionJson = currentJson.getAsJsonObject("condition");
        String conditionText = conditionJson.getAsJsonPrimitive("text").getAsString();
        String conditionIconUrl = conditionJson.getAsJsonPrimitive("icon").getAsString();
        int conditionCode = conditionJson.getAsJsonPrimitive("code").getAsInt();

        // Parse rest current weather details
        double windMph = currentJson.getAsJsonPrimitive("wind_mph").getAsDouble();
        double windKph = currentJson.getAsJsonPrimitive("wind_kph").getAsDouble();
        int windDegree = currentJson.getAsJsonPrimitive("wind_degree").getAsInt();
        String windDirection = currentJson.getAsJsonPrimitive("wind_dir").getAsString();
        double pressureMb = currentJson.getAsJsonPrimitive("pressure_mb").getAsDouble();
        double pressureIn = currentJson.getAsJsonPrimitive("pressure_in").getAsDouble();
        double precip_mm = currentJson.getAsJsonPrimitive("precip_mm").getAsDouble();
        double precipIn = currentJson.getAsJsonPrimitive("precip_in").getAsDouble();
        int humidity = currentJson.getAsJsonPrimitive("humidity").getAsInt();
        int cloud = currentJson.getAsJsonPrimitive("cloud").getAsInt();
        double feelslikeC = currentJson.getAsJsonPrimitive("feelslike_c").getAsDouble();
        double feelslikeF = currentJson.getAsJsonPrimitive("feelslike_f").getAsDouble();
        double visKm = currentJson.getAsJsonPrimitive("vis_km").getAsDouble();
        double visMiles = currentJson.getAsJsonPrimitive("vis_miles").getAsDouble();
        double uv = currentJson.getAsJsonPrimitive("uv").getAsDouble();
        double gustMph = currentJson.getAsJsonPrimitive("gust_mph").getAsDouble();
        double gustKph = currentJson.getAsJsonPrimitive("gust_kph").getAsDouble();

        /*
         TODO:
          Above are listed all weather values that we can get from API.
          But we don't need all of that.
          Feel free to comment or remove the not needed ones.
         */

        // initialise WeatherData model
        WeatherData weatherData = new WeatherData();
        weatherData.setLocationName(cityName + ", " + country);
        weatherData.setTemp((int) tempC);
        weatherData.setConditionIconUrl("https:" + conditionIconUrl);

        return weatherData;
    }
}
