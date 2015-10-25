package root.view;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import root.model.CommModel;

import java.io.File;

public class SceneView extends Application {

    public static void main(String[] args){
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(new File("src/main/java/root/controller/scene.fxml").toURI().toURL());
        Scene scene = new Scene(root);
        Image ico = new Image("file:src/main/resources/icon.png");
        primaryStage.getIcons().add(ico);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                CommModel.getInstance().close();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Serial port connector");
        primaryStage.show();
    }
}
