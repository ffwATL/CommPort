package root.util;

import gnu.io.CommPortIdentifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public final class DiscoveringComm {

    private DiscoveringComm(){};

    private static Logger logger = LogManager.getLogger();

    public static ObservableList<String> getPortName(){
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

    public static ObservableList<Integer> getBaudRateList(){
        ObservableList<Integer> baudRateList = FXCollections.observableArrayList();
        baudRateList.add(1200);
        baudRateList.add(2400);
        baudRateList.add(4800);
        baudRateList.add(9600);
        baudRateList.add(14400);
        baudRateList.add(19200);
        baudRateList.add(28800);
        baudRateList.add(38400);
        baudRateList.add(57600);
        baudRateList.add(115200);
        baudRateList.add(230400);
        return baudRateList;
    }

    public static List<TimeSetConfig> getTimeConfigMap(){
        List<TimeSetConfig> list = new ArrayList<>();
        list.add(new TimeSetConfig("Milliseconds", 1));
        list.add(new TimeSetConfig("Seconds", 1000));
        return list;
    }
}
