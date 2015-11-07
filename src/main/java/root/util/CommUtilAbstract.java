package root.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.HashMap;
import java.util.Map;

public abstract class CommUtilAbstract implements DiscoveringComm {

    private static Map<String, Integer> timeConfigMap;
    private static ObservableList<Integer> baudRateList;


    public static Map<String, Integer> getTimeConfigMap(){
        if(timeConfigMap == null){
            timeConfigMap = new HashMap<>();
            timeConfigMap.put("Milliseconds", 1);
            timeConfigMap.put("Seconds", 1000);
        }
        return timeConfigMap;
    }

    public static ObservableList<String> getTimeConfigList(){
        return FXCollections.observableArrayList(getTimeConfigMap().keySet());
    }

    public static ObservableList<Integer> getBaudRateList(){
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
}
