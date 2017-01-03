package root.util;


import org.apache.commons.lang.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Command;

public class CommDataParser implements DataParser {

    private static final Logger logger = LogManager.getLogger();
    private static double MAX_VALUE_A/* = 1800000/2*/;
    private static double MAX_VALUE_H/* = 1800000/2*/;
    private static CommDataParser dataParser;

    private final static char[] digits = {
            '0' , '1' , '2' , '3' , '4' , '5' ,
            '6' , '7' , '8' , '9' , 'a' , 'b' ,
            'c' , 'd' , 'e' , 'f' , 'g' , 'h' ,
            'i' , 'j' , 'k' , 'l' , 'm' , 'n' ,
            'o' , 'p' , 'q' , 'r' , 's' , 't' ,
            'u' , 'v' , 'w' , 'x' , 'y' , 'z'
    };

    private CommDataParser(){}

    public static CommDataParser getInstance(){
        return dataParser == null ? dataParser = new CommDataParser() : dataParser;
    }

    private static double [] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (arr.length < data) throw new Exception("Wrong array length given: "+ arr.length);
        long value = 0;
        double [] position = new double[data / 3];
        int c = 0;
        if(data == 3 || data == 6){
            for (int i = 0; i < data; i++){
                value = (value << 8) + (arr[i] & 0xff);
                if((i+1) % 3 == 0) {
                    position[c++] = (double) value;
                    value = 0;
                }
            }
        }else logger.warn("Wrong data length: "+ data);
        return position;
    }

    @Override
    public String setMaxValues(byte[] arr, int data){
        StringBuilder builder = new StringBuilder();
        try {
            double[] parsed = parseSixByteArray(arr, data);
            MAX_VALUE_A = 1;
            MAX_VALUE_H = 1;
            if(parsed != null){
                if(parsed[0] != 0) MAX_VALUE_A = parsed[0];
                if(parsed[1] != 0) MAX_VALUE_H = parsed[1];
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        builder.append(MAX_VALUE_A);
        builder.append("   ");
        for (int i = 0; i < arr.length; i++){
            builder.append(arr[i] & 0xff).append(" ");
            if(i == 2) builder.append("\n").append(MAX_VALUE_H).append("   ");
        }
        return builder.toString();
    }

    @Override
    public Double[] getDoublePosition(byte[] input, int data){
        double [] parsed;
        Double [] position = null;
        int c = 0;
        try {
            parsed = parseSixByteArray(input, data);
            position = new Double[parsed.length];
            for(int i = 0; i < position.length; i++){
                if(c++ % 2 == 0){
                    position[i] = 360.0 * (parsed[i] / MAX_VALUE_A);
                }else position[i] = 360.0 * (parsed[i] / MAX_VALUE_H);
            }
        } catch (Exception e) {
            logger.error("Error is occurred while parsing: " + e.getMessage());
        }
        return position;
    }

    @Override
    public String getStringPosition(byte[] input, int data){
        StringBuilder builder = new StringBuilder();
        Double [] position = getDoublePosition(input, data);
        builder.append("A: ");
        for (int i = 0; i < position.length; i++){
            if(i==0) builder.append(position[i]);
            else builder.append("H: ").append(position[i]);
            if(i != position.length - 1) builder.append("\n");
        }
        return builder.toString();
    }

    @Override
    public int[] calculateFreq(int freq){
        long n = Math.abs(10000000 / freq);
        return convertLongToUnsignedIntegerArrayFreq(n, Command.SET_FREQUENCY);
    }
    @Override
    public int[] convertLongToUnsignedIntegerArrayFreq(long value, final int command){

        char[] hexArray = toHexCharArray(value);
        int[] result = new int[4];
        int count = 1;
        result[0] = command;
        StringBuilder builder = new StringBuilder();
        for(int i=0; i< hexArray.length; i++){
            builder.append(hexArray[i]);
            if(builder.length() == 2 || i == hexArray.length - 1){
                int arr_byte = Integer.parseInt(builder.toString(), 16);
                result[count++] = arr_byte;
                if(i < hexArray.length - 1) builder = new StringBuilder();
            }
        }
        /*logger.info("before: "+ Arrays.toString(result));*/
        count = result[1];
        result[1] = result[2];
        result[2] = count;
        count = result[1];

        result[1] = result[3];
        result[3] = count;

        return result;
    }

    @Override
    public int[] convertLongToUnsignedIntegerArrayImpulse(long value, final int command){

        char[] hexArray = toHexCharArray(value);
        ArrayUtils.reverse(hexArray);
        int count = 1;
       /* int size = hexArray.length;
        size = size % 2 > 0 ? (size + 1) / 2 + 1 : size / 2 + 1;*/

        int[] result = new int[4];
        result[0] = command;

        StringBuilder builder = new StringBuilder();
        for(int i=0; i < hexArray.length; i+=2){

            if(i+1 < hexArray.length) builder.append(hexArray[i + 1]);
            builder.append(hexArray[i]);
            if(builder.length() == 2 || i == hexArray.length-1) {
                int arr_byte = Integer.parseInt(builder.toString(), 16);
                result[count++] = arr_byte;
                if(i < hexArray.length-1){
                    builder = new StringBuilder();
                }
            }
        }
        return result;
    }


    @Override
    public char[]  toHexCharArray(long i) {
        return toUnsignedString0(i, 4);
    }

    private char[] toUnsignedString0(long val, int shift) {
        int mag = Long.SIZE - Long.numberOfLeadingZeros(val);
        int chars = Math.max(((mag + (shift - 1)) / shift), 1);
        char[] buf = new char[chars];

        formatUnsignedLong(val, shift, buf, 0, chars);

        // Use special constructor which takes over "buf".
        return buf;
    }

    private int formatUnsignedLong(long val, int shift, char[] buf, int offset, int len) {
        int charPos = len;
        int radix = 1 << shift;
        int mask = radix - 1;
        do {
            buf[offset + --charPos] = digits[((int) val) & mask];
            val >>>= shift;
        } while (val != 0 && charPos > 0);

        return charPos;
    }
}