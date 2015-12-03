import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Formatter;

public class Test2 extends Thread{
    private static Logger logger = LogManager.getLogger();
    private int b = 0;

    public static void main(String[] args){
        Formatter formatter = new Formatter();
        new Test2().print(3);
        logger.trace("start");
        /*logger.trace(new Formatter().format("%2.2f",Math.PI).toString());*/
        logger.trace(formatter.format("%12.4f", 100.0 / 3.0));
        Integer i1 = 23;
        Integer i2 = 23;
        logger.trace((2>1||3<0) +" hello");
        formatter.flush();
        logger.trace(formatter.toString());
    }

    public void print( Object o ) {
        logger.trace("Object");
    }

    public void print( int str ) {
        logger.trace("int");
    }

    public void print( Integer i ) {
        logger.trace("Integer");
    }
}
