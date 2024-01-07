package pl.edu.agh.to.weatherapp.gui;

import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javafx.application.Application;
import javafx.stage.Stage;

import lombok.Getter;

import pl.edu.agh.to.weatherapp.configuration.AppConfiguration;
import pl.edu.agh.to.weatherapp.gui.controllers.AppController;


@Configuration
@Import(AppConfiguration.class)
public class App extends Application {
    @Getter
    private static ConfigurableApplicationContext applicationContext;

    @Override
    public synchronized void init() {
        applicationContext = SpringApplication.run(App.class);
    }

    @Override
    public void stop() {
        applicationContext.close();
    }

    @Override
    public void start(Stage primaryStage) {
        new AppController(primaryStage).initRootLayout();
    }
}
