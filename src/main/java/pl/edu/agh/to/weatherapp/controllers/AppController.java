package pl.edu.agh.to.weatherapp.controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import pl.edu.agh.to.weatherapp.App;

public class AppController {
    private final Stage primaryStage;
    private static final String APP_TITLE = "Potezna wichura";
    private static final String PRESENTER_RESOURCE = "/view/WeatherPresenter.fxml";

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @SneakyThrows
    public void initRootLayout() {
        primaryStage.setTitle(APP_TITLE);
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(PRESENTER_RESOURCE));
        loader.setControllerFactory(App.getApplicationContext()::getBean);
        GridPane rootLayout = loader.load();

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
