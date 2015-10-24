package root.model;


import javafx.application.Application;

public interface Comm<T extends Application> {

    public void setGui(T gui);

    public boolean connect(String portName);

    public void write(String  b);

    public void close();
}
