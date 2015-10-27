package root.util;


import javafx.collections.ObservableList;

import java.util.Map;

public interface TimeSetConfigCollection {

    public ObservableList<String> getTimeConfigList();

    public Map<String, Integer> getTimeConfigMap();

}
