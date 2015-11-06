package root.view;


import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import root.model.RxTxCommModel;

import java.io.File;

public class SceneView extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(new File("src/main/java/root/controller/scene.fxml").toURI().toURL()); //for testing with IDEA
        Image ico = new Image("file:src/main/resources/icon.png");
        /*AnchorPane root = (AnchorPane) FXMLLoader.load(SceneView.class.getResource("fxml/scene.fxml")); //for deployment
        Image ico = new Image(this.getClass().getResource("fxml/icon.png").toExternalForm()); //for deployment*/
        Scene scene = new Scene(root);
        primaryStage.getIcons().add(ico);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                RxTxCommModel.getInstance().close();
            }
        });
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Serial port connector");
        primaryStage.show();
    }
}
