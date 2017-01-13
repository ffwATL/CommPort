package response;

import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.CommAbstract;
import root.response.ResponseHandler;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;


public class TestCommModel extends CommAbstract {

    private static final Logger logger = LogManager.getLogger();
    private volatile Queue<ResponseHandler> responseHandlers = new ConcurrentLinkedQueue<>();


    @Override
    public boolean connect(String portName, Integer baudRate) {
        return false;
    }

    @Override
    public void addResponseHandler(ResponseHandler responseHandler) {
        responseHandlers.add(responseHandler);
    }

    @Override
    public void removeResponseHandlers() {
        responseHandlers.clear();

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void write(int[] msg) throws SerialPortException {
        String type = msg[0] == 0 ? "SECOND" : "FIRST";
        logger.info("send: " + type);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                handleResponse(1, 2, 3);
            }
        }).start();
    }

    @Override
    public int getQueueSize() {
        return responseHandlers.size();
    }

    private synchronized void handleResponse(int ... response){
        if(!responseHandlers.isEmpty()){
            try {
                responseHandlers.poll().handle(response);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean sendBreak(int duration) {
        return false;
    }

    @Override
    public void close() {

    }
}
