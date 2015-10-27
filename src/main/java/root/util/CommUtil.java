package root.util;


import gnu.io.CommPortIdentifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public final class CommUtil extends CommUtilAbstract {
    private Map<String, Integer> timeConfigMap;
    private static ObservableList<Integer> baudRateList;
    private static CommUtil commUtil;
    private static Logger logger = LogManager.getLogger();

    private CommUtil(){
    }

    public static CommUtil getInstance(){
        return commUtil == null ? commUtil = new CommUtil() : commUtil;
    }

    @Override
    public Map<String, Integer> getTimeConfigMap(){
        if(timeConfigMap == null){
            timeConfigMap = new HashMap<>();
            timeConfigMap.put("Milliseconds", 1);
            timeConfigMap.put("Seconds", 1000);
        }
        return timeConfigMap;
    }

    @Override
    public ObservableList<String> getTimeConfigList(){
        return FXCollections.observableArrayList(getTimeConfigMap().keySet());
    }

    @Override
    public ObservableList<Integer> getBaudRateList(){
        if(baudRateList == null) {
            baudRateList = FXCollections.observableArrayList();
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
        }
        return baudRateList;
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
