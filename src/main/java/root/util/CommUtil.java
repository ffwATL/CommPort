package root.util;


import gnu.io.CommPortIdentifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class CommUtil extends CommUtilAbstract {

    private static CommUtil commUtil;
    private static final Logger logger = LogManager.getLogger();

    private CommUtil(){
    }

    public static CommUtil getInstance(){
        return commUtil == null ? commUtil = new CommUtil() : commUtil;
    }

    @Override
    public ObservableList<String> getPortName(){
        ObservableList<String> portList = FXCollections.observableArrayList();
        java.util.Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier portIdentifier = portEnum.nextElement();
            logger.debug("found port type: " + portIdentifier.getPortType() + "port name: " + portIdentifier.getName());
            if(portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                portList.add(portIdentifier.getName());
            }
        }
        return portList;
    }
}
