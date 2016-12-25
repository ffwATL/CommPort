package root.model;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.CommDataParser;
import root.util.DataParser;

import java.util.Arrays;

public class JsscCommModel extends CommAbstract {

    private static final Logger logger = LogManager.getLogger();
    private static SerialPort serialPort;
    private static JsscCommModel jsscCommModel;
    private static DataParser dataParser;
    private static boolean firstStart = true;

    static {
        dataParser = CommDataParser.getInstance();
        CommDataParser.getInstance();
    }

    private JsscCommModel(){}

    public static JsscCommModel getInstance(){
        return jsscCommModel == null ? jsscCommModel = new JsscCommModel() : jsscCommModel;
    }

    @Override
    public boolean connect(String portName, Integer baudRate) {
        serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.addEventListener(new PortReader());
            logger.info("Connected to " + portName);
            write("7");
            return true;
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void write(String b) throws SerialPortException {
        try {
            int [] arr = {255,0,234};
            /*byte [] arr = {Byte.valueOf(b)};
            String hex = DatatypeConverter.printHexBinary(arr);*/
            Integer val = Integer.valueOf(b);
            String hex = Integer.toHexString(val);
            logger.info("hex: "+ hex);
            serialPort.writeIntArray(arr);
            /*serialPort.writeByte(Byte.valueOf(hex));*/
        } catch (SerialPortException | NumberFormatException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }

    @Override
    public void write(int[] msg) throws SerialPortException {
        try {
            logger.info("Sending: "+ Arrays.toString(msg));
            serialPort.writeIntArray(msg);
            serialPort.sendBreak(50);
        }catch (SerialPortException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if(serialPort!= null && serialPort.isOpened()){
                serialPort.removeEventListener();
                serialPort.closePort();
            }
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
        }
        firstStart = !firstStart;
        serialPort = null;
    }

    static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte[] buffer;
            if(serialPortEvent.getEventValue() > 0){
                try {
                    StringBuilder builder = new StringBuilder();
                    buffer = serialPort.readBytes();
                    if(firstStart){
                        /*changeUI(dataParser.setMaxValues(buffer, buffer.length));*/
                        firstStart = !firstStart;
                    }else {
                        for (byte b : buffer){
                            builder.append(b & 0xFF);
                            builder.append(" ");
                        }
                        logger.info("response: "+ Arrays.toString(buffer));
                        builder.append("\n");
                        /*builder.append(dataParser.getStringPosition(buffer, buffer.length));*/
                        changeUI(builder.toString());
                    }
                } catch (SerialPortException e) {
                    logger.error("SerialPortException is occurred: " + e.getMessage());
                }
            }
        }
    }
}