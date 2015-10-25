package root.model;


import root.view.Graphics;

public interface Comm<T extends Graphics> {

    public void setGui(T gui);

    public boolean connect(String portName, Integer baudRate);

    public void write(String  b);

    public void close();
}
