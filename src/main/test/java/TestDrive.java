import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.util.LoadData;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();

    public static void main(String[]args){
        logger.debug("getting list of person: "+ LoadData.getList());
    }
}
