package root;


import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class GraphicsInterface extends Application {

    private Button button;
    private BorderPane rootLayout;
    private Stage primaryStage;
    private static Logger logger = LogManager.getLogger();

    Text sceneTitle = new Text("Welcome");


    public static void main(String[] args){
        launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Comm port test");
        /*button = new Button("click me");
        button.setOnAction(new ClickMeEventHandler());
        StackPane layout = new StackPane();
        layout.getChildren().add(button);

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        gridPane.add(sceneTitle,0,0);
        gridPane.add(button,1,0);
        Scene scene = new Scene(gridPane,300,250);
        primaryStage.setScene(scene);
        primaryStage.show();*/
        initRootLayout();
        showPersonOverview();

    }
    public void initRootLayout(){
        try{
            FXMLLoader loader = new FXMLLoader();
            File file = new File("src/main/resources/fx/rootLayout.fxml");
            loader.setLocation(file.toURI().toURL());
            rootLayout = (BorderPane) loader.load();
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            logger.error("IOException is occurred: " + e.getMessage());
        }
    }

    public void showPersonOverview(){
        try{
            FXMLLoader loader = new FXMLLoader();
            File file = new File("src/main/resources/fx/gui.fxml");
            loader.setLocation(file.toURI().toURL());
            AnchorPane personOwerview = (AnchorPane) loader.load();
            rootLayout.setCenter(personOwerview);
        } catch (IOException e) {
            logger.error("IOException is occurred: " + e.getMessage());
        }
    }

    class ClickMeEventHandler implements EventHandler<ActionEvent>{

        @Override
        public void handle(ActionEvent event) {
            if(event.getSource() == button){
                sceneTitle.setText("hello");
            }
        }
    }
}
