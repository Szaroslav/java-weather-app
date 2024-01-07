package pl.edu.agh.to.weatherapp;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.edu.agh.to.weatherapp.gui.App;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        Application.launch(App.class);
    }
}
