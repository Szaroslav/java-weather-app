package pl.edu.agh.to.weatherapp.exceptions;

public class DatabaseFailureException extends RuntimeException {
    public DatabaseFailureException(String errorMessage) {
        super(errorMessage);
    }
}
