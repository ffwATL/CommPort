package root.model;

import gnu.io.*;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.controller.Graphics;
import root.response.ResponseHandler;
import root.util.CommDataParser;
import root.util.DataParser;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class RxTxCommModel extends CommAbstract {

    private static final Logger logger = LogManager.getLogger();
    private static Comm<Graphics> comm;
    private static OutputStream outputStream;
    private SerialPort serialPort;
    private static BufferedInputStream inputStream;
    private static DataParser dataParser;

    static {
        DatatypeConverter.printInt(0);
        dataParser = CommDataParser.getInstance();
    }

    private RxTxCommModel(){}

    public static Comm getInstance(){
        return comm == null ? comm = new RxTxCommModel() : comm;
    }

    @Override
    public boolean connect(String portName, Integer baudRate){
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if(commPort instanceof SerialPort){
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                if(inputStream == null) inputStream = new BufferedInputStream(serialPort.getInputStream());
                outputStream = serialPort.getOutputStream();
                SerialReader serialReader = new SerialReader();
                serialPort.addEventListener(serialReader);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setInputBufferSize(3);
                serialPort.setOutputBufferSize(6);
                logger.info("Connected to " + portName);
                return true;
            }
        }catch (NoSuchPortException e){
            logger.error("No such port. Maybe there's wrong port name " + e.getMessage());
        }catch (PortInUseException e){
            logger.error("Port "+ portName + "is currently in use. "+e.getMessage());
        }catch (UnsupportedCommOperationException e){
            logger.error("Unsupported Comm Operation. Please check configuration. "+e.getMessage());
        }catch (IOException e){
            logger.error("IOException is occurred: " + e.getMessage());
        }catch (TooManyListenersException e){
            logger.error("Too many listeners is set: " + e.getMessage());
        }
        return  false;
    }

    @Override
    public void addResponseHandler(ResponseHandler responseHandler) {

    }

    @Override
    public void removeResponseHandlers() {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    /*@Override
    public void write(String  b){
        logger.trace("starting conversion..");
        byte [] arr = {Byte.valueOf(b)};
        String hex = DatatypeConverter.printHexBinary(arr);
        try {
           logger.trace("writing: " + hex);
           outputStream.write(Byte.valueOf(hex));
        }catch (IOException e){
            logger.error("can't write to COM port =/ "+e.getMessage());
        }
    }*/

    @Override
    public void write(int[] msg) throws SerialPortException {
        comm.write(msg);
    }

    @Override
    public boolean sendBreak(int duration) {
        return comm.sendBreak(duration);
    }

    @Override
    public void close(){
        if(serialPort != null){
            try {
                outputStream.flush();
                outputStream.close();
            }catch (IOException e){
                logger.error("Some problem with closing connection ='\'"+e.getMessage());
            }
            serialPort.removeEventListener();
            serialPort.close();
            stopExecutingCommand();
            serialPort = null;
            inputStream = null;
        }
    }

    static class SerialReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte [] buffer = new byte[6];
            int data;
            try {
                logger.trace("have a new mail");
                data = inputStream.read(buffer);
                logger.trace("data = " + data);
                if(data > 0){
                    changeUI(dataParser.getStringPosition(buffer, data));
                }
            } catch (IOException e){
                logger.error("IOException is occurred: " + e.getMessage());
            } catch (Exception e) {
                logger.error("Something goes wrong while reading: " + e.getMessage());
            }
        }
    }
}
