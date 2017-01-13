package root.model;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.response.ResponseHandler;
import root.util.CommDataParser;
import root.util.DataParser;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JsscCommModel extends CommAbstract {

    private static final Logger logger = LogManager.getLogger();
    private static SerialPort serialPort;
    private static JsscCommModel jsscCommModel;
    private static DataParser dataParser;
    private static boolean firstStart = true;

    private boolean connected = false;
    private static final Queue<ResponseHandler> responseHandlers = new ConcurrentLinkedQueue<>();

    static {
        dataParser = CommDataParser.getInstance();
        CommDataParser.getInstance();
    }

    private JsscCommModel(){}

    public static synchronized JsscCommModel getInstance(){
        return jsscCommModel == null ? jsscCommModel = new JsscCommModel() : jsscCommModel;
    }

    @Override
    public boolean connect(String portName, Integer baudRate) {
        serialPort = new SerialPort(portName);

        try {
            serialPort.openPort();
            serialPort.setParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
            serialPort.addEventListener(new PortReader());
            this.connected = true;
            logger.info("Connected to " + portName);
            return true;
        } catch (SerialPortException e) {
            logger.error(e.getMessage());
            return false;
        }
    }

    @Override
    public void addResponseHandler(ResponseHandler responseHandler) {
        synchronized (responseHandlers){
            responseHandlers.add(responseHandler);
        }
    }

    @Override
    public void removeResponseHandlers() {
        /*this.handleResponse=null;*/
        responseHandlers.clear();
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }

    @Override
    public boolean sendBreak(int duration){
        try {
            return serialPort.sendBreak(duration);
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void write(int[] msg) throws SerialPortException {
        try {
            /*logger.info("Sending: " + Arrays.toString(msg));*/
            serialPort.writeIntArray(msg);
        }catch (SerialPortException e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public int getQueueSize() {
        return responseHandlers.size();
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
        this.connected = false;
        firstStart = !firstStart;
        serialPort = null;
    }

    private void handleResponse(int[] response){
        if(!responseHandlers.isEmpty()){
            responseHandlers.poll().handle(response);
        }
    }

    class PortReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte[] buffer;

            if(serialPortEvent.getEventValue() > 0){
                try {
                    buffer = serialPort.readBytes();
                    int[] response = new int[3];
                    int r_counter = 0;

                    for (byte aBuffer : buffer) {
                        int unsignedByte = aBuffer & 0xFF;

                        response[r_counter++] = unsignedByte;

                        if (r_counter == response.length) {
                            synchronized (responseHandlers){
                                handleResponse(response);
                            }
                            r_counter = 0;
                        }
                    }
                    logger.info("response: "+ Arrays.toString(buffer));
                   /* handleResponse(response);*/

                } catch (SerialPortException e) {
                    logger.error("SerialPortException is occurred: " + e.getMessage());
                }
            }
        }
    }
}