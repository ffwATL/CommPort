package root.model.property;


import root.response.ResponseHandler;

public class CommandProperty {

    private final int[] command;

    private final ResponseHandler responseHandler;

    public CommandProperty(int[] command, ResponseHandler responseHandler) {
        this.command = command;
        this.responseHandler = responseHandler;
    }

    public int[] getCommand() {
        return command;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }
}