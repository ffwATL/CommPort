package root.util;


public interface DataParser {

    public Double[] getDoublePosition(byte[] input, int data);

    public String getStringPosition(byte[] input, int data);

    public String setMaxValues(byte[] arr, int data);

}
