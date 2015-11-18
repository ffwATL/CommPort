import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();


    public static void main(String[]args) throws Exception {
        File file = new File("E:/service.log");
        parseFile(file);

    }

    private static void parseFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bf = new BufferedReader(reader);
        String line;
        OutputStream outputStream = new FileOutputStream(new File("E:/out_with_code.log"));
        OutputStreamWriter writer = new OutputStreamWriter(outputStream);
        BufferedWriter bw = new BufferedWriter(writer);
        while ((line = bf.readLine()) != null) {
            int b = line.indexOf("A:") + "A:".length();
            int e = line.indexOf("H:")/* + "H:".length()*/;
            int rawB = line.indexOf("w: ") + "w: ".length();
            int rawE = line.indexOf("    ")+2;
            /*bw.write(line.substring(b+2,e)+"\n");*/
            bw.write(line.substring(rawB, 18) + "    " + line.substring(b, e) + "\n");
            /*logger.trace(line.substring(b+2,e));*/
        }
        bw.flush();
        bw.close();
    }





}
