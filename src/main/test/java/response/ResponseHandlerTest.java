package response;

import jssc.SerialPortException;

public class ResponseHandlerTest {

    private static final TestCommModel commModel = new TestCommModel();

    public static void main(String[] args) throws SerialPortException {
        ResponseHandlerTest test = new ResponseHandlerTest();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test.consumingTest(true);
                } catch (SerialPortException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int[] second = {0,0,0,0};
                for(int i=0; i< 100; i++) {
                    try {
                        commModel.addResponseHandler(new SecondResponseHandler());
                        commModel.write(second);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    private void consumingTest(boolean first) throws SerialPortException {
        int[] command = {3,4,5};
        if(first) commModel.executeCommand(command, 100, new FirstResponseHandler());
        else commModel.executeCommand(command, 100, new SecondResponseHandler());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        commModel.stopExecutingCommand();
    }
}