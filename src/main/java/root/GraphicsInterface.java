package root;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GraphicsInterface extends Application {

    private Stage primaryStage;
    private static Logger logger = LogManager.getLogger();
    private String portName;
    private TextArea textArea;
    private static CommTest commTest;


    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void init(){
        commTest = new CommTest();
        commTest.setGui(this);
    }

    private void setWelcomeScene(){
        Group root = new Group();
        Scene scene = new Scene(root,400,300);
        Button connectButton = new Button("Connect");
        connectButton.setLayoutX(300);
        connectButton.setLayoutY(280);
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setWorkScene();
            }
        });
        ObservableList<String> portList = DiscoveringComm.getPortName();
        if(portList.size()>0) portName = portList.get(0);
        else {
            connectButton.setDisable(true);
            portName = "no device";
            portList.add(portName);
        }
        ChoiceBox<String> choiceBox = new ChoiceBox<>(portList);
        choiceBox.setLayoutX(20);
        choiceBox.setLayoutY(20);
        choiceBox.setTooltip(new Tooltip("Choose COM to connect"));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                portName = newValue;
            }
        });
        root.getChildren().add(connectButton);
        root.getChildren().add(choiceBox);
        primaryStage.setScene(scene);

    }
    private void setWorkScene(){
        Group root = new Group();
        Scene scene = new Scene(root,400,300);
        textArea = new TextArea();
        textArea.setLayoutX(90);
        textArea.setLayoutY(50);
        textArea.setPrefSize(200, 100);
        Button backButton = new Button("Back");
        backButton.setLayoutX(300);
        backButton.setLayoutY(280);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea = null;
                setWelcomeScene();
            }
        });
        TextField textField = new TextField();
        textField.setPrefSize(200,5);
        textField.setLayoutX(90);
        textField.setLayoutY(280);
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea.appendText(textField.getText()+"\n");
                textField.setText("");
            }
        });
        root.getChildren().add(textArea);
        root.getChildren().add(backButton);
        root.getChildren().add(textField);
        primaryStage.setScene(scene);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Comm port test");
        primaryStage.setWidth(400);
        primaryStage.setHeight(350);
        setWelcomeScene();
        primaryStage.show();
    }





}
