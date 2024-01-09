package pl.edu.agh.to.weatherapp.exceptions;

public class DatabaseFailure extends RuntimeException {
    public DatabaseFailure(String errorMessage) {
        super(errorMessage);
    }
}
