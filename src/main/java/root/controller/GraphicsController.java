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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.JsscCommModel;
import root.util.CommUtilAbstract;
import root.util.JsscCommUtil;

import java.net.URL;
import java.util.ResourceBundle;


public class GraphicsController implements Initializable, Graphics {

    private static final Logger logger = LogManager.getLogger();
    private static final String FIRST = "1";
    private static final String SECOND = "2";
    private static final String THIRD = "3";
    private static final String DEFAULT = FIRST;
    private static final long DEFAULT_DELAY = 100;

    private static final Comm commModel = JsscCommModel.getInstance();;
    private static CommUtilAbstract commUtil = JsscCommUtil.getInstance();
    private static long delay = DEFAULT_DELAY;
    private static String command = DEFAULT;
    private static boolean writeFile;
    private static String portName = "no device";
    private static final Logger writeFileLogger = LogManager.getLogger("fileLogger");

    private boolean connect;

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
    @FXML
    private ToggleButton writeLogButton;
    @FXML
    private Pane leftPane;
    @FXML
    private Pane rightPane;
    @FXML
    private Label maxLabelH;
    @FXML
    private Label maxLabelA;
    @FXML
    private Button enterButton;

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
        if(firstStart) setLabels(s);
        else {
            textArea.setText(s);
            if(writeFile) writeLog(s.replace("\n","   "));
        }
        /*logger.trace("appended.. " +s);*/
    }

    private void setLabels(String s){
        String[] parsed = s.split("\n");
        if(parsed.length > 0) maxLabelA.setText(parsed[0]);
        if(parsed.length > 1) maxLabelH.setText(parsed[1]);
        firstStart = !firstStart;
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

    public void inputTextFieldAction(ActionEvent event){
        commModel.write(inputTextField.getText());
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
        timeConfigBox.setItems(commUtil.getTimeConfigList());
        timeConfigBox.getSelectionModel().selectFirst();
        baudRateBox.setItems(commUtil.getBaudRateList());
        baudRateBox.getSelectionModel().select(3);
    }

    private void changeLeftPaneState(boolean state){
        inputTextField.setDisable(!state);
        enterButton.setDisable(!state);
        baudRateBox.setDisable(state);
        commPortBox.setDisable(state);
        scanButton.setDisable(state);
        changeRightPaneState(!state);
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

    private void changeRightPaneState(boolean state){
        rightPane.setDisable(state);
        if(firstCommandButton.isSelected()) firstCommandButton.setSelected(false);
        if (secondCommandButton.isSelected()) secondCommandButton.setSelected(false);
        if (thirdCommandButton.isSelected()) thirdCommandButton.setSelected(false);
        if (setTimerButton.isSelected()) setTimerButton.setSelected(false);
        if (startButton.isSelected()) startButton.setSelected(false);
        if (writeLogButton.isSelected()) writeLogButton.setSelected(false);
        if(state){
            timerTextField.setDisable(!state);
            timeConfigBox.setDisable(!state);
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
            commModel.executeCommand(command, delay, delay, timeConfigBox.getValue()); // call executor.start();
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
        Integer l;
        if(event.getSource() == timerTextField){
            try{
                l = new Integer(timerTextField.getText());
            }catch (NumberFormatException e){
                logger.trace("trying to handle exception.. ");
                timerTextField.deletePreviousChar();
            }
        }else {
            try{
                l = new Integer(inputTextField.getText());
            }catch (NumberFormatException e){
                logger.trace("trying to handle exception.. ");
               inputTextField.deletePreviousChar();
            }
        }
    }

    @FXML
    private void toggleCommandButtonHandler(ActionEvent event){
        if(event.getSource() == firstCommandButton){
            secondCommandButton.setSelected(false);
            thirdCommandButton.setSelected(false);
        }else if(event.getSource() == secondCommandButton){
            firstCommandButton.setSelected(false);
            thirdCommandButton.setSelected(false);
        }else if(event.getSource() == thirdCommandButton){
            firstCommandButton.setSelected(false);
            secondCommandButton.setSelected(false);
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
