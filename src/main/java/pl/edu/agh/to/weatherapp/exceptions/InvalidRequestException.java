package pl.edu.agh.to.weatherapp.exceptions;

public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String errorMessage) {
        super(errorMessage);
    }
}
