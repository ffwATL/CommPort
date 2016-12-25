package root.model;


import jssc.SerialPortException;
import root.controller.Graphics;
import root.response.ResponseHandler;

public interface Comm<T extends Graphics> {

    void setGui(T gui);

    T getGui();

    boolean connect(String portName, Integer baudRate);

    void addResponseHandler(ResponseHandler responseHandler);
    void removeResponseHandler(ResponseHandler responseHandler);

    boolean isConnected();

    void write(String  b) throws SerialPortException;
    void write(int[] msg) throws SerialPortException;

    void close();

    void executeCommand(String send, long delay, long period, String factor);

    void stopExecutingCommand();
}
