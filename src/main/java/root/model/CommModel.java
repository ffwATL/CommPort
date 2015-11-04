package root.model;


import gnu.io.*;
import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.CommUtil;
import root.view.Graphics;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TooManyListenersException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CommModel implements Comm<Graphics> {

    private static final Logger logger = LogManager.getLogger();
    private static Comm<Graphics> comm;

    private OutputStream outputStream;
    private Graphics gui;
    private SerialPort serialPort;
    private SerialReader serialReader;
    private BufferedInputStream inputStream;
    private Timer timer;

    private CommModel(){
    }

    public static Comm getInstance(){
        return comm == null ? comm = new CommModel() : comm;
    }

    @Override
    public void setGui(Graphics gui) {
        this.gui = gui;
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
                serialReader = new SerialReader();
                serialPort.addEventListener(serialReader);
                serialPort.notifyOnDataAvailable(true);
                serialPort.setInputBufferSize(6);
                serialPort.enableReceiveThreshold(6);
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
        logger.trace("starting conversion..");
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
            }catch (IOException e){
                logger.error("Some problem with closing connection ='\'"+e.getMessage());
            }
            serialPort.removeEventListener();
            serialPort.close();
            stopExecutingCommand();
            serialPort = null;
        }
    }

    private static long[] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (data % 3 != 0 || arr.length < data) throw new Exception("wrong array length given: "+ arr.length);
        long[] res = new long[data / 3];
        int c = 0;
        for (int i = 0; i < data; i += 3){
            res[c++] = (arr[i] * 256 + arr[i + 1]) * 256 + arr[i + 2];
        }
        return res;
    }

    private void changeUI(double[] input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < input.length; i++){
                            gui.updateTerminal(String.valueOf(input[i]) + " - ");
                        }
                        gui.updateTerminal("\n");
                    }
                });
            }
        }).start();
    }

    @Override
    public void executeCommand(String send, long delay, long period, String factor){
        int f = CommUtil.getInstance().getTimeConfigMap().get(factor);
        delay *= f;
        period = delay;
        Executor executor = Executors.newCachedThreadPool();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                executor.execute(new Runnable() {
                    public void run() {
                        write(send);
                        logger.trace("inside executor");
                    }
                });
            }
        }, delay, period);
    }

    @Override
    public void stopExecutingCommand(){
        if(timer != null) {
            timer.cancel();
        }
    }

    class SerialReader implements SerialPortEventListener {
        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            logger.trace("have a new mail");
            byte [] buffer = new byte[6];
            int data;
            try {
                logger.trace("start reading");
                while (true){
                    data = inputStream.read(buffer);
                    if(data == -1) break;
                    if(data > 0){
                        changeUI(Calculating.getPosition(buffer, data));
                        /*logger.trace("data = " + data);*/
                    }
                    break;
                }
                logger.trace("finish reading");


            } catch (IOException e){
                logger.error("IOException is occurred: " + e.getMessage());
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
