package root.util;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jssc.SerialPortList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class JsscCommUtil extends CommUtilAbstract{

    private static final Logger logger = LogManager.getLogger();
    private static JsscCommUtil jsscCommUtil;

    private JsscCommUtil(){
    }

    public static JsscCommUtil getInstance(){
        return jsscCommUtil == null ? jsscCommUtil = new JsscCommUtil() : jsscCommUtil;
    }

    @Override
    public ObservableList<String> getPortName() {
        ObservableList<String> list = FXCollections.observableArrayList();
        for (String name: SerialPortList.getPortNames()){
            logger.debug("found port name: " + name);
            list.add(name);
        }
        return list;
    }
}
