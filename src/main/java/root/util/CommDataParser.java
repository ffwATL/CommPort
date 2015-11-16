package root.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommDataParser implements DataParser {

    private static final Logger logger = LogManager.getLogger();
    private static double MAX_VALUE_A/* = 1800000/2*/;
    private static double MAX_VALUE_H/* = 1800000/2*/;
    private static CommDataParser dataParser;

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
            if(parsed != null){
                MAX_VALUE_A = parsed[0];
                MAX_VALUE_H = parsed[1];
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        builder.append(MAX_VALUE_A);
        builder.append("   ");
        for (int i = 0; i < arr.length; i++){
            builder.append((arr[i] & 0xff) + " ");
            if(i == 2) builder.append("\n" + MAX_VALUE_H + "   ");
        }
        return builder.toString();
    }

    @Override
    public double[] getDoublePosition(byte[] input, int data){
        double [] parsed;
        double[] position = null;
        int c = 0;
        try {
            parsed = parseSixByteArray(input, data);
            position = new double[parsed.length];
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
        double[] position = getDoublePosition(input, data);
        builder.append("A: ");
        for (int i = 0; i < position.length; i++){
            if(i==0) builder.append(position[i]);
            else builder.append("H: "+position[i]);
            if(i != position.length - 1) builder.append("\n");
        }
        return builder.toString();
    }
}