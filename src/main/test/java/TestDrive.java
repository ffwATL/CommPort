import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.model.Comm;
import root.model.JsscCommModel;
import root.util.JsscCommUtil;

public class TestDrive {
    private static Logger logger = LogManager.getLogger();
    static private String s="126";
    static private byte[] foo = {Byte.MAX_VALUE, Byte.MAX_VALUE, Byte.MAX_VALUE};
    static private byte[] foo2 = {(byte)0x5f, (byte) 0xf1,03};
    static private byte [] test = {12,-67,-21,11,23,14};


    public static void main(String[]args) throws Exception {
       /* int a = 0;
        byte b = (byte) a;
        *//*logger.trace(b & 0xFF);*//*
        double res1 = new BigInteger(foo2).doubleValue();
        String[] names = SerialPortList.getPortNames();
        logger.trace(names[0]);*/

        Comm comm = JsscCommModel.getInstance();
        comm.connect(JsscCommUtil.getInstance().getPortName().get(0), 9600);
        comm.write("3");
        Thread.sleep(20);
        comm.write("3");
        Thread.sleep(20);
        comm.close();
        /*Comm comm = RxTxCommModel.getInstance();
        comm.connect(RxTxCommUtil.getInstance().getPortName().get(0),9600);
        comm.write("3");
        Thread.sleep(20);
        comm.write("3");
        Thread.sleep(20);
        comm.close();*/
    }





}
