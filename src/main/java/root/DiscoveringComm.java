package root;

import gnu.io.CommPortIdentifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DiscoveringComm {
    private static Logger logger = LogManager.getLogger();

    public static String getPortName(){
        String portName = "";
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements())
        {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            logger.debug("found port type: " + portIdentifier.getPortType()+"port name: "+portIdentifier.getName());
            if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) portName = portIdentifier.getName();
        }
        logger.trace("returning: "+portName);
        return portName;
    }
}
