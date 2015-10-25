import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.bind.DatatypeConverter;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();
    static private String s="126";

    public static void main(String[]args){
        byte a = 0x7e;
        byte b = new Byte(s);
        byte [] array = new byte[] { b};
        logger.warn(b);
        /*Byte  b2 =Byte.decode(DatatypeConverter.printHexBinary(array));*/
        for (byte  as: DatatypeConverter.printHexBinary(array).getBytes()){
            logger.trace("converting as: " + as);
        }

        /*logger.trace("converting: " + b2);*/
    }
}
