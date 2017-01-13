package root.model;


import javafx.application.Platform;
import jssc.SerialPortException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.controller.Graphics;
import root.response.ResponseHandler;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class CommAbstract implements Comm<Graphics> {

    private static final Logger logger = LogManager.getLogger();
    private static Graphics gui;
    private static Timer timer;
    private static ExecutorService executor;
    private static TimerTask timerTask;
    private int[] send;

    @Override
    public Graphics getGui(){
        return gui;
    }

    @Override
    public void setGui(Graphics graphics) {
        gui = graphics;
    }

    @Override
    public void executeCommand(final int[] send, long period, ResponseHandler responseHandler){
        long delay = period;
        /*int f = CommUtilAbstract.getTimeConfigMap().get(factor);
        delay *= f;
         period = delay;*/

        executor = Executors.newCachedThreadPool();
        timer = new Timer();
        this.send = send;
        timer.scheduleAtFixedRate(getTimerTask(responseHandler), delay, period);
    }

    private synchronized TimerTask getTimerTask(ResponseHandler responseHandler){
        return timerTask == null ? timerTask = new TimerTask() {
            @Override
            public void run() {
                executor.execute(new Runnable() {
                    public void run() {
                        try {
                            addResponseHandler(responseHandler);
                            write(send);
                        } catch (SerialPortException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } : timerTask;
    }

    public static void changeUI(final String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gui.updateFirstTerminal(input);
                    }
                });
            }
        }).start();
    }

    @Override
    public void stopExecutingCommand(){
        if(timer != null) {
            timer.cancel();
        }
        if(timerTask!= null) timerTask.cancel();
        if(executor!=null) executor.shutdown();
        timerTask = null;
        timer = null;
        executor = null;
    }

    /*abstract void removeResponseHandlers();*/
}