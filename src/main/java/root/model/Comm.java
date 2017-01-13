package root.model;


import jssc.SerialPortException;
import root.controller.Graphics;
import root.response.ResponseHandler;

public interface Comm<T extends Graphics> {

    void setGui(T gui);

    T getGui();

    boolean connect(String portName, Integer baudRate);

    void addResponseHandler(ResponseHandler responseHandler);
    void removeResponseHandlers();

    boolean isConnected();

    void write(int[] msg) throws SerialPortException;

    int getQueueSize();

    boolean sendBreak(int duration);

    void close();

    void executeCommand(int[] send, long period, ResponseHandler responseHandler);

    void stopExecutingCommand();
}
