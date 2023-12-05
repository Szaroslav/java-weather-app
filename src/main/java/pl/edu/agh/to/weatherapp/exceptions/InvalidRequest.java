package pl.edu.agh.to.weatherapp.exceptions;

public class InvalidRequest extends RuntimeException {
    public InvalidRequest(String errorMessage) {
        super(errorMessage);
    }
}
