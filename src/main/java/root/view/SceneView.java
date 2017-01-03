package root.view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import root.model.JsscCommModel;

import java.io.File;

public class SceneView extends Application {

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(new File("src/main/java/root/controller/scene.fxml").toURI().toURL()); //for testing with IDEA
        Image ico = new Image("file:src/main/java/root/view/fxml/icon-5.png");  //for testing with IDEA
        /*AnchorPane root = (AnchorPane) FXMLLoader.load(SceneView.class.getResource("fxml/scene.fxml")); //for deployment
        Image ico = new Image(this.getClass().getResource("fxml/icon-5.png").toExternalForm()); //for deployment*/
        Scene scene = new Scene(root);
        primaryStage.getIcons().add(ico);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                /*RxTxCommModel.getInstance().close();*/
                JsscCommModel.getInstance().close();
            }
        });
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Serial Port Connector v.1.0.1");
        primaryStage.setMaximized(true);
        primaryStage.show();
    }
}