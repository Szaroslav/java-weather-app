package pl.edu.agh.to.weatherapp;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import pl.edu.agh.to.weatherapp.configuration.AppConfiguration;
import pl.edu.agh.to.weatherapp.controllers.AppController;

@Configuration
@Import(AppConfiguration.class)
public class App extends Application {
  private static ConfigurableApplicationContext applicationContext;

  public static ConfigurableApplicationContext getApplicationContext() {
    return applicationContext;
  }

  @Override
  public void init() {
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
