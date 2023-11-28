package pl.edu.agh.to.weatherapp.parser;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import pl.edu.agh.to.weatherapp.exceptions.InvalidRequest;
import pl.edu.agh.to.weatherapp.model.WeatherData;

import java.nio.charset.StandardCharsets;

public class JsonParser implements Parser {
    @Override
    public WeatherData parse(String content) {
        JsonObject json = new Gson().fromJson(new String(content.getBytes(), StandardCharsets.UTF_8), JsonObject.class);

        if (json.has("error")) {
            String errorMessage = json.getAsJsonObject("error")
                .getAsJsonPrimitive("message")
                .getAsString();
            throw new InvalidRequest(errorMessage);
        } else {
            return weatherDataFromJson(json);
        }
    }

    private  WeatherData weatherDataFromJson(JsonObject json) {
        // Parse location details
        JsonObject locationJson = json.getAsJsonObject("location");
        String cityName = locationJson.getAsJsonPrimitive("name").getAsString();
        String country = locationJson.getAsJsonPrimitive("country").getAsString();

        // Parse current weather details
        JsonObject currentJson = json.getAsJsonObject("current");
        double tempC = currentJson.getAsJsonPrimitive("temp_c").getAsDouble();

        // Parse current weather condition details
        JsonObject conditionJson = currentJson.getAsJsonObject("condition");
        String conditionIconUrl = conditionJson.getAsJsonPrimitive("icon").getAsString();

        // initialise WeatherData model
        WeatherData weatherData = new WeatherData();
        weatherData.setLocationName(cityName + ", " + country);
        weatherData.setTemp((int) tempC);
        weatherData.setConditionIconUrl("https:" + conditionIconUrl);

        return weatherData;
    }
}
