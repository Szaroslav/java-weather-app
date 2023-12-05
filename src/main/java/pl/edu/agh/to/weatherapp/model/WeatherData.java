package pl.edu.agh.to.weatherapp.model;

public class WeatherData {
    private int temp;
    private String locationName;
    private String conditionIconUrl;

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getConditionIconUrl() {
        return conditionIconUrl;
    }

    public void setConditionIconUrl(String conditionIconUrl) {
        this.conditionIconUrl = conditionIconUrl;
    }
}
