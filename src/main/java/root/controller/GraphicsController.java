package root.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.Command;
import root.model.JsscCommModel;
import root.util.CommUtilAbstract;
import root.util.JsscCommUtil;

import java.net.URL;
import java.util.ResourceBundle;


public class GraphicsController implements Initializable, Graphics {

    private static final Logger logger = LogManager.getLogger();

    private static final Comm commModel = JsscCommModel.getInstance();
    private static CommUtilAbstract commUtil = JsscCommUtil.getInstance();

    private static boolean writeFile;
    private static String portName = "no device";
    private static final Logger writeFileLogger = LogManager.getLogger("fileLogger");

    private boolean connect;

    @FXML
    private ComboBox<String> commPortBox;
    @FXML
    private ComboBox<Integer> baudRateBox;

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
    private Pane rightPane;
    @FXML
    private Button resetButton;


    private static boolean firstStart = true;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        commModel.setGui(this);
        scanPorts();
        comboBoxInitialization();
        initTooltips();
    }

    @Override
    public void updateTerminal(String s){
        /*if(firstStart) setLabels(s);*/
       /* else {

            if(writeFile) writeLog(s.replace("\n","   "));
        }*/
        textArea.appendText(s);
        /*logger.trace("appended.. " +s);*/
    }

    @FXML
    private void setWriteFile(){
        writeFile = !writeFile;
    }

    private void writeLog(String log){
        writeFileLogger.info(log);
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
        }else{
            changeLeftPaneState(false);
            firstStart = !firstStart;
        }
    }

    public void inputTextFieldAction(ActionEvent event) throws SerialPortException {
       /* commModel.write(inputTextField.getText());*/
        /*inputTextField.setText("");*/
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
        baudRateBox.setItems(CommUtilAbstract.getBaudRateList());
        baudRateBox.getSelectionModel().select(3);
    }

    private void changeLeftPaneState(boolean state){

        baudRateBox.setDisable(state);
        commPortBox.setDisable(state);
        scanButton.setDisable(state);

        if(state) {
            connectButton.setTooltip(new Tooltip("Close a connection"));
            connectButton.setText("Disconnect");
            connectStatusLabel.setText("Connected");
            indicatorCircle.setFill(Color.web("#1abc38"));
            connectStatusLabel.setTextFill(Color.web("#1abc38"));
        }
        else {
            connectButton.setTooltip(new Tooltip("Make a connection to selected COM"));
            commModel.stopExecutingCommand();
            commModel.close();
            connectButton.setText("Connect");
            indicatorCircle.setFill(Color.web("#d30031"));
            connectStatusLabel.setText("Disconnected");
            connectStatusLabel.setTextFill(Color.web("#d30031"));
        }
        connect = state;
    }

    @FXML
    private void resetButtonAction() throws SerialPortException {
        commModel.write(Command.RESET_COMMAND_FIRST);
        commModel.write(Command.RESET_COMMAND_SECOND);
    }
    @FXML
     private void startFreqButtonAction() throws SerialPortException{
        commModel.write(Command.START_FREQ);
    }
    @FXML
    private void stopFreqButtonAction() throws SerialPortException{
        commModel.write(Command.STOP_FREQ);
    }

    @FXML
    private void validateTextInput(KeyEvent event){
        Integer l;
        /*if(event.getSource() == timerTextField){
            try{
                l = new Integer(timerTextField.getText());
            }catch (NumberFormatException e){
                logger.trace("trying to handle exception.. " + e.getMessage());
                timerTextField.deletePreviousChar();
            }
        }else {
            try{
                l = new Integer(inputTextField.getText());
            }catch (NumberFormatException e){
                logger.trace("trying to handle exception.. " + e.getMessage());
               inputTextField.deletePreviousChar();
            }
        }*/
    }

    private void initTooltips(){
        baudRateBox.setTooltip(new Tooltip("Baud rate for serial communication"));
        commPortBox.setTooltip(new Tooltip("COM port to connect"));
        scanButton.setTooltip(new Tooltip("Scan for available serial port"));
        clearButton.setTooltip(new Tooltip("Clear console"));
        connectButton.setTooltip(new Tooltip("Make a connection to selected COM"));
    }
}