import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();
    static private String s="126";
    static private byte[] foo = {Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE};
    static private byte[] foo2 = {(byte)0x5f, (byte) 0xf1,03};
    static private byte [] test = {12,-67,-21,11,23,14};


    public static void main(String[]args) throws Exception {
        int a = 0;
        byte b = (byte) a;
        /*logger.trace(b & 0xFF);*/
        double res1 = new BigInteger(foo2).doubleValue();
        logger.trace("res1 = " + res1);
        logger.trace("my result: " +  test(foo2, 3)[0]);

        /*for(double position: Calculating.getPosition(foo2,6)){
            logger.trace(position);
        }*/
        /*long res = foo2[0]*256*256;*/



    }

    public static double[] test(byte []arr, int data){
        long value = 0;
        double [] position = new double[data / 3];
        int c = 0;
        if(/*data == 3 | data == 6*/true){
            for (int i = 0; i < data; i++){
                /*if(i % 3 == 0 && i > 0) {
                    position[c++] = (double) value;
                    value = 0;
                }*/
                value = (value << 8) + (arr[i] & 0xff);
            }
        }
        position[0] = value;
        return position;
    }

    private static long getLong(byte[] arr){
        System.err.println("start");
        long res = (arr[0]*256 + arr[1]) * 256 + arr[2];
        System.err.println("end");
        return res;
    }

    private static long[] parseSixByteArray(byte[] arr, int data) throws Exception {
        if (data % 3 != 0 || arr.length < data) throw new Exception("wrong array length given: "+ arr.length);
        long[] res = new long[data / 3];
        int c = 0;
        for (int i=0; i < data; i += 3){
            res[c++] = (arr[i]*256 + arr[i+1]) * 256 + arr[i+2];
        }
        return res;
    }



}
