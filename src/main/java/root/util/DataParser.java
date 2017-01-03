package root.util;


public interface DataParser {

    Double[] getDoublePosition(byte[] input, int data);

    String getStringPosition(byte[] input, int data);

    String setMaxValues(byte[] arr, int data);

    /*int[] convertLongToUnsignedIntegerArrayImpulse(long value, int command);*/

    int[] convertLongToUnsignedIntegerArrayFreq(long value, final int command);

    int[] convertLongToUnsignedIntegerArrayImpulse(long value, int command);

    char[]  toHexCharArray(long i);

   /* int[] convertLongToUnsignedIntegerArrayImpulse(long value);*/

    int[] calculateFreq(int freq);
}
