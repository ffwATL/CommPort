package response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import root.response.ResponseHandler;


public class SecondResponseHandler implements ResponseHandler {

    private static final Logger logger = LogManager.getLogger();

    private static final String type = "SecondResponseHandler";

    @Override
    public void handle(int[] response) {
        logger.info("***handled, type = "+ type);
    }
}
