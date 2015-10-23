package root;

import gnu.io.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.TooManyListenersException;

public class CommTest  {

    private static Logger logger = LogManager.getLogger();

    private GraphicsInterface gui;

    public void setGui(GraphicsInterface gui){
        this.gui = gui;
    }

    public static void main(String[] args){
       /* try
        {
            (new CommTest()).connect(DiscoveringComm.getPortName());
        }
        catch (Exception e)
        {
           logger.error("Exception is occurred.."+e.getMessage());
        }*/

    }


    private void connect(String portName) throws NoSuchPortException, PortInUseException, UnsupportedCommOperationException, IOException, InterruptedException, TooManyListenersException {
        CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if(portIdentifier.isCurrentlyOwned()){
            logger.error(portName+" currently in use!");
        }else {
            CommPort commPort = portIdentifier.open(this.getClass().getName(),2000);
            if(commPort instanceof SerialPort){
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8,SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                InputStream inputStream = serialPort.getInputStream();
                OutputStream out = serialPort.getOutputStream();
                logger.info("Connected to " + portName);
                serialPort.addEventListener(new SerialReader(inputStream));
                serialPort.notifyOnDataAvailable(true);
            }
        }
    }

    public static class SerialReader implements SerialPortEventListener {

        private InputStream in;
        byte[] buffer = new byte[1024];

        public SerialReader (InputStream in){
            this.in = in;
        }

        @Override
        public void serialEvent(SerialPortEvent serialPortEvent) {
            int data;
            try{
                logger.trace("trying to read..");
                int len = 0;
                while ((data = in.read())>-1){
                    if(data =='\n') break;
                    buffer[len++] = (byte) data;
                    logger.trace("Reading: " + buffer[len-1]);
                }
            }catch (IOException e){
                logger.error("IOException is occurred: "+e.getMessage());
            }
        }
    }

    /** */
    public static class SerialWriter implements Runnable
    {
        private OutputStream out;

        public SerialWriter (OutputStream out){
            this.out = out;
        }

        public void run (){
            try {
                int c = 0;
                logger.trace("writing to COM3.. ");
                this.out.write(3);
                out.flush();
                Thread.sleep(50);
                while ( ( c = System.in.read()) > -1 ){
                    this.out.write(c);
                    logger.trace("writing to COM: "+c);
                }
                /*out.flush();*/
            }
            catch ( IOException e )
            {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
