package root.model;


import javafx.application.Platform;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.CommUtil;
import root.controller.Graphics;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public abstract class CommAbstract implements Comm<Graphics> {

    private static final Logger logger = LogManager.getLogger();
    private static Graphics gui;
    private static Timer timer;

    @Override
    public Graphics getGui(){
        return gui;
    }

    @Override
    public void setGui(Graphics gui) {
        this.gui = gui;
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

    public static void changeUI(String input){
        new Thread(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gui.updateTerminal(input);
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
    }

}
