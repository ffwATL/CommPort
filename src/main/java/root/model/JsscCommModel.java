package root.model;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.CommDataParser;
import root.util.DataParser;

import javax.xml.bind.DatatypeConverter;

public class JsscCommModel extends CommAbstract {

    private static final Logger logger = LogManager.getLogger();
    private static SerialPort serialPort;
    private static JsscCommModel jsscCommModel;
    private static DataParser dataParser;

    static {
        DatatypeConverter.printInt(0);
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
            return true;
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
        }
        return false;
    }

    @Override
    public void write(String b) {
        /*logger.trace("starting conversion..");*/
        byte [] arr = {Byte.valueOf(b)};
        String hex = DatatypeConverter.printHexBinary(arr);
        try {
            logger.trace("writing " +hex);
            serialPort.writeByte(Byte.valueOf(hex));
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
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
        serialPort = null;
    }

    static class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte[] buffer;
            if(serialPortEvent.getEventValue() > 0){
                try {
                    buffer = serialPort.readBytes();
                    StringBuilder builder = new StringBuilder();
                    /*builder.append("raw: ");
                    for (byte b : buffer){
                        builder.append(b & 0xFF);
                        builder.append(" ");
                    }
                    builder.append("\n");*/
                    builder.append(dataParser.getStringPosition(buffer, buffer.length));
                    changeUI(builder.toString());
                } catch (SerialPortException e) {
                    logger.error("SerialPortException is occurred: " + e.getMessage());
                }
            }
        }
    }
}
