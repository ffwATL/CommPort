package root.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.Command;
import root.model.JsscCommModel;
import root.response.ReadCounterResponseHandler;
import root.util.CommDataParser;
import root.util.CommUtilAbstract;
import root.util.DataParser;
import root.util.JsscCommUtil;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;


public class GraphicsController implements Initializable, Graphics {

    private static final Logger logger = LogManager.getLogger();

    private static final Comm<Graphics> commModel = JsscCommModel.getInstance();
    private static CommUtilAbstract commUtil = JsscCommUtil.getInstance();
    private static final DataParser dataParser =CommDataParser.getInstance();

    private static boolean writeFile;
    private static String portName = "no device";
    private static final Logger writeFileLogger = LogManager.getLogger("fileLogger");

    @FXML
    private ComboBox<String> commPortBox;
    @FXML
    private ComboBox<Integer> baudRateBox;
    @FXML
    private ToggleButton downButton;
    @FXML
    private ToggleButton upButton;
    @FXML
    private ToggleButton stopFreqButton;
    @FXML
    private ToggleButton startFreqButton;
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
    private Pane centralPane;
    @FXML
    private TextField impulseCountInput;
    @FXML
    private TextField freqInput;
    @FXML
    private Button countWithSettingsButton;
    @FXML
    private ToggleButton setImpulseButton;
    @FXML
    private ToggleButton setFreqButton;

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
        textArea.setText(s);
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

    /**
     * Method handles "connect" & "disconnect" buttons event.
     */
    public void connect(ActionEvent event)/* throws IOException*/ {

        if (!commModel.isConnected() && commModel.connect(portName, baudRateBox.getValue())) {
            changeLeftPaneState(true);
            centralPane.setDisable(false);
        }else{
            changeLeftPaneState(false);
            centralPane.setDisable(true);
            commModel.close();
            firstStart = !firstStart;
        }
    }

    /**
     * Method handles "CS" button event. It clears output #textArea console.
     */
    public void clear(ActionEvent event){
        textArea.setText("");
    }

    /**
     * Make comboBox initialization.
     */
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

    /**
     * Handles left pane state that depends on @param state.
     * @param state
     */
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
            connectButton.setText("Connect");
            indicatorCircle.setFill(Color.web("#d30031"));
            connectStatusLabel.setText("Disconnected");
            connectStatusLabel.setTextFill(Color.web("#d30031"));
        }
    }

    /**
     * Handle "resetButton" event by sending "reset" command to the device.
     * @throws SerialPortException
     */
    @FXML
    private void resetButtonAction() throws SerialPortException {
        commModel.write(Command.RESET_COMMAND_FIRST);
        commModel.sendBreak(50);
        commModel.write(Command.RESET_COMMAND_SECOND);
    }

    /**
     *
     * @param event
     * @throws SerialPortException
     */
    @FXML
     private void startFreqButtonAction(ActionEvent event) throws SerialPortException{
        if(startFreqButton.isSelected()){
            handleUpDownButtons(startFreqButton, stopFreqButton);
            commModel.write(Command.START_FREQ);
        }

    }
    @FXML
    private void stopFreqButtonAction(ActionEvent event) throws SerialPortException{
        if(stopFreqButton.isSelected()){
            handleUpDownButtons(stopFreqButton, startFreqButton);
            commModel.write(Command.STOP_FREQ);
        }
    }

    @FXML
    private void downButtonAction(ActionEvent event) throws SerialPortException {
        if(downButton.isSelected()){
            handleUpDownButtons(downButton, upButton);
            sendImpulseSettings(Command.DOWN_COMMAND);
        }
    }

    @FXML
    private void upButtonAction(ActionEvent event) throws SerialPortException {
        if(upButton.isSelected()){
            handleUpDownButtons(upButton, downButton);
            sendImpulseSettings(Command.UP_COMMAND);
        }
    }

    @FXML
    private void setImpulseCountAction(ActionEvent event) throws SerialPortException {
        ToggleButton source = (ToggleButton) event.getSource();
        boolean isSelected = source.isSelected();

        disableInputTextField(isSelected, impulseCountInput);
        disableCountWithSettingsButton();

        if(isSelected){
            sendImpulseCount();
        }
    }

    @FXML
    private void setFrequencyAction(ActionEvent event) throws SerialPortException {
        ToggleButton source = (ToggleButton) event.getSource();
        boolean isSelected = source.isSelected();

        disableInputTextField(isSelected, freqInput);
        disableCountWithSettingsButton();

        if(isSelected){
            sendFrequency();
        }
    }
    @FXML
    private void getCounterWithPreviousSettings(){
        sendImpulseCount();
        commModel.sendBreak(20);
        sendFrequency();
        commModel.sendBreak(20);
        getImpulseCount();
    }

    private void disableCountWithSettingsButton(){
        countWithSettingsButton.setDisable(!(setFreqButton.isSelected() && setImpulseButton.isSelected()));
    }

    private void sendImpulseCount(){
        long impulseCount = Long.valueOf(impulseCountInput.getText());
        int[] message = dataParser.convertLongToUnsignedIntegerArrayImpulse(impulseCount, Command.SET_IMPULSE_COUNT);
        int tmp = message[1];

        message[1] = message[3];
        message[3] = tmp;
        tmp = message[3];
        message[3] = message[2];
        message[2] = tmp;

        try {
            commModel.write(message);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private void sendFrequency(){
        int freq = Integer.parseInt(freqInput.getText());
        int[] message = dataParser.calculateFreq(freq);

        try {
            commModel.write(message);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }

    private void disableInputTextField(boolean disable, TextField textField){
        textField.setDisable(disable);
    }

    private void handleUpDownButtons(ToggleButton pressed, ToggleButton released){
        released.setSelected(!pressed.isSelected());
    }

    private void sendImpulseSettings(int[] command) throws SerialPortException {
        System.err.println("Input field: " + impulseCountInput.getText() + ", command: " + Arrays.toString(command));
        commModel.write(command);
    }

    @FXML
    private void getImpulseCount() {
        try{
            commModel.addResponseHandler(new ReadCounterResponseHandler(this));
            commModel.write(Command.GET_COUNTER);
        }catch (SerialPortException e){
            e.printStackTrace();
            commModel.removeResponseHandlers();
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