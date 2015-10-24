import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();
    static private String s="126";

    public static void main(String[]args){
        byte b = Byte.valueOf(s);
        byte [] array = new byte[] { 1, 2, 3 };
        logger.warn(b);
        /*logger.trace("converting: " + DatatypeConverter.printHexBinary(s.toCharArray()));*/
    }
}
