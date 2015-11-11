package root.util;


public interface DataParser {

    public double[] getDoublePosition(byte[] input, int data);

    public String getStringPosition(byte[] input, int data);

}
