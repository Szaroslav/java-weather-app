package pl.edu.agh.to.weatherapp.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import pl.edu.agh.to.weatherapp.App;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;

public class AppController {
    @FXML
    private TextField searchTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Label temperatureLabel;
    @FXML
    private Label locationLabel;
    @FXML
    private ImageView conditionIconImageView;
    private Stage primaryStage;

    public AppController(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @SneakyThrows
    public void initRootLayout() {
        primaryStage.setTitle("Potezna wichura");
        primaryStage.setMinWidth(400);
        primaryStage.setMinHeight(400);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
        loader.setControllerFactory(App.getApplicationContext()::getBean);
        AnchorPane rootLayout = loader.load();

        WeatherPresenter controller = loader.getController();
        controller.setAppController(this);

        Scene scene = new Scene(rootLayout);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
