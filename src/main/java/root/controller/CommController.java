package root.controller;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
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

    private static final Logger logger = LogManager.getLogger();
    private static final String FIRST = "1";
    private static final String SECOND = "2";
    private static final String THIRD = "3";
    private static final String DEFAULT = FIRST;
    private static final long DEFAULT_DELAY = 100;

    private static Comm commModel;
    private static CommUtilAbstract commUtil = CommUtil.getInstance();
    private static long delay = DEFAULT_DELAY;
    private static String command = DEFAULT;

    private boolean connect;
    private String portName = "no device";

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
    private ToggleButton startButton;


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
        logger.trace("appending.. ");
        textArea.appendText(s);
        logger.trace("finished appending ");
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
        startButton.setDisable(true);
        setTimerButton.setSelected(state);
        firstCommandButton.setSelected(state);
        secondCommandButton.setSelected(state);
        thirdCommandButton.setSelected(state);
        if(state){
            firstCommandButton.setTextFill(Color.web("#860000"));
            secondCommandButton.setTextFill(Color.web("#860000"));
            thirdCommandButton.setTextFill(Color.web("#860000"));
        }
    }

    private void scanCommand(){
        if(firstCommandButton.isSelected()) command = FIRST;
        else if(secondCommandButton.isSelected()) command = SECOND;
        else if(thirdCommandButton.isSelected()) command = THIRD;
        else {
            firstCommandButton.setSelected(true);
            command = DEFAULT;
        }
    }

    @FXML
    private void startButtonHandler(ActionEvent event){
        if(!startButton.isSelected()){
            commModel.stopExecutingCommand();
            startButton.setText("Start");
        }else{
            scanCommand();
            commModel.executeCommand(command, delay, delay, timeConfigBox.getValue());
            // call executor.start();
            startButton.setText("Stop");
        }
    }
    @FXML
    private void setTimerHandler(ActionEvent event){
        String delayString = timerTextField.getText();
        if(setTimerButton.isSelected()){
            startButton.setDisable(false);
            timeConfigBox.setDisable(true);
            timerTextField.setDisable(true);
            if(delayString.length() > 0) delay = new Long(delayString);
            else {
                delay = DEFAULT_DELAY;
                timerTextField.setText(String.valueOf(DEFAULT_DELAY));
            }
        }else{
            timerTextField.setDisable(false);
            startButton.setDisable(true);
            timeConfigBox.setDisable(false);
        }
    }

    @FXML
    private void validateTextInput(KeyEvent event){
        try{
            Long l = new Long(timerTextField.getText());
        }catch (NumberFormatException e){
            logger.trace("trying to handle exception.. ");
            timerTextField.deletePreviousChar();
        }catch (Exception e){
            logger.trace("something goes wrong: " + e.getMessage());
        }
    }

    @FXML
    private void toggleCommandButtonHandler(ActionEvent event){
        if(event.getSource() == firstCommandButton){
            secondCommandButton.setSelected(false);
            thirdCommandButton.setSelected(false);
            firstCommandButton.textFillProperty().setValue(Color.GREEN);
            secondCommandButton.textFillProperty().setValue(Color.web("#860000"));
            thirdCommandButton.textFillProperty().setValue(Color.web("#860000"));
        }else if(event.getSource() == secondCommandButton){
            firstCommandButton.setSelected(false);
            thirdCommandButton.setSelected(false);
            secondCommandButton.textFillProperty().setValue(Color.GREEN);
            firstCommandButton.textFillProperty().setValue(Color.web("#860000"));
            thirdCommandButton.textFillProperty().setValue(Color.web("#860000"));
        }else if(event.getSource() == thirdCommandButton){
            firstCommandButton.setSelected(false);
            secondCommandButton.setSelected(false);
            thirdCommandButton.textFillProperty().setValue(Color.GREEN);
            secondCommandButton.textFillProperty().setValue(Color.web("#860000"));
            firstCommandButton.textFillProperty().setValue(Color.web("#860000"));
        }
    }

    private void initTooltips(){
        baudRateBox.setTooltip(new Tooltip("Baud rate for serial communication"));
        commPortBox.setTooltip(new Tooltip("COM port to connect"));
        scanButton.setTooltip(new Tooltip("Scan for available serial port"));
        clearButton.setTooltip(new Tooltip("Clear console"));
        connectButton.setTooltip(new Tooltip("Make a connection to selected COM"));
    }



}
