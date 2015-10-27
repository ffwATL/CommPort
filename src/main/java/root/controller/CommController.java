package root.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.CommModel;
import root.util.CommUtil;
import root.util.CommUtilAbstract;
import root.view.Graphics;

import java.net.URL;
import java.util.ResourceBundle;


public class CommController implements Initializable, Graphics {

    private String portName = "no device";
    private static Comm commModel;
    private boolean connect;
    private static final Logger logger = LogManager.getLogger();
    private static CommUtilAbstract commUtil = CommUtil.getInstance();

    @FXML
    private ComboBox<String> commPortBox;
    @FXML
    private ComboBox<Integer> baudRateBox;
    @FXML
    private TextField inputTextField;
    @FXML
    private Button scanButton;
    @FXML
    private Button connectButton;
    @FXML
    private TextArea textArea;
    @FXML
    private Circle indicatorCircle;
    @FXML
    private Button clearButton;
    @FXML
    private Label connectStatusLabel;
    @FXML
    private ComboBox<String> timeConfigBox;
    @FXML
    private ToggleButton setTimerButton;
    @FXML
    private TextField timerTextField;
    @FXML
    private ToggleButton  firstCommandButton;
    @FXML
    private ToggleButton  secondCommandButton;
    @FXML
    private ToggleButton  thirdCommandButton;
    @FXML
    private Button startButton;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commModel = CommModel.getInstance();
        commModel.setGui(this);
        scanPorts();
        comboBoxInitialization();
        initTooltips();
    }


    @Override
    public void updateTerminal(String s){
        logger.trace("appending: " + s);
        textArea.appendText(s);
    }

    private void comboBoxInitialization(){
        commPortBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                portName = s2;
            }
        });
        timeConfigBox.setItems(commUtil.getTimeConfigList());
        timeConfigBox.getSelectionModel().selectFirst();
        baudRateBox.setItems(commUtil.getBaudRateList());
        baudRateBox.getSelectionModel().select(3);
    }

    public void scanPorts(){
        ObservableList<String> portList = commUtil.getPortName();
        if(portList.size() > 0) {
            connectButton.setDisable(false);
            baudRateBox.setDisable(false);
            portName = portList.get(0);
        }
        else {
            connectButton.setDisable(true);
            baudRateBox.setDisable(true);
            portList.add(portName);
        }
        commPortBox.setItems(portList);
        commPortBox.getSelectionModel().selectFirst();
    }

    public void connect(ActionEvent event){
        if (!connect && commModel.connect(portName, baudRateBox.getValue())) {
            changeLeftPaneState(true);
        }else changeLeftPaneState(false);
    }

    public void inputTextFieldAction(ActionEvent event){
        commModel.write(inputTextField.getText());
        inputTextField.setText("");
    }

    public void clear(ActionEvent event){
        textArea.setText("");
    }

    private void changeLeftPaneState(boolean state){
        inputTextField.setDisable(!state);
        commPortBox.setDisable(state);
        baudRateBox.setDisable(state);
        scanButton.setDisable(state);
        changeRightPaneState(!state);
        if(state) {
            connectButton.setTooltip(new Tooltip("Close a connection"));
            connectButton.setText("Disconnect");
            connectStatusLabel.setText("Connected");
            indicatorCircle.setFill(Color.GREEN);
            connectStatusLabel.setTextFill(Color.GREEN);
        }
        else {
            connectButton.setTooltip(new Tooltip("Make a connection to selected COM"));
            commModel.close();
            connectButton.setText("Connect");
            indicatorCircle.setFill(Color.RED);
            connectStatusLabel.setText("Disconnected");
            connectStatusLabel.setTextFill(Color.RED);
        }
        connect = state;
    }

    private void changeRightPaneState(boolean state){
        setTimerButton.setDisable(state);
        secondCommandButton.setDisable(state);
        firstCommandButton.setDisable(state);
        thirdCommandButton.setDisable(state);
        timeConfigBox.setDisable(state);
        timerTextField.setDisable(state);
    }

    private void initTooltips(){
        baudRateBox.setTooltip(new Tooltip("Baud rate for serial communication"));
        commPortBox.setTooltip(new Tooltip("COM port to connect"));
        scanButton.setTooltip(new Tooltip("Scan for available serial port"));
        clearButton.setTooltip(new Tooltip("Clear console"));
        connectButton.setTooltip(new Tooltip("Make a connection to selected COM"));
    }



}
