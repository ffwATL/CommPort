package root.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Calculating {

    private static final Logger logger = LogManager.getLogger();
    private static final int MAX_VALUE_A = 8355711;
    private static final int MAX_VALUE_H = 8355711;

    private static double [] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (arr.length < data) throw new Exception("wrong array length given: "+ arr.length);
        long value = 0;
        double [] position = new double[/*data / 3*/1];
        int c = 0;
        for (int i = 0; i < 3; i++){
            value = (value << 8) + (arr[i] & 0xff);
        }
        position[0] = value;
        /*if(data == 3 || data == 6){
            for (int i = 0; i < data; i++){
                if(i % 3 == 0 && i > 0) {
                    position[c++] = (double) value;
                    value = 0;
                }
                value = (value << 8) + (arr[i] & 0xff);
            }
        }else logger.warn("Wrong data length: "+ data);*/
        return position;
    }

    public static double[] getPosition(byte[] input, int data){
        double [] parsed = null;
        double[] position = null;
        int c = 0;

        try {
            parsed = parseSixByteArray(input, data);
            logger.trace("input[0] = " + parsed[0]);
            position = new double[parsed.length];
            for(int i = 0; i < position.length; i++){
                if(c++ % 2 == 0){
                    position[i] = 360 * (parsed[i] / MAX_VALUE_A);
                }else position[i] = 360 * (parsed[i] / MAX_VALUE_H);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return parsed;
    }


    public static String getStringPosition(byte[] input, int data){
        StringBuilder builder = new StringBuilder();
        double[] position = getPosition(input, data);
        for (int i = 0; i < position.length; i++){
            builder.append(position[i]+"\n");
        }
        return builder.toString();
    }
}
