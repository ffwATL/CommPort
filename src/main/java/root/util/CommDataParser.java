package root.util;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommDataParser implements DataParser{

    private static final Logger logger = LogManager.getLogger();
    private static final int MAX_VALUE_A = 8355711;
    private static final int MAX_VALUE_H = 8355711;
    private static CommDataParser dataParser;

    private CommDataParser(){
    }

    public static CommDataParser getInstance(){
        return dataParser == null ? dataParser = new CommDataParser() : dataParser;
    }

    private static double [] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (arr.length < data) throw new Exception("wrong array length given: "+ arr.length);
        long value = 0;
        double [] position = new double[data / 3];
        int c = 0;
        if(data == 3 || data == 6){
            for (int i = 0; i < data; i++){
                value = (value << 8) + (arr[i] & 0xff);
                if((i+1) % 3 == 0) {
                    position[c++] = (double) value;
                    value = 0;
                    /*logger.trace("in cycle " + i);*/
                }
               /* logger.trace("value = " + value);*/
            }
        }else logger.warn("Wrong data length: "+ data);
        return position;
    }

    @Override
    public double[] getDoublePosition(byte[] input, int data){
        double [] parsed;
        double[] position = null;
        int c = 0;
        try {
            parsed = parseSixByteArray(input, data);
            logger.trace("input[0] = " + parsed[0]);
            if(parsed.length > 1) logger.trace("input[1] = " + parsed[1]);
            position = new double[parsed.length];
            for(int i = 0; i < position.length; i++){
                if(c++ % 2 == 0){
                    position[i] = 360.0 * (parsed[i] / MAX_VALUE_A);
                }else position[i] = 360.0 * (parsed[i] / MAX_VALUE_H);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return position;
    }

    @Override
    public String getStringPosition(byte[] input, int data){
        StringBuilder builder = new StringBuilder();
        double[] position = getDoublePosition(input, data);
        for (int i = 0; i < position.length; i++){
            builder.append(position[i]);
            builder.append("\n");
        }
        return builder.toString();
    }
}
