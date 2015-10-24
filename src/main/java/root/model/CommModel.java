package root.model;

import gnu.io.*;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.view.GraphicsInterface;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class CommModel implements Comm<GraphicsInterface> {

    private static Logger logger = LogManager.getLogger();
    private OutputStream outputStream;
    private GraphicsInterface gui;
    private SerialPort serialPort;
    private SerialReader serialReader;
    private InputStream inputStream;
    private static Comm<GraphicsInterface> comm;

    private CommModel(){
    }

    public static Comm getInstance(){
        if(comm == null) comm = new CommModel();
        return comm;
    }

    @Override
    public void setGui(GraphicsInterface gui) {
        this.gui = gui;
    }

    @Override
    public boolean connect(String portName){
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            if(commPort instanceof SerialPort){
                serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
                serialReader = new SerialReader();
                serialPort.addEventListener(serialReader);
                serialPort.notifyOnDataAvailable(true);
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
    public void write(String  b){
        byte [] arr = {Byte.valueOf(b)};
        String hex = DatatypeConverter.printHexBinary(arr);
        try {
           logger.trace("writing: " + hex);
           outputStream.write(Byte.valueOf(hex));
        }catch (IOException e){
            logger.error("can't write to COM port =/ "+e.getMessage());
        }
    }

    @Override
    public void close(){
        if(serialPort != null){
            try {
                outputStream.flush();
                outputStream.close();
                inputStream.close();
            }catch (IOException e){
                logger.error("Some problem with closing connection ='\'"+e.getMessage());
            }
            serialPort.removeEventListener();
            serialPort.close();
            serialPort = null;
        }
    }

    private void changeUI(byte [] input, int end){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < end; i++){
                            gui.updateTerminal(String.valueOf(input[i]) + " ");
                            if(i + 1 == end){
                                logger.trace("getting new line.. " + i);
                                gui.updateTerminal("\n");
                            }
                        }
                    }
                });
            }
        }).start();
    }

    class SerialReader implements SerialPortEventListener {

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            byte [] buffer = new byte[7];
            int len = 0;
            try {
                while ((buffer[len++] = (byte) inputStream.read()) > -1);
                len-=1;
            } catch (IOException e){
                logger.error("IOException is occurred: " + e.getMessage());
            }
            changeUI(buffer, len);
        }
    }
}
