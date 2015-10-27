import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.CommUtil;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();
    static private String s="126";

    public static void main(String[]args){
        logger.trace(CommUtil.getInstance() == null);


    }
}
