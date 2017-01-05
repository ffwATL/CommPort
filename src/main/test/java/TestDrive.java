import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.ephem.Ephem;
import root.ephem.EphemObject;

import java.io.*;
import java.util.HashMap;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();

    static int count = 1;
    static {
        count =0;
    }

    public static void main(String[]args) throws Exception {
        Integer s1 = 0;
        Integer s2 = 7;
        Integer s3 = 208;

        byte[] test = new byte[] {s1.byteValue(),s2.byteValue(), s3.byteValue()};
        int res = s1*256+s2*16+s3;
        byte[] aa = hex2ByteArray("7d0");
        int[] qq = new int[aa.length];
        for(int i=0; i< aa.length; i++){
            qq[i] = aa[i] &0xFF;
        }
        System.err.println(valueOfThreeBytes(test));
    }


    public static byte[] hex2ByteArray( String input ) {
        int len = input.length();

        if (len == 0) {
            return new byte[] {};
        }

        byte[] data;
        int startIdx;
        if (len % 2 != 0) {
            data = new byte[(len / 2) + 1];
            data[0] = (byte) Character.digit(input.charAt(0), 16);
            startIdx = 1;
        } else {
            data = new byte[len / 2];
            startIdx = 0;
        }

        for (int i = startIdx; i < len; i += 2) {
            data[(i + 1) / 2] = (byte) ((Character.digit(input.charAt(i), 16) << 4)
                    + Character.digit(input.charAt(i+1), 16));
        }
        return data;
    }

    private static int valueOfThreeBytes(byte ... arr){
        return ((0xFF & arr[0]) << 16) | ((0xFF & arr[1]) << 8) | (0xFF & arr[2]);
    }

    private static void parseEphem(File file, HashMap<String, Ephem> map){
        try(InputStream is = new FileInputStream(file)){
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bf = new BufferedReader(reader);
            String line;
            logger.trace("starting..");
            while ((line = bf.readLine()) != null){
                if(line.length() > 20){
                    int hr = fixString(line.substring(1, 3));
                    int m = fixString(line.substring(4, 6));
                    int s = fixString(line.substring(7, 9));
                    double a = Double.valueOf(line.substring(12, 20));
                    double h = Double.valueOf(line.substring(23, 30));
                    Ephem ephem = new EphemObject(hr, m, s, a, h);
                    map.put(ephem.getKey(),ephem);
                    count++;
                }
            }
            logger.trace("finished.");
        }catch (IOException e){
            logger.error(e.getMessage());
        }
    }

    private static int fixString(String in){
        return in.charAt(0) == ' ' ? Integer.valueOf(in.substring(1)) : Integer.valueOf(in);
    }

    private static void parseEf(File file)throws IOException{
        InputStream is = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(reader);
        String line;
        while ((line = bf.readLine()) != null){
            if(line.length() > 10){

            }
        }
    }

    private static void parseFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(reader);
        String line;
        OutputStream outputStream = new FileOutputStream(new File("E:/comm_temp/logs/out/service_360_out.log"));
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter bw = new BufferedWriter(writer);
        while ((line = bf.readLine()) != null) {
            int b = line.indexOf("H:") + "H:".length();
            int e = line.indexOf("H:")/* + "H:".length()*/;
            int rawB = line.indexOf("w: ") + "w: ".length();
            int rawE = line.indexOf("    ")+2;
            /*bw.write(line.substring(b+2,e)+"\n");*/
            bw.write(line.substring(0, 22) + "    " + line.substring(b) + "\n");
            /*logger.trace(line.substring(b+2,e));*/
        }
        bw.flush();
        bw.close();
    }
}