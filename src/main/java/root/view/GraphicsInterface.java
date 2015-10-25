package root.view;


import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.CommModel;
import root.util.DiscoveringComm;

public class GraphicsInterface extends Application implements Graphics {

    private Stage primaryStage;
    private static Logger logger = LogManager.getLogger();
    private String portName;
    private TextArea textArea;
    private static Comm commModel;
    private boolean connect;

    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        Image ico = new Image("file:src/main/resources/icon.png");
        primaryStage.getIcons().add(ico);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                closeConnection();
            }
        });
        primaryStage.setTitle("Comm port test");
        primaryStage.setWidth(500);
        primaryStage.setHeight(450);
        setWelcomeScene();
        primaryStage.show();
    }

    @Override
    public void init(){
        commModel = CommModel.getInstance();
        commModel.setGui(this);
    }

    private void clearText(){
        textArea.setText("");
    }

    public void updateTerminal(String s){
        logger.trace("appending: "+ s);
        textArea.appendText(s);
    }

    private void closeConnection(){
        if(connect){
            commModel.close();
            connect = false;
        }
    }

    private void scanPorts(Button connectButton, ComboBox choiceBox){
        ObservableList<String> portList = DiscoveringComm.getPortName();
        if(portList.size() > 0) {
            connectButton.setDisable(false);
            portName = portList.get(0);
        }
        else {
            connectButton.setDisable(true);
            portName = "no device";
            portList.add(portName);
        }
        choiceBox.setItems(portList);

    }

    private void setWelcomeScene(){
        Group root = new Group();
        Scene scene = new Scene(root,500,400);
        Button connectButton = new Button("Connect");
        connectButton.setLayoutX(365);
        connectButton.setLayoutY(350);
        connectButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (commModel.connect(portName, 9600)) {
                    connect = true;
                    setWorkScene();
                }
            }
        });
        connectButton.setTooltip(new Tooltip("Make a connect to the selected COM"));
        Button scanButton = new Button("Scan");
        ComboBox<String> choiceBox = new ComboBox<>();
        scanButton.setTooltip(new Tooltip("Scan for available ports"));
        scanButton.setLayoutX(40);
        scanButton.setLayoutY(100);
        scanButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                scanPorts(connectButton, choiceBox);
            }
        });
        scanPorts(connectButton, choiceBox);
        choiceBox.setLayoutX(40);
        choiceBox.setLayoutY(50);
        choiceBox.setTooltip(new Tooltip("Choose COM to connect"));
        choiceBox.getSelectionModel().selectFirst();
        choiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                portName = newValue;
            }
        });
        root.getChildren().add(connectButton);
        root.getChildren().add(scanButton);
        root.getChildren().add(choiceBox);
        primaryStage.setScene(scene);
    }

    private void setWorkScene(){
        Group root = new Group();
        Scene scene = new Scene(root, 500, 400);
        textArea = new TextArea();
        textArea.setLayoutX(140);
        textArea.setLayoutY(100);
        textArea.setPrefSize(200, 170);
        textArea.setEditable(false);
        Button backButton = new Button("Back");
        backButton.setLayoutX(385);
        backButton.setLayoutY(350);
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                textArea = null;
                closeConnection();
                setWelcomeScene();
            }
        });
        Button clearButton = new Button("Clear");
        clearButton.setLayoutX(315);
        clearButton.setLayoutY(350);
        clearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                clearText();
            }
        });
        TextField textField = new TextField();
        textField.setPrefSize(200, 5);
        textField.setLayoutX(140);
        textField.setLayoutY(290);
        textField.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                commModel.write(textField.getText());
                textField.setText("");
            }
        });
        root.getChildren().add(textArea);
        root.getChildren().add(backButton);
        root.getChildren().add(textField);
        root.getChildren().add(clearButton);
        primaryStage.setScene(scene);
    }
}
