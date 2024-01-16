package pl.edu.agh.to.weatherapp.exceptions;

public class DatabaseFailureException extends RuntimeException {
    public DatabaseFailureException(String errorMessage) {
        super(errorMessage);
    }
    public DatabaseFailureException(Exception exception) {
        super(exception);
    }
    public DatabaseFailureException(String errorMessage, Exception exception) {
        super(errorMessage, exception);
    }
}
