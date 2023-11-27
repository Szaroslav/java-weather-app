package pl.edu.agh.to.weatherapp.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.testfx.api.FxRobot;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import pl.edu.agh.to.weatherapp.gui.MockClasses.WeatherServiceMock;
import pl.edu.agh.to.weatherapp.gui.MockClasses.WeatherServiceMockDelay;
import pl.edu.agh.to.weatherapp.presenters.WeatherPresenter;

import java.io.IOException;

@ExtendWith(ApplicationExtension.class)
public class GuiTestServiceDelay {
//        private ConfigurableApplicationContext context;
        private Button button;
        private WeatherPresenter controller;

        @Start
        private void start(Stage stage) throws IOException {
//
//                AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
//                ctx.register(GuiTestConfiguration.class); // Replace YourConfigClass with your Spring configuration class
//                ctx.refresh();
//
//                context = ctx;

                stage.setTitle("Potezna wichura");
                stage.setMinWidth(400);
                stage.setMinHeight(400);

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/view/WeatherPresenter.fxml"));
                loader.setControllerFactory(c -> {
                        return new WeatherPresenter(new WeatherServiceMockDelay());
                });
                AnchorPane rootLayout = loader.load();
                //Added for testing
                button = new Button("click me!");
                button.setId("myButton");
                button.setOnAction(actionEvent -> button.setText("clicked!"));
                rootLayout .getChildren().add(button);

                controller = loader.getController();
                Scene scene = new Scene(rootLayout);
                stage.setScene(scene);
                stage.show();
        }

        @Test
        void service_non_blocking(FxRobot robot) throws InterruptedException {
                robot.clickOn("#searchButton");
                robot.clickOn("#myButton");
                Assertions.assertThat(robot.lookup("#locationLabel").queryAs(Label.class)).hasText("");
                Assertions.assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class)).hasText("");
                Assertions.assertThat(robot.lookup("#myButton").queryAs(Button.class)).hasText("clicked!");
                robot.wait(1000);
                Assertions.assertThat(robot.lookup("#locationLabel").queryAs(Label.class)).hasText("Tarnów");
                Assertions.assertThat(robot.lookup("#temperatureLabel").queryAs(Label.class)).hasText("42°C");
        }


}
