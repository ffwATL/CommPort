package root.model;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Calculating {

    private static final Logger logger = LogManager.getLogger();
    private static final int MAX_VALUE_A = 8355711;
    private static final int MAX_VALUE_H = 8355711;

    private static long[] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (arr.length < data) throw new Exception("wrong array length given: "+ arr.length);
        long[] res;
        if(data == 6){
            logger.trace("data = 6");
            res = new long[data / 3];
            int c = 0;
            for (int i = 0; i < data; i += 3){
                res[c++] = (arr[i] * 256 + arr[i + 1]) * 256 + arr[i + 2];
            }
        }else if(data == 2){
            logger.trace("data = 2");
            res = new long[data / 2];
            int c = 0;
            for (int i = 0; i < data; i += 2){
                res[c++] = (arr[i] * 256 + arr[i + 1]) * 256;
            }
        }else {
            logger.trace("data = 1");
            res = new long[1];
            res[0] = arr[0];
        }
        return res;
    }

    public static double[] getPosition(byte[] input, int data){
        long[] parsed;
        double[] position = null;
        int c = 0;
        try {
            parsed = parseSixByteArray(input, data);
            position = new double[parsed.length];
            for(int i = 0; i < position.length; i++){
                if(c++ % 2 == 0){
                    position[i] = (double) parsed[i] / MAX_VALUE_A;
                }else position[i] = (double) parsed[i] / MAX_VALUE_H;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return position;
    }
}
